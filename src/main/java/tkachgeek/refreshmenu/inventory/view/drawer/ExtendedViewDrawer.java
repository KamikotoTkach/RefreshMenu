package tkachgeek.refreshmenu.inventory.view.drawer;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import tkachgeek.refreshmenu.MenuContext;
import tkachgeek.tkachutils.numbers.NumbersUtils;
import tkachgeek.tkachutils.protocol.Packet;

public class ExtendedViewDrawer extends PagedViewDrawer {
  int inventorySize;
  
  @Override
  public void draw(MenuContext context) {
    inventorySize = context.view().getInventory().getSize();
    
    super.draw(context);
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
      slot = slot - inventorySize;
      slot = slot >= 27 ? slot - 27 : slot + 9;
      Packet.setSlot(context.player(), slot, item);
    }
  }
}
