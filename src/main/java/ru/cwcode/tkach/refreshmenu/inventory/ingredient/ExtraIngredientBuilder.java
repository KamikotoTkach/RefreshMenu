package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import ru.cwcode.tkach.refreshmenu.inventory.ingredient.extra.Extra;

import java.util.ArrayList;
import java.util.List;

public class ExtraIngredientBuilder extends IngredientBuilder<ExtraIngredient, ExtraIngredientBuilder> {
  private final List<Extra> extras = new ArrayList<>();
  
  public ExtraIngredientBuilder extra(Extra extra) {
    this.extras.add(extra);
    return this;
  }
  
  @Override
  public ExtraIngredient build() {
    return new ExtraIngredient(name, description, amount, type, customModelData, extras);
  }
}
