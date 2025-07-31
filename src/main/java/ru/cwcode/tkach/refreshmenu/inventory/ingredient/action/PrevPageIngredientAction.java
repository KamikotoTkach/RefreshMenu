package ru.cwcode.tkach.refreshmenu.inventory.ingredient.action;

import org.bukkit.event.inventory.ClickType;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.view.PagedView;

public class PrevPageIngredientAction implements Action {
  @Override
  public void accept(MenuContext context, ClickType clickType) {
    if (context.view() instanceof PagedView<?> view) view.prevPage();
  }
}
