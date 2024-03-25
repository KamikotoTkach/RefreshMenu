package tkachgeek.refreshmenu.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import tkachgeek.refreshmenu.RefreshMenu;
import tkachgeek.refreshmenu.inventory.view.ExtendedView;
import tkachgeek.refreshmenu.inventory.view.View;
import tkachgeek.refreshmenu.inventory.view.drawer.ExtendedViewDrawer;
import tkachgeek.tkachutils.protocol.Packet;
import tkachgeek.tkachutils.server.ServerUtils;

import java.util.List;
import java.util.Map;

public class PacketListener {
  
  public static final ItemStack AIR = new ItemStack(Material.AIR);
  
  public PacketListener() {
    ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(ProtocolLibrary.getPlugin(), PacketType.Play.Server.WINDOW_ITEMS) {
      public void onPacketSending(PacketEvent event) {
        if (event.getPacket().getIntegers().read(0) < 1) return;
        
        View openedView = RefreshMenu.getApi().getOpenedView(event.getPlayer());
        if (openedView == null || !(openedView.getDrawer() instanceof ExtendedViewDrawer extendedViewDrawer)) return;
        
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
    ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(ProtocolLibrary.getPlugin(), PacketType.Play.Server.SET_SLOT) {
      public void onPacketSending(PacketEvent event) {
        if (event.getPacket().getIntegers().read(0) < 1) return;
        
        View openedView = RefreshMenu.getApi().getOpenedView(event.getPlayer());
        if (openedView == null || !(openedView.getDrawer() instanceof ExtendedViewDrawer extendedViewDrawer)) return;
        
        int topInventorySize = extendedViewDrawer.getTopInventorySize();
        
        Integer slot;
        if (ServerUtils.isVersionGreater_1_16_5()) {
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
    
    ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(ProtocolLibrary.getPlugin(), PacketType.Play.Client.WINDOW_CLICK) {
      public void onPacketReceiving(PacketEvent event) {
        Integer windowId = event.getPacket().getIntegers().read(0);
        if (windowId < 1) return;
        
        Player player = event.getPlayer();
        
        View openedView = RefreshMenu.getApi().getOpenedView(player);
        if (openedView == null || !(openedView.getDrawer() instanceof ExtendedViewDrawer extendedViewDrawer)) return;
        
        int topInventorySize = extendedViewDrawer.getTopInventorySize();
        
        Integer clickedSlot;
        if (ServerUtils.isVersionGreater("1.18.1")) {
          clickedSlot = event.getPacket().getIntegers().read(2);
        } else {
          clickedSlot = event.getPacket().getIntegers().read(1);
        }
        
        ItemStack[] playerInventoryBuffer = extendedViewDrawer.getPlayerInventoryBuffer();
        
        if (clickedSlot > topInventorySize - 1) {
          if (ServerUtils.isVersionGreater("1.16.5")) {
            Map<Integer, Object> handle = (Map<Integer, Object>) (event.getPacket().getStructures().read(2).getHandle());
            
            for (Integer slot : handle.keySet()) {
              if (slot <= topInventorySize - 1) continue;
              
              Packet.setSlot(player, slot, playerInventoryBuffer[slot - topInventorySize], windowId);
            }
          }
          
          Packet.setSlot(player, -1, AIR, -1);
          Packet.setSlot(player, clickedSlot, playerInventoryBuffer[clickedSlot - topInventorySize], windowId);
          
          Integer button;
          if (ServerUtils.isVersionGreater("1.17")) {
            button = event.getPacket().getIntegers().read(3);
          } else {
            button = event.getPacket().getIntegers().read(2);
          }
          
          ClickType click = button == 0 ? ClickType.LEFT : ClickType.RIGHT;
          
          InventoryView openInventory = player.getOpenInventory();
          if (openInventory.getTopInventory().getHolder() instanceof ExtendedView<?> extendedView) {
            extendedView.onOwnInventoryClick(new InventoryClickEvent(openInventory, InventoryType.SlotType.CONTAINER, clickedSlot, click, InventoryAction.PICKUP_ALL));
          }
          
          event.setCancelled(true);
        }
      }
    });
  }
  
  private static void setSlot(Integer windowId, Integer slot, ItemStack itemStack, Player player) {
    if (itemStack == null) itemStack = AIR;
    
    PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SET_SLOT);
    packet.getIntegers().write(0, windowId);
    packet.getIntegers().write(1, 0);
    packet.getIntegers().write(2, slot);
    
    packet.getItemModifier().write(0, itemStack);
    ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
  }
}
