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
          itemStacks.set(slot, playerInventoryBuffer[c++]);
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
        
        Integer slot = event.getPacket().getIntegers().read(0);
        if (slot > topInventorySize - 1) {
          int playerInvSlot = slot - topInventorySize;
          event.getPacket().getItemModifier().write(0, extendedViewDrawer.getPlayerInventoryBuffer()[playerInvSlot >= 27 ? playerInvSlot - 27 : playerInvSlot + 9]);
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
        
        Integer clickedSlot = event.getPacket().getIntegers().read(2);
        
        if (clickedSlot > topInventorySize - 1) {
          
          Map<Integer, Object> handle = (Map<Integer, Object>) (event.getPacket().getStructures().read(2).getHandle());
          ItemStack[] playerInventoryBuffer = extendedViewDrawer.getPlayerInventoryBuffer();
          
          for (Integer slot : handle.keySet()) {
            if (slot <= topInventorySize - 1) continue;
            
            setSlot(windowId, slot, playerInventoryBuffer[slot - topInventorySize], player);
          }
          
          setSlot(-1, -1, null, player);
          
          ClickType click = event.getPacket().getIntegers().read(3) == 0 ? ClickType.LEFT : ClickType.RIGHT;
          
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
