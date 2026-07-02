package ru.cwcode.tkach.refreshmenu.protocol;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCloseWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowItems;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import ru.cwcode.cwutils.protocol.Packet;
import ru.cwcode.cwutils.server.PaperServerUtils;
import ru.cwcode.tkach.refreshmenu.RefreshMenu;
import ru.cwcode.tkach.refreshmenu.inventory.view.ExtendedView;
import ru.cwcode.tkach.refreshmenu.inventory.view.View;
import ru.cwcode.tkach.refreshmenu.inventory.view.drawer.ExtendedViewDrawer;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class PacketListener extends PacketListenerAbstract {
  private static final ItemStack AIR = new ItemStack(Material.AIR);
  private static final com.github.retrooper.packetevents.protocol.item.ItemStack PACKET_AIR = com.github.retrooper.packetevents.protocol.item.ItemStack.EMPTY;
  private static final Set<String> MODE_TO_RESTORE = Set.of("QUICK_MOVE", "PICKUP_ALL");

  // packetevents normalizes the click packet across versions, so slot/button are read directly;
  // the changed-slots array only exists since 1.17 (as items until 1.21.4, as hashed stacks since 1.21.5)
  private static final boolean HAS_CHANGED_SLOTS = PaperServerUtils.isVersionGreater("1.16.5");

  public PacketListener() {
    super(PacketListenerPriority.NORMAL);
  }

  @Override
  public void onPacketSend(PacketSendEvent event) {
    PacketTypeCommon type = event.getPacketType();

    if (type == PacketType.Play.Server.WINDOW_ITEMS) {
      onWindowItems(event);
    } else if (type == PacketType.Play.Server.OPEN_WINDOW) {
      onOpenWindow(event);
    } else if (type == PacketType.Play.Server.SET_SLOT) {
      onSetSlot(event);
    }
  }

  @Override
  public void onPacketReceive(PacketReceiveEvent event) {
    PacketTypeCommon type = event.getPacketType();

    if (type == PacketType.Play.Client.CLOSE_WINDOW) {
      onCloseWindow(event);
    } else if (type == PacketType.Play.Client.CLICK_WINDOW) {
      onClickWindow(event);
    }
  }

  //replacing items in bottom player inventory to relevant items from extended view
  private void onWindowItems(PacketSendEvent event) {
    WrapperPlayServerWindowItems packet = new WrapperPlayServerWindowItems(event);
    if (packet.getWindowId() < 1) return;

    Player player = event.getPlayer();
    InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
    if (!(holder instanceof View openedView)) return;

    if (!(openedView.getDrawer() instanceof ExtendedViewDrawer extendedViewDrawer)) return;

    List<com.github.retrooper.packetevents.protocol.item.ItemStack> itemStacks = packet.getItems();
    ItemStack[] playerInventoryBuffer = extendedViewDrawer.getPlayerInventoryBuffer();

    int topInventorySize = extendedViewDrawer.getTopInventorySize();

    for (int slot = topInventorySize; slot < itemStacks.size(); slot++) {
      int playerInvSlot = slot - topInventorySize;
      if (!extendedViewDrawer.isPlayerInventorySlotControlled(playerInvSlot)) continue;

      ItemStack element = playerInventoryBuffer[playerInvSlot];
      itemStacks.set(slot, element == null ? PACKET_AIR : SpigotConversionUtil.fromBukkitItemStack(element));
    }

    packet.setItems(itemStacks);
  }

  //catching opened window ID instead of getting it using reflection
  private void onOpenWindow(PacketSendEvent event) {
    WrapperPlayServerOpenWindow packet = new WrapperPlayServerOpenWindow(event);
    Player player = event.getPlayer();

    OpenedWindowService.handleOpen(player, packet.getContainerId(), packet.getType());
  }

  //replacing item in bottom player inventory to relevant items from extended view
  private void onSetSlot(PacketSendEvent event) {
    WrapperPlayServerSetSlot packet = new WrapperPlayServerSetSlot(event);
    if (packet.getWindowId() < 1) return;

    Player player = event.getPlayer();
    InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
    if (!(holder instanceof View openedView)) return;

    if (!(openedView.getDrawer() instanceof ExtendedViewDrawer extendedViewDrawer)) return;

    int topInventorySize = extendedViewDrawer.getTopInventorySize();
    int slot = packet.getSlot();

    if (slot > topInventorySize - 1) {
      int playerInvSlot = slot - topInventorySize;
      ItemStack[] playerInventoryBuffer = extendedViewDrawer.getPlayerInventoryBuffer();
      if (extendedViewDrawer.isPlayerInventorySlotControlled(playerInvSlot)) {
        ItemStack element = playerInventoryBuffer[playerInvSlot];
        packet.setItem(element == null ? PACKET_AIR : SpigotConversionUtil.fromBukkitItemStack(element));
      }
    }
  }

  //remove opened window on close
  private void onCloseWindow(PacketReceiveEvent event) {
    WrapperPlayClientCloseWindow packet = new WrapperPlayClientCloseWindow(event);
    Player player = event.getPlayer();

    OpenedWindowService.handleClose(player, packet.getWindowId());
  }

  //catching bottom inventory click and redirect control to extended view
  private void onClickWindow(PacketReceiveEvent event) {
    WrapperPlayClientClickWindow packet = new WrapperPlayClientClickWindow(event);
    int windowId = packet.getWindowId();
    if (windowId < 1) return;

    Player player = event.getPlayer();

    InventoryHolder holder;
    try {
      holder = player.getOpenInventory().getTopInventory().getHolder();
    } catch (RuntimeException e) { //for catching "Tile is null, asynchronous access?"
      return;
    }

    if (!(holder instanceof View openedView)) return;

    int clickedSlot = packet.getSlot();
    String clickModeName = packet.getWindowClickType().name();
    int button = packet.getButton();

    InventoryView openInventory = player.getOpenInventory();
    ExtendedViewDrawer extendedViewDrawer = openedView.getDrawer() instanceof ExtendedViewDrawer drawer ? drawer : null;
    int topInventorySize = extendedViewDrawer == null ? openInventory.getTopInventory().getSize() : extendedViewDrawer.getTopInventorySize();
    ItemStack[] playerInventoryBuffer = extendedViewDrawer == null ? null : extendedViewDrawer.getPlayerInventoryBuffer();

    boolean uncontrolledPlayerInventoryClick = extendedViewDrawer != null
                                               && extendedViewDrawer.isRawPlayerInventorySlot(clickedSlot)
                                               && !extendedViewDrawer.isRawSlotControlled(clickedSlot);

    if (uncontrolledPlayerInventoryClick && "QUICK_MOVE".equals(clickModeName)) {
      if (HAS_CHANGED_SLOTS) {
        for (Integer slot : readChangedSlots(packet)) {
          restoreWindowSlot(player, openInventory, extendedViewDrawer, topInventorySize, slot, windowId);
        }
      } else {
        restoreTopInventory(player, openInventory, extendedViewDrawer, topInventorySize, windowId);
        restoreWindowSlot(player, openInventory, extendedViewDrawer, topInventorySize, clickedSlot, windowId);
      }

      restoreCursor(player);
      event.setCancelled(true);
      return;
    }

    if (HAS_CHANGED_SLOTS) {
      boolean restoredSlot = false;

      for (Integer slot : readChangedSlots(packet)) {
        if (slot < 0) continue;

        if (extendedViewDrawer != null && extendedViewDrawer.isRawPlayerInventorySlot(slot)) {
          if (extendedViewDrawer.isRawSlotControlled(slot)) {
            int playerInvSlot = slot - topInventorySize;
            Packet.setSlot(player, slot, playerInventoryBuffer[playerInvSlot], windowId);
            restoredSlot = true;
          }
          continue;
        }

        if (slot >= openInventory.countSlots()) continue;

        ItemStack item = openInventory.getItem(slot);
        Packet.setSlot(player, slot, item == null ? AIR : item, windowId);
        restoredSlot = true;
      }

      if (restoredSlot) {
        ItemStack cursorItem = player.getItemOnCursor();
        Packet.setSlot(player, -1, cursorItem == null ? AIR : cursorItem, -1);
      }
    }

    if (!HAS_CHANGED_SLOTS && clickedSlot >= 0 && ("PICKUP".equals(clickModeName) || "QUICK_CRAFT".equals(clickModeName))) {
      boolean restoredSlot = false;

      if (extendedViewDrawer != null && extendedViewDrawer.isRawPlayerInventorySlot(clickedSlot)) {
        if (extendedViewDrawer.isRawSlotControlled(clickedSlot)) {
          int playerInvSlot = clickedSlot - topInventorySize;
          Packet.setSlot(player, clickedSlot, playerInventoryBuffer[playerInvSlot], windowId);
          restoredSlot = true;
        }
      } else if (clickedSlot < openInventory.countSlots()) {
        ItemStack clickedItem = openInventory.getItem(clickedSlot);
        Packet.setSlot(player, clickedSlot, clickedItem == null ? AIR : clickedItem, windowId);
        restoredSlot = true;
      }

      if (restoredSlot) {
        ItemStack cursorItem = player.getItemOnCursor();
        Packet.setSlot(player, -1, cursorItem == null ? AIR : cursorItem, -1);
      }
    }

    if (clickedSlot >= 0 && "SWAP".equals(clickModeName) && button == 40) {
      boolean uncontrolledPlayerSlot = extendedViewDrawer != null
                                       && extendedViewDrawer.isRawPlayerInventorySlot(clickedSlot)
                                       && !extendedViewDrawer.isRawSlotControlled(clickedSlot);

      if (!uncontrolledPlayerSlot) {
        if (extendedViewDrawer == null || !extendedViewDrawer.isRawSlotControlled(clickedSlot)) {
          ItemStack clickedItem = openInventory.getItem(clickedSlot);
          Packet.setSlot(player, clickedSlot, clickedItem == null ? AIR : clickedItem, windowId);
        }

        ItemStack offhandItem = player.getInventory().getItemInOffHand();
        Packet.setSlot(player, 40, offhandItem == null ? AIR : offhandItem, -2);
        int offhandRawSlot = topInventorySize + 40;
        if (offhandRawSlot < openInventory.countSlots()) {
          Packet.setSlot(player, offhandRawSlot, offhandItem == null ? AIR : offhandItem, windowId);
        }
      }
    }

    if (extendedViewDrawer == null) return;

    if (clickedSlot > topInventorySize - 1) {
      if (!extendedViewDrawer.isRawSlotControlled(clickedSlot)) return;

      if (!HAS_CHANGED_SLOTS) {
        boolean shouldRestore = MODE_TO_RESTORE.contains(clickModeName);
        if (shouldRestore) {
          // 1.16.5 and lower do not send changedSlots for shift-click or collect-all.
          for (int slot = 0; slot < topInventorySize; slot++) {
            ItemStack topItem = openInventory.getItem(slot);
            Packet.setSlot(player, slot, topItem == null ? AIR : topItem, windowId);
          }
        }
      }
      if ("SWAP".equals(clickModeName) && button >= 0 && button < 9) {
        int bufferSlot = 27 + button;
        ItemStack item = extendedViewDrawer.getDisplayedPlayerInventoryItem(player, bufferSlot);
        Packet.setSlot(player, topInventorySize + bufferSlot, item == null ? AIR : item, windowId);
      }

      //todo: restore similar items when player use double-click
      Packet.setSlot(player, -1, AIR, -1);
      Packet.setSlot(player, clickedSlot, playerInventoryBuffer[clickedSlot - topInventorySize], windowId);

      ClickType click = button == 0 ? ClickType.LEFT : ClickType.RIGHT;

      if (openInventory.getTopInventory().getHolder() instanceof ExtendedView<?> extendedView) {
        Bukkit.getScheduler().runTask(RefreshMenu.plugin, () -> {
          extendedView.onOwnInventoryClick(new InventoryClickEvent(openInventory, InventoryType.SlotType.CONTAINER, clickedSlot, click, InventoryAction.PICKUP_ALL));
        });
      }

      event.setCancelled(true);
    } else if (clickedSlot >= 0 && "SWAP".equals(clickModeName) && button >= 0 && button < 9) {
      ItemStack topItem = openInventory.getItem(clickedSlot);
      Packet.setSlot(player, clickedSlot, topItem == null ? AIR : topItem, windowId);

      int bufferSlot = 27 + button;
      ItemStack item = extendedViewDrawer.getDisplayedPlayerInventoryItem(player, bufferSlot);
      Packet.setSlot(player, topInventorySize + bufferSlot, item == null ? AIR : item, windowId);
    }
  }

  private Set<Integer> readChangedSlots(WrapperPlayClientClickWindow packet) {
    Optional<Map<Integer, com.github.retrooper.packetevents.protocol.item.ItemStack>> slots = packet.getSlots();
    if (slots.isPresent()) return slots.get().keySet();

    // since 1.21.5 changed slots are carried as hashed stacks instead of full item stacks
    Map<Integer, ?> hashedSlots = packet.getHashedSlots();
    return hashedSlots == null ? Set.of() : hashedSlots.keySet();
  }

  private void restoreTopInventory(Player player, InventoryView openInventory, ExtendedViewDrawer extendedViewDrawer, int topInventorySize, int windowId) {
    for (int slot = 0; slot < topInventorySize; slot++) {
      restoreWindowSlot(player, openInventory, extendedViewDrawer, topInventorySize, slot, windowId);
    }
  }

  private void restoreWindowSlot(Player player, InventoryView openInventory, ExtendedViewDrawer extendedViewDrawer, int topInventorySize, int slot, int windowId) {
    if (slot < 0 || slot >= openInventory.countSlots()) return;

    ItemStack item;
    if (extendedViewDrawer != null && extendedViewDrawer.isRawPlayerInventorySlot(slot)) {
      item = extendedViewDrawer.getDisplayedPlayerInventoryItem(player, slot - topInventorySize);
    } else {
      item = openInventory.getItem(slot);
    }

    Packet.setSlot(player, slot, item == null ? AIR : item, windowId);
  }

  private void restoreCursor(Player player) {
    ItemStack cursorItem = player.getItemOnCursor();
    Packet.setSlot(player, -1, cursorItem == null ? AIR : cursorItem, -1);
  }
}
