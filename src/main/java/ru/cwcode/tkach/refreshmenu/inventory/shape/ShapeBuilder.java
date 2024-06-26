package ru.cwcode.tkach.refreshmenu.inventory.shape;

import org.bukkit.event.inventory.InventoryType;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.type.ChestType;
import ru.cwcode.tkach.refreshmenu.inventory.type.InventoryTypeHolder;
import ru.cwcode.tkach.refreshmenu.inventory.type.InventoryTypeHolderImpl;
import ru.cwcode.tkach.refreshmenu.inventory.view.View;

import java.util.HashMap;

public class ShapeBuilder {
  HashMap<Character, Ingredient> ingredients = new HashMap<>();
  private String name = "<rainbow>Студия ClockWork vk.com/cwcode";
  private String[] shape;
  private InventoryTypeHolder type;
  
  public ShapeBuilder type(InventoryType type) {
    this.type = new InventoryTypeHolderImpl(type);
    return this;
  }
  
  public ShapeBuilder type(InventoryTypeHolder type) {
    this.type = type;
    
    return this;
  }
  
  public ShapeBuilder chest(int size) {
    this.type = new ChestType(size);
    return this;
  }
  
  public ShapeBuilder name(String name) {
    this.name = name;
    return this;
  }
  
  public ShapeBuilder shape(String... shape) {
    this.shape = shape;
    return this;
  }
  
  public InventoryShape build() {
    return new InventoryShape(name, shape, type, ingredients);
  }
  
  public void build(View view) {
    view.setShape(new InventoryShape(name, shape, type, ingredients));
  }
  
  public ShapeBuilder ingredient(char character, Ingredient ingredient) {
    ingredients.put(character, ingredient);
    return this;
  }
}