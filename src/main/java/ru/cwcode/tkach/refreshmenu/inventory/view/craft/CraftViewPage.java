package ru.cwcode.tkach.refreshmenu.inventory.view.craft;

import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;

import java.util.HashMap;
import java.util.Map;

public class CraftViewPage {
  Map<Integer, Ingredient> ingredients = new HashMap<>();
  
  public Map<Integer, Ingredient> getIngredients() {
    return ingredients;
  }
}
