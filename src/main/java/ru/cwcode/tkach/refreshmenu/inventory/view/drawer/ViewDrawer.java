package ru.cwcode.tkach.refreshmenu.inventory.view.drawer;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.shape.InventoryShape;
import tkachgeek.tkachutils.numbers.NumbersUtils;
import tkachgeek.tkachutils.player.WindowIdCatcher;

import java.util.HashMap;
import java.util.Set;

public class ViewDrawer extends AbstractDrawer {
  public static ItemStack AIR = new ItemStack(Material.AIR);
  public static ItemStack[] buffer = null;
  @Override
  public void draw(MenuContext context) {
    buffer = context.view().getInventory().getContents().clone();
    
    InventoryShape shape = context.view().getShape();
    String joinedShape = shape.getJoinedShape();
    
    for (int i = 0; i < getDrawingSize(context); i++) {
      char currShapeChar = joinedShape.charAt(i);
      
      ItemStack item = findItem(context, i, currShapeChar);
      
      setItem(context, i, item);
    }
    
    drawBuffer(context);
  }
  
  protected void drawBuffer(MenuContext context) {
    context.view().getInventory().setContents(buffer);
    buffer = null;
  }
  
  @Override
  public void drawChars(MenuContext context, Set<Character> characters) {
    buffer = context.view().getInventory().getContents().clone();
    
    InventoryShape shape = context.view().getShape();
    String joinedShape = shape.getJoinedShape();
    
    for (int i = 0; i < getDrawingSize(context); i++) {
      char currShapeChar = joinedShape.charAt(i);
      
      if (!characters.contains(currShapeChar)) continue;
      
      ItemStack item = findItem(context, i, currShapeChar);
      
      setItem(context, i, item);
    }
    
    drawBuffer(context);
  }
  
  protected int getDrawingSize(MenuContext context) {
    return NumbersUtils.notGreater(context.view().getShape().getJoinedShape().length(),
                                   context.view().getInventory().getSize());
  }
  
  protected void setItem(MenuContext context, int slot, @Nullable ItemStack item) {
    buffer[slot] = item == null ? AIR : item;
  }
  
  protected ItemStack findItem(MenuContext context, int slot, char currShapeChar) {
    HashMap<Character, Ingredient> ingredientMap = context.view().getShape().getIngredientMap();
    
    if (ingredientMap.containsKey(currShapeChar)) {
      return ingredientMap.get(currShapeChar).getItem(context);
    }
    
    return null;
  }
}
