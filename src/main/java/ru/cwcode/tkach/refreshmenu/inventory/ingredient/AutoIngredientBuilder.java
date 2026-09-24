package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import org.bukkit.Material;

import java.util.List;

public class AutoIngredientBuilder {
  private final AutoIngredient ingredient = new AutoIngredient();
  
  public AutoIngredientBuilder name(String name) {
    ingredient.name = name;
    return this;
  }
  
  public AutoIngredientBuilder description(String... description) {
    ingredient.description = List.of(description);
    return this;
  }
  
  public AutoIngredientBuilder description(List<String> description) {
    ingredient.description = description;
    return this;
  }
  
  public AutoIngredientBuilder amount(int amount) {
    ingredient.amount = amount;
    return this;
  }
  
  public AutoIngredientBuilder type(Material type) {
    return type(type.name());
  }
  
  public AutoIngredientBuilder type(String type) {
    ingredient.type = type;
    return this;
  }
  
  public AutoIngredientBuilder customModelData(int customModelData) {
    ingredient.customModelData = customModelData;
    return this;
  }
  
  public AutoIngredientBuilder glow(boolean glow) {
    ingredient.glow = glow;
    return this;
  }
  
  public AutoIngredientBuilder show(String condition) {
    ingredient.show = condition;
    return this;
  }
  
  public AutoIngredientBuilder hide(String condition) {
    ingredient.hide = condition;
    return this;
  }
  
  public AutoIngredientBuilder click(String... lines) {
    ingredient.click = List.of(lines);
    return this;
  }
  
  public AutoIngredientBuilder state(String condition, AutoIngredient patch) {
    ingredient.state.put(condition, patch);
    return this;
  }
  
  public AutoIngredient build() {
    return ingredient;
  }
}
