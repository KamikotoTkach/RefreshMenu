package tkachgeek.refreshmenu.inventory.ingredient.action;

import org.bukkit.event.inventory.ClickType;
import tkachgeek.refreshmenu.MenuContext;

public class SetViewAction implements Action {
  String view = "default";
  @Override
  public void accept(MenuContext context, ClickType clickType) {
    context.view().getMenu().openView(context.player(), view);
  }
}
