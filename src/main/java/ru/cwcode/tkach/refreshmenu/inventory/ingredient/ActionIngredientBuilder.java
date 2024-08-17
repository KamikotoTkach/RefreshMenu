package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import org.bukkit.event.inventory.ClickType;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.action.Action;

import java.util.EnumMap;
/**
 * @deprecated <p> Use {@link ExtraIngredientBuilder} instead
 */
@Deprecated
public class ActionIngredientBuilder extends IngredientBuilder<ActionIngredient, ActionIngredientBuilder> {
  private final EnumMap<ClickType, Action> actions = new EnumMap<>(ClickType.class);
  
  public ActionIngredientBuilder bind(ClickType clickType, Action action) {
    this.actions.put(clickType, action);
    return this;
  }
  
  @Override
  public ActionIngredient build() {
    return new ActionIngredient(name, description, amount, type, customModelData, actions);
  }
}
