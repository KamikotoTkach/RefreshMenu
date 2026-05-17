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
        
        int c = 0;
        for (int slot = topInventorySize; slot < itemStacks.size(); slot++) {
          ItemStack element = playerInventoryBuffer[c++];
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
          ItemStack element = extendedViewDrawer.getPlayerInventoryBuffer()[playerInvSlot];
          event.getPacket().getItemModifier().write(0, element == null ? AIR : element);
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
        if (!(openedView.getDrawer() instanceof ExtendedViewDrawer extendedViewDrawer)) return;
        
        int topInventorySize = extendedViewDrawer.getTopInventorySize();
        
        Integer clickedSlot;
        if (PaperServerUtils.isVersionGreater("1.18.1")) {
          clickedSlot = event.getPacket().getIntegers().read(2);
        } else {
          clickedSlot = event.getPacket().getIntegers().read(1);
        }
        
        ItemStack[] playerInventoryBuffer = extendedViewDrawer.getPlayerInventoryBuffer();
        Enum<?> clickMode = event.getPacket().getSpecificModifier(Enum.class).readSafely(0);
        String clickModeName = clickMode == null ? null : clickMode.name();
        
        Integer button;
        if (PaperServerUtils.isVersionGreater("1.17")) {
          button = event.getPacket().getIntegers().read(3);
        } else {
          button = event.getPacket().getIntegers().read(2);
        }
        
        if (clickedSlot > topInventorySize - 1) {
          InventoryView openInventory = player.getOpenInventory();
          
          if (PaperServerUtils.isVersionGreater("1.16.5")) {
            Map<Integer, Object> handle = (Map<Integer, Object>) (event.getPacket().getStructures().read(2).getHandle());
            
            for (Integer slot : handle.keySet()) {
              if (slot < 0) continue;
              
              if (slot < topInventorySize) {
                ItemStack topItem = openInventory.getItem(slot);
                Packet.setSlot(player, slot, topItem == null ? AIR : topItem, windowId);
                continue;
              }
              
              int playerInvSlot = slot - topInventorySize;
              if (playerInvSlot < playerInventoryBuffer.length) {
                Packet.setSlot(player, slot, playerInventoryBuffer[playerInvSlot], windowId);
              }
            }
          } else {
            boolean shouldRestore = MODE_TO_RESTORE.contains(clickModeName);
            if (shouldRestore) {
              // 1.16.5 and lower do not send changedSlots for shift-click or collect-all.
              for (int slot = 0; slot < topInventorySize; slot++) {
                ItemStack topItem = openInventory.getItem(slot);
                Packet.setSlot(player, slot, topItem == null ? AIR : topItem, windowId);
              }
            }
          }
          if ("SWAP".equals(clickModeName)) restoreSwapTarget(player, extendedViewDrawer, button, windowId);
          
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
        } else if (clickedSlot >= 0 && "SWAP".equals(clickModeName)) {
          InventoryView openInventory = player.getOpenInventory();
          ItemStack topItem = openInventory.getItem(clickedSlot);
          Packet.setSlot(player, clickedSlot, topItem == null ? AIR : topItem, windowId);
          restoreSwapTarget(player, extendedViewDrawer, button, windowId);
        }
      }
    });
  }
  
  private void restoreSwapTarget(Player player, ExtendedViewDrawer drawer, int button, int windowId) {
    if (button >= 0 && button < 9) {
      int bufferSlot = 27 + button;
      ItemStack item = drawer.getPlayerInventoryBuffer()[bufferSlot];
      Packet.setSlot(player, drawer.getTopInventorySize() + bufferSlot, item == null ? AIR : item, windowId);
    } else if (button == 40) {
      ItemStack item = player.getInventory().getItemInOffHand();
      Packet.setSlot(player, 40, item == null ? AIR : item, -2);
    }
  }
}
