package tkachgeek.refreshmenu.inventory.view.drawer;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import tkachgeek.refreshmenu.MenuContext;
import tkachgeek.tkachutils.numbers.NumbersUtils;

import java.util.Set;

public class ExtendedViewDrawer extends PagedViewDrawer {
  int inventorySize;
  ItemStack[] playerInventoryBuffer = new ItemStack[36];
  @Override
  public void draw(MenuContext context) {
    inventorySize = context.view().getInventory().getSize();
    
    super.draw(context);
  }
  
  @Override
  public void drawChars(MenuContext context, Set<Character> characters) {
    inventorySize = context.view().getInventory().getSize();
    
    super.drawChars(context, characters);
  }
  
  public void sendSlotPacket(Player player, int slot) {
    PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SET_SLOT);
    
    packet.getIntegers().write(0, -2);
    packet.getIntegers().write(1, 0);
    packet.getIntegers().write(2, slot >= 27 ? slot - 27 : slot + 9);

    packet.getItemModifier().write(0, playerInventoryBuffer[slot]);
    ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
  }
  
  @Override
  protected int getDrawingSize(MenuContext context) {
    return NumbersUtils.notGreater(context.view().getShape().getJoinedShape().length(),
                                   inventorySize + 36);
  }
  
  @Override
  protected void setItem(MenuContext context, int slot, @Nullable ItemStack item) {
    if (slot < inventorySize) {
      super.setItem(context, slot, item);
    } else {
      playerInventoryBuffer[slot-inventorySize] = item;
    }
  }
  
  @Override
  protected void drawBuffer(MenuContext context) {
    super.drawBuffer(context);
    
    for (int i = 0; i < playerInventoryBuffer.length; i++) {
      sendSlotPacket(context.player(), i);
    }
  }
  
  public ItemStack[] getPlayerInventoryBuffer() {
    return playerInventoryBuffer;
  }
  
  public int getTopInventorySize() {
    return inventorySize;
  }
  
}
