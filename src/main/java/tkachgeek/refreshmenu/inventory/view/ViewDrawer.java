package tkachgeek.refreshmenu.inventory.view;

import org.bukkit.inventory.Inventory;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;
import tkachgeek.tkachutils.numbers.NumbersUtils;

import java.util.HashMap;

public class ViewDrawer {
  public static void drawPage(PagedView<? extends Ingredient> view) {
    int pageSize = view.getShape().howMany(view.getDynamicChar());
    int dynamicItemIndex = view.getPage() * pageSize;
    String joinedShape = view.getShape().getJoinedShape();
    HashMap<Character, Ingredient> ingredientMap = view.getShape().getIngredientMap();
    
    Inventory inventory = view.getInventory();
    inventory.clear();
    
    char currShapeChar;
    for (int i = 0; i < NumbersUtils.notGreater(joinedShape.length(), inventory.getSize()); i++) {
      currShapeChar = joinedShape.charAt(i);
      
      if (ingredientMap.containsKey(currShapeChar)) {
        inventory.setItem(i, ingredientMap.get(currShapeChar).getItem(view.placeholders));
      } else if (view.getDynamicChar() == currShapeChar && dynamicItemIndex < view.getDynamic().size()) {
        inventory.setItem(i, view.getDynamic().get(dynamicItemIndex++).getItem(view.placeholders));
      }
    }
  }
  
  public static Inventory createFilledInventory(View view) {
    Inventory inventory = view.getShape().createInventory(view);
    fillInventory(view, inventory);
    return inventory;
  }
  
  public static void fillInventory(View view, Inventory inventory) {
    HashMap<Character, Ingredient> ingredientMap = view.getShape().getIngredientMap();
    String joinedShape = view.getShape().getJoinedShape();
    
    char currShapeChar;
    for (int i = 0; i < NumbersUtils.notGreater(joinedShape.length(), inventory.getSize()); i++) {
      currShapeChar = joinedShape.charAt(i);
      
      if (ingredientMap.containsKey(currShapeChar)) {
        inventory.setItem(i, ingredientMap.get(currShapeChar).getItem(view.placeholders));
      }
    }
  }
  
  public static void redrawIngredient(View view, char character) {
    String joinedShape = view.getShape().getJoinedShape();
    HashMap<Character, Ingredient> ingredientMap = view.getShape().getIngredientMap();
    Inventory inventory = view.getInventory();
    
    char currShapeChar;
    for (int i = 0; i < NumbersUtils.notGreater(joinedShape.length(), inventory.getSize()); i++) {
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
