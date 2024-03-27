package tkachgeek.refreshmenu.inventory.ingredient.action;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.ClickType;
import tkachgeek.refreshmenu.MenuContext;

public class ConsoleCommandAction implements Action {
  String command = "say настрой сперва";
  
  public ConsoleCommandAction() {
  }
  
  public ConsoleCommandAction(String command) {
    this.command = command;
  }
  
  @Override
  public void accept(MenuContext context, ClickType clickType) {
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("<player>", context.player().getName()));
  }
}
