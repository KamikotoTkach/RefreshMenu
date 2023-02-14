package tkachgeek.refreshmenu.inventory.view;

import org.bukkit.inventory.Inventory;

public class ViewDrawer {
  public static void drawPage(PagedView view) {
    int pageSize = view.getShape().howMany(view.getDynamicChar());
    int pointer = view.getPage() * pageSize;
    
    for (int i = 0; i < view.getShape().getJoinedShape().length(); i++) {
      char c = view.getShape().getJoinedShape().charAt(i);
      
      if (view.getShape().getIngredientMap().containsKey(c)) {
        view.getInventory().setItem(i, view.getShape().getIngredientMap().get(c).getItem(view.placeholders));
      } else if (view.getDynamicChar() == c) {
        if (pointer < view.getDynamic().size()) {
          view.getInventory().setItem(i, view.getDynamic().get(pointer).getItem(view.placeholders));
          pointer++;
        }
      } else {
        view.getInventory().setItem(i, null);
      }
    }
  }
  
  public static Inventory createFilledInventory(View view) {
    Inventory inventory = view.getShape().createInventory();
    
    for (int i = 0; i < view.getShape().getJoinedShape().length(); i++) {
      char c = view.getShape().getJoinedShape().charAt(i);
      if (view.getShape().getIngredientMap().containsKey(c)) {
        inventory.setItem(i, view.getShape().getIngredientMap().get(c).getItem(view.placeholders));
      } else {
        inventory.setItem(i, null);
      }
    }
    return inventory;
  }
  
  public static void redrawIngredient(View view, char character) {
    for (int i = 0; i < view.getShape().getJoinedShape().length(); i++) {
      char c = view.getShape().getJoinedShape().charAt(i);
      if (view.getShape().getIngredientMap().containsKey(c)) {
        if (c == character) {
          view.getInventory().setItem(i, view.getShape().getIngredientMap().get(c).getItem(view.placeholders));
        }
      } else {
        view.getInventory().setItem(i, null);
      }
    }
  }
}
