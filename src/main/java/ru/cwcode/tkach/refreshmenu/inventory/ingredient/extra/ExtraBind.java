package ru.cwcode.tkach.refreshmenu.inventory.ingredient.extra;

import org.bukkit.event.inventory.ClickType;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.ExtraIngredient;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.action.Action;

import java.util.EnumMap;

public class ExtraBind implements Extra {
  EnumMap<ClickType, Action> actions = new EnumMap<>(ClickType.class);
  
  public ExtraBind bind(ClickType clickType, Action action) {
    actions.put(clickType, action);
    return this;
  }
  
  @Override
  public boolean isHandlingOnClick(ExtraIngredient extraIngredient, MenuContext context, ClickType clickType) {
    return actions.containsKey(clickType);
  }
  
  @Override
  public void onClick(ExtraIngredient extraIngredient, MenuContext context, ClickType clickType) {
    Action action = actions.get(clickType);
    
    if (action != null) {
      action.accept(context, clickType);
    }
  }
}
