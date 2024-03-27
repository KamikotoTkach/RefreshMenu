package ru.cwcode.tkach.refreshmenu.inventory.ingredient.action;

import org.bukkit.event.inventory.ClickType;
import ru.cwcode.tkach.refreshmenu.MenuContext;

public class CloseInventoryAction implements Action{
  @Override
  public void accept(MenuContext context, ClickType clickType) {
    context.player().closeInventory();
  }
}
