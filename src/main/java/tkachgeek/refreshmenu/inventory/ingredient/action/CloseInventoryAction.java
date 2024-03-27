package tkachgeek.refreshmenu.inventory.ingredient.action;

import org.bukkit.event.inventory.ClickType;
import tkachgeek.refreshmenu.MenuContext;

public class CloseInventoryAction implements Action{
  @Override
  public void accept(MenuContext context, ClickType clickType) {
    context.player().closeInventory();
  }
}
