package ru.cwcode.tkach.refreshmenu.inventory.ingredient.extra;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.ExtraIngredient;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.action.Action;

import java.util.EnumMap;

public class ExtraBind implements Extra {
  EnumMap<ClickType, Action> actions = new EnumMap<>(ClickType.class);
  
  public static ExtraBindBuilder builder() {
    return new ExtraBindBuilder();
  }
  
  public ExtraBind bind(ClickType clickType, Action action) {
    actions.put(clickType, action);
    return this;
  }
  
  @Override
  public boolean isHandlingOnClick(ExtraIngredient extraIngredient, MenuContext context, InventoryClickEvent event) {
    return actions.containsKey(event.getClick());
  }
  
  @Override
  public void onClick(ExtraIngredient extraIngredient, MenuContext context, InventoryClickEvent event) {
    Action action = actions.get(event.getClick());
    
    if (action != null) {
      action.accept(context, event.getClick());
    }
  }
}
