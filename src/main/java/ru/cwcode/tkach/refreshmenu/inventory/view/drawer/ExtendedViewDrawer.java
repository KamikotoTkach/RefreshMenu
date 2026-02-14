package ru.cwcode.tkach.refreshmenu.inventory.view.drawer;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import ru.cwcode.cwutils.numbers.NumbersUtils;
import ru.cwcode.cwutils.protocol.Packet;
import ru.cwcode.tkach.refreshmenu.MenuContext;

import java.util.Collection;

public class ExtendedViewDrawer extends PagedViewDrawer {
  volatile int inventorySize;
  @Getter
  volatile ItemStack[] playerInventoryBuffer = new ItemStack[36];
  
  @Override
  public void draw(MenuContext context) {
    if (buffer != null) return;
    
    inventorySize = context.view().getInventory().getSize();
    
    super.draw(context);
  }
  
  @Override
  public void drawChars(MenuContext context, Collection<Character> characters) {
    if (buffer != null) return;
    
    inventorySize = context.view().getInventory().getSize();
    
    super.drawChars(context, characters);
  }
  
  public void sendSlotPacket(Player player, int slot) {
    ItemStack item = playerInventoryBuffer[slot];
    Packet.setSlot(player, slot > 27 ? slot - 27 : slot + 9, item == null ? AIR : item, -2);
  }
  
  @Override
  protected int getDrawingSize(MenuContext context) {
    return NumbersUtils.notGreater(context.view().getShape().getJoinedShape().length(),
                                   inventorySize + 36);
  }
  
  @Override
  public void setItem(MenuContext context, int slot, @Nullable ItemStack item) {
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
  
  public int getTopInventorySize() {
    return inventorySize;
  }
  
}
