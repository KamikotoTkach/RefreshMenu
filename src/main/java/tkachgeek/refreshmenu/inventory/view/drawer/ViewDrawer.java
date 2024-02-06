package tkachgeek.refreshmenu.inventory.view.drawer;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import tkachgeek.refreshmenu.MenuContext;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;
import tkachgeek.refreshmenu.inventory.shape.InventoryShape;
import tkachgeek.tkachutils.numbers.NumbersUtils;

import java.util.HashMap;
import java.util.Set;

public class ViewDrawer extends AbstractDrawer {
  public static ItemStack AIR = new ItemStack(Material.AIR);
  @Override
  public void draw(MenuContext context) {
    InventoryShape shape = context.view().getShape();
    String joinedShape = shape.getJoinedShape();
    
    for (int i = 0; i < getDrawingSize(context); i++) {
      char currShapeChar = joinedShape.charAt(i);
      
      ItemStack item = findItem(context, i, currShapeChar);
      
      if (item != null) {
        setItem(context, i, item);
      }
    }
  }
  
  @Override
  public void drawChars(MenuContext context, Set<Character> characters) {
    InventoryShape shape = context.view().getShape();
    String joinedShape = shape.getJoinedShape();
    
    for (int i = 0; i < getDrawingSize(context); i++) {
      char currShapeChar = joinedShape.charAt(i);
      
      if (!characters.contains(currShapeChar)) continue;
      
      ItemStack item = findItem(context, i, currShapeChar);
      
      if (item != null) {
        setItem(context, i, item);
      }
    }
  }
  
  protected int getDrawingSize(MenuContext context) {
    return NumbersUtils.notGreater(context.view().getShape().getJoinedShape().length(),
                                   context.view().getInventory().getSize());
  }
  
  protected void setItem(MenuContext context, int slot, @Nullable ItemStack item) {
    if (item != null) context.view().getInventory().setItem(slot, item);
  }
  
  protected ItemStack findItem(MenuContext context, int slot, char currShapeChar) {
    HashMap<Character, Ingredient> ingredientMap = context.view().getShape().getIngredientMap();
    
    if (ingredientMap.containsKey(currShapeChar)) {
      return ingredientMap.get(currShapeChar).getItem(context);
    }
    
    return null;
  }
}