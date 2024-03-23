package tkachgeek.refreshmenu.inventory.ingredient.action;

import org.bukkit.event.inventory.ClickType;
import tkachgeek.refreshmenu.MenuContext;

public class PlayerCommandAction implements Action{
  String command;
  @Override
  public void accept(MenuContext context, ClickType clickType) {
    context.player().performCommand(command);
  }
}
