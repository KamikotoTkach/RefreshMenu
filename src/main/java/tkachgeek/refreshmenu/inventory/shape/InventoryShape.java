package tkachgeek.refreshmenu.inventory.shape;

import org.bukkit.inventory.Inventory;
import tkachgeek.config.minilocale.Message;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;
import tkachgeek.refreshmenu.inventory.type.InventoryTypeHolder;

import java.util.HashMap;

public class InventoryShape {
  public InventoryTypeHolder type;
  String name;
  String[] shape;
  HashMap<Character, Ingredient> ingredientMap = new HashMap<>();
  transient String joinedShape = null;
  
  public InventoryShape(String name, String[] shape, InventoryTypeHolder type, HashMap<Character, Ingredient> ingredients) {
    this.name = name;
    this.shape = shape;
    this.type = type;
    this.ingredientMap = ingredients;
  }
  
  public InventoryShape() {
  }
  
  public static ShapeBuilder builder() {
    return new ShapeBuilder();
  }
  
  public InventoryTypeHolder getType() {
    return type;
  }
  
  public Inventory createInventory() {
    return type.createInventory(new Message(getName()).get());
  }
  
  
  public char charAtIndex(int index) {
    return getJoinedShape().charAt(index);
  }
  
  
  
  public int howMany(char dynamicChar) {
    int i = 0;
    for (String line : getShape()) {
      for (char c : line.toCharArray()) {
        if (c == dynamicChar) i++;
      }
    }
    return i;
  }
  
  public String getName() {
    return name;
  }
  
  public String[] getShape() {
    return shape;
  }
  
  public HashMap<Character, Ingredient> getIngredientMap() {
    return ingredientMap;
  }
  
  public String getJoinedShape() {
    if(joinedShape == null) joinedShape = String.join("", getShape());
    return joinedShape;
  }
}
