package ru.cwcode.tkach.refreshmenu.inventory.ingredient.extra;

import org.bukkit.inventory.ItemStack;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.ExtraIngredient;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.view.drawer.ViewDrawer;

import java.util.HashMap;

public class ExtraState implements Extra {
  HashMap<String, Ingredient> states = new HashMap<>();
  transient String lastState = null;
  
  @Override
  public ItemStack getItem(ExtraIngredient extraIngredient, MenuContext context) {
    return states.entrySet().stream()
                 .filter(entry -> shouldChangeState(entry.getKey(), context))
                 .map(x -> {
                   lastState = x.getKey();
                   return x.getValue().getItem(context);
                 })
                 .findFirst()
                 .orElseGet(() -> {
                   lastState = null;
                   return ViewDrawer.AIR;
                 });
  }
  
  @Override
  public boolean isHandlingShouldRefresh(ExtraIngredient extraIngredient, MenuContext context) {
    return shouldRefresh(extraIngredient, context);
  }
  
  @Override
  public boolean isHandlingGetItem(ExtraIngredient extraIngredient, MenuContext context) {
    return shouldRefresh(extraIngredient, context);
  }
  
  @Override
  public boolean shouldRefresh(ExtraIngredient extraIngredient, MenuContext context) {
    return states.keySet().stream().anyMatch((state) -> {
      return shouldChangeState(state, context);
    });
  }
  
  private boolean shouldChangeState(String state, MenuContext context) {
    String[] splittedState = state.split(":");
    
    if (splittedState.length != 2) {
      return false;
    }
    
    String stateValue = context.view().getState(splittedState[0]);
    return stateValue != null && stateValue.equals(splittedState[1]);
  }
  
  public ExtraState addState(String state, Ingredient display) {
    states.put(state, display);
    return this;
  }
}
