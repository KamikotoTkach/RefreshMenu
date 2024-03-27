package tkachgeek.refreshmenu.inventory.ingredient;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import tkachgeek.refreshmenu.MenuContext;
import tkachgeek.refreshmenu.inventory.ingredient.action.Action;

import java.util.EnumMap;
import java.util.List;

public class ActionIngredient extends IngredientImpl {
  EnumMap<ClickType, Action> actions = new EnumMap<>(ClickType.class);
  
  public ActionIngredient() {
  }
  
  public ActionIngredient(String name, List<String> description, int amount, Material type, int customModelData, EnumMap<ClickType, Action> actions) {
    super(name, description, amount, type, customModelData);
    this.actions = actions;
  }
  
  public ActionIngredient bind(ClickType clickType, Action action) {
    actions.put(clickType, action);
    return this;
  }
  
  
  @Override
  public void onClick(MenuContext context, ClickType clickType) {
    Action action = actions.get(clickType);
    
    if (action != null) {
      action.accept(context, clickType);
    }
  }
}
