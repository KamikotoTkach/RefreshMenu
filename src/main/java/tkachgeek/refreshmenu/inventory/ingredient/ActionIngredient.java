package tkachgeek.refreshmenu.inventory.ingredient;

import org.bukkit.event.inventory.ClickType;
import tkachgeek.refreshmenu.MenuContext;
import tkachgeek.refreshmenu.inventory.ingredient.action.Action;

import java.util.EnumMap;

public class ActionIngredient extends IngredientImpl {
  EnumMap<ClickType, Action> actions = new EnumMap<>(ClickType.class);
  
  @Override
  public void onClick(MenuContext context, ClickType clickType) {
    Action action = actions.get(clickType);
    
    if (action != null) {
      action.accept(context, clickType);
    }
  }
}
