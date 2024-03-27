package ru.cwcode.tkach.refreshmenu.inventory.ingredient.action;

import org.bukkit.event.inventory.ClickType;
import ru.cwcode.tkach.refreshmenu.MenuContext;

public class PlayerCommandAction implements Action {
  String command = "say настрой сперва";
  
  public PlayerCommandAction() {
  }
  
  public PlayerCommandAction(String command) {
    this.command = command;
  }
  
  @Override
  public void accept(MenuContext context, ClickType clickType) {
    context.player().performCommand(command);
  }
}
