package ru.cwcode.tkach.refreshmenu.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
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
import ru.cwcode.tkach.refreshmenu.inventory.view.ExtendedView;
import ru.cwcode.tkach.refreshmenu.inventory.view.View;
import ru.cwcode.tkach.refreshmenu.inventory.view.drawer.ExtendedViewDrawer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PacketListener {
  private static final ItemStack AIR = new ItemStack(Material.AIR);
  private static final Set<String> MODE_TO_RESTORE = Set.of("QUICK_MOVE", "PICKUP_ALL");
  
  private final WindowClickPacketReader windowClickPacketReader = WindowClickPacketReader.create();
  
  public PacketListener() {
    //replacing items in bottom player inventory to relevant items from extended view
    ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(ProtocolLibrary.getPlugin(), PacketType.Play.Server.WINDOW_ITEMS) {
      public void onPacketSending(PacketEvent event) {
        Integer windowID = event.getPacket().getIntegers().read(0);
        if (windowID < 1) return;
        
        InventoryHolder holder = event.getPlayer().getOpenInventory().getTopInventory().getHolder();
        if (!(holder instanceof View openedView)) return;
        
        if (!(openedView.getDrawer() instanceof ExtendedViewDrawer extendedViewDrawer)) return;
        
        List<ItemStack> itemStacks = event.getPacket().getItemListModifier().read(0);
        ItemStack[] playerInventoryBuffer = extendedViewDrawer.getPlayerInventoryBuffer();
        
        int topInventorySize = extendedViewDrawer.getTopInventorySize();
        
        for (int slot = topInventorySize; slot < itemStacks.size(); slot++) {
          int playerInvSlot = slot - topInventorySize;
          if (!extendedViewDrawer.isPlayerInventorySlotControlled(playerInvSlot)) continue;
          
          ItemStack element = playerInventoryBuffer[playerInvSlot];
          itemStacks.set(slot, element == null ? AIR : element);
        }
        
        event.getPacket().getItemListModifier().write(0, itemStacks);
      }
    });
    
    //catching opened window ID instead of getting it using reflection
    ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(ProtocolLibrary.getPlugin(), PacketType.Play.Server.OPEN_WINDOW) {
      public void onPacketSending(PacketEvent event) {
        Player player = event.getPlayer();
        Integer windowID = event.getPacket().getIntegers().read(0);
        Object type = event.getPacket().getModifier().read(1);
        
        OpenedWindowService.handleOpen(player, windowID, type);
      }
    });
    
    //remove opened window on close
    ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(ProtocolLibrary.getPlugin(), PacketType.Play.Client.CLOSE_WINDOW) {
      public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        Integer windowID = event.getPacket().getIntegers().read(0);
        
        OpenedWindowService.handleClose(player, windowID);
      }
    });
    
    //replacing item in bottom player inventory to relevant items from extended view
    ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(ProtocolLibrary.getPlugin(), PacketType.Play.Server.SET_SLOT) {
      public void onPacketSending(PacketEvent event) {
        if (event.getPacket().getIntegers().read(0) < 1) return;
        
        InventoryHolder holder = event.getPlayer().getOpenInventory().getTopInventory().getHolder();
        if (!(holder instanceof View openedView)) return;
        
        if (!(openedView.getDrawer() instanceof ExtendedViewDrawer extendedViewDrawer)) return;
        
        int topInventorySize = extendedViewDrawer.getTopInventorySize();
        
        Integer slot;
        if (PaperServerUtils.isVersionGreater_1_16_5()) {
          slot = event.getPacket().getIntegers().read(2);
        } else {
          slot = event.getPacket().getIntegers().read(1);
        }
        
        if (slot > topInventorySize - 1) {
          int playerInvSlot = slot - topInventorySize;
          ItemStack[] playerInventoryBuffer = extendedViewDrawer.getPlayerInventoryBuffer();
          if (extendedViewDrawer.isPlayerInventorySlotControlled(playerInvSlot)) {
            ItemStack element = playerInventoryBuffer[playerInvSlot];
            event.getPacket().getItemModifier().write(0, element == null ? AIR : element);
          }
        }
      }
    });
    
    //catching bottom inventory click and redirect control to extended view
    ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(ProtocolLibrary.getPlugin(), PacketType.Play.Client.WINDOW_CLICK) {
      public void onPacketReceiving(PacketEvent event) {
        Integer windowId = event.getPacket().getIntegers().read(0);
        if (windowId < 1) return;
        
        Player player = event.getPlayer();
        
        InventoryHolder holder;
        try {
          holder = player.getOpenInventory().getTopInventory().getHolder();
        } catch (RuntimeException e) { //for catching "Tile is null, asynchronous access?"
          return;
        }
        
        if (!(holder instanceof View openedView)) return;
        
        int clickedSlot = windowClickPacketReader.readSlot(event.getPacket());
        
        Enum<?> clickMode = event.getPacket().getSpecificModifier(Enum.class).readSafely(0);
        String clickModeName = clickMode == null ? null : clickMode.name();
        
        int button = windowClickPacketReader.readButton(event.getPacket());
        
        InventoryView openInventory = player.getOpenInventory();
        ExtendedViewDrawer extendedViewDrawer = openedView.getDrawer() instanceof ExtendedViewDrawer drawer ? drawer : null;
        int topInventorySize = extendedViewDrawer == null ? openInventory.getTopInventory().getSize() : extendedViewDrawer.getTopInventorySize();
        ItemStack[] playerInventoryBuffer = extendedViewDrawer == null ? null : extendedViewDrawer.getPlayerInventoryBuffer();
        
        boolean uncontrolledPlayerInventoryClick = extendedViewDrawer != null
                                                   && extendedViewDrawer.isRawPlayerInventorySlot(clickedSlot)
                                                   && !extendedViewDrawer.isRawSlotControlled(clickedSlot);
        
        if (uncontrolledPlayerInventoryClick && "QUICK_MOVE".equals(clickModeName)) {
          if (windowClickPacketReader.hasChangedSlots()) {
            Map<Integer, Object> handle = windowClickPacketReader.readChangedSlots(event.getPacket());
            for (Integer slot : handle.keySet()) {
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
        
        if (windowClickPacketReader.hasChangedSlots()) {
          Map<Integer, Object> handle = windowClickPacketReader.readChangedSlots(event.getPacket());
          boolean restoredSlot = false;
          
          for (Integer slot : handle.keySet()) {
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
        
        if (!windowClickPacketReader.hasChangedSlots() && clickedSlot >= 0 && ("PICKUP".equals(clickModeName) || "QUICK_CRAFT".equals(clickModeName))) {
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
          
          if (!windowClickPacketReader.hasChangedSlots()) {
            boolean shouldRestore = clickModeName != null && MODE_TO_RESTORE.contains(clickModeName);
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
            Bukkit.getScheduler().runTask(plugin, () -> {
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
    });
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
