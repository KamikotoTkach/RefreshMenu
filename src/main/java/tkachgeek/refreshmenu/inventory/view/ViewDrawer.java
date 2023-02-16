package tkachgeek.refreshmenu.inventory.view;

import org.bukkit.inventory.Inventory;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;

import java.util.HashMap;

public class ViewDrawer {
  public static void drawPage(PagedView view) {
    int pageSize = view.getShape().howMany(view.getDynamicChar());
    int dynamicItemIndex = view.getPage() * pageSize;
    String joinedShape = view.getShape().getJoinedShape();
    HashMap<Character, Ingredient> ingredientMap = view.getShape().getIngredientMap();
    
    Inventory inventory = view.getInventory();
    inventory.clear();
    
    char currShapeChar;
    for (int i = 0; i < joinedShape.length(); i++) {
      currShapeChar = joinedShape.charAt(i);
      
      if (ingredientMap.containsKey(currShapeChar)) {
        inventory.setItem(i, ingredientMap.get(currShapeChar).getItem(view.placeholders));
      } else if (view.getDynamicChar() == currShapeChar && dynamicItemIndex < view.getDynamic().size()) {
        inventory.setItem(i, view.getDynamic().get(dynamicItemIndex++).getItem(view.placeholders));
      }
    }
  }
  
  public static Inventory createFilledInventory(View view) {
    HashMap<Character, Ingredient> ingredientMap = view.getShape().getIngredientMap();
    Inventory inventory = view.getShape().createInventory(view);
    String joinedShape = view.getShape().getJoinedShape();
    
    char currShapeChar;
    for (int i = 0; i < joinedShape.length(); i++) {
      currShapeChar = joinedShape.charAt(i);
      
      if (ingredientMap.containsKey(currShapeChar)) {
        inventory.setItem(i, ingredientMap.get(currShapeChar).getItem(view.placeholders));
      }
    }
    return inventory;
  }
  
  public static void redrawIngredient(View view, char character) {
    String joinedShape = view.getShape().getJoinedShape();
    HashMap<Character, Ingredient> ingredientMap = view.getShape().getIngredientMap();
    Inventory inventory = view.getInventory();
    
    char currShapeChar;
    for (int i = 0; i < joinedShape.length(); i++) {
      currShapeChar = joinedShape.charAt(i);
      
      if (ingredientMap.containsKey(currShapeChar)) {
        if (currShapeChar == character) {
          inventory.setItem(i, ingredientMap.get(currShapeChar).getItem(view.placeholders));
        }
      } else {
        inventory.setItem(i, null);
      }
    }
  }
}
