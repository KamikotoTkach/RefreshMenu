package tkachgeek.refreshmenu.inventory.view;

import org.bukkit.inventory.Inventory;
import tkachgeek.refreshmenu.inventory.shape.InventoryShape;

public class ViewDrawer {
  public static void drawPage(PagedView view) {
    int pageSize = view.getShape().howMany(view.getDynamicChar());
    int pointer = view.getPage() * pageSize;
    
    for (int i = 0; i < view.getShape().getJoinedShape().length(); i++) {
      char c = view.getShape().getJoinedShape().charAt(i);
      
      if (view.getShape().getIngredientMap().containsKey(c)) {
        view.getInventory().setItem(i, view.getShape().getIngredientMap().get(c).getItem());
      } else if(view.getDynamicChar() == c){
        if(pointer < view.getDynamic().size()){
          view.getInventory().setItem(i, view.getDynamic().get(pointer).getItem());
          pointer++;
        }
      } else {
        view.getInventory().setItem(i, null);
      }
    }
  }
  
  public static Inventory shaped(InventoryShape shape) {
    Inventory inventory = shape.createInventory();
    
    for (int i = 0; i < shape.getJoinedShape().length(); i++) {
      char c = shape.getJoinedShape().charAt(i);
      if (shape.getIngredientMap().containsKey(c)) {
        inventory.setItem(i, shape.getIngredientMap().get(c).getItem());
      } else {
        inventory.setItem(i, null);
      }
    }
    return inventory;
  }
  public static void updateIngredient(InventoryShape shape, char character, Inventory inventory) {
    for (int i = 0; i < shape.getJoinedShape().length(); i++) {
      char c = shape.getJoinedShape().charAt(i);
      if (shape.getIngredientMap().containsKey(c)) {
        if (c == character) {
          inventory.setItem(i, shape.getIngredientMap().get(c).getItem());
        }
      } else {
        inventory.setItem(i, null);
      }
    }
  }
}
