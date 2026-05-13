package ru.cwcode.tkach.refreshmenu.inventory.view.drawer;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cwcode.cwutils.numbers.NumbersUtils;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.shape.InventoryShape;

import java.util.*;

public class ViewDrawer extends AbstractDrawer {
  public static ItemStack AIR = new ItemStack(Material.AIR);
  public volatile ItemStack[] buffer = null;
  
  @Override
  public void draw(MenuContext context) {
    drawChars(context, Collections.emptySet());
  }
  
  @Override
  public void drawChars(MenuContext context, Collection<Character> characters) {
    if (buffer != null) return;
    
    try {
      buffer = context.view().getInventory().getContents().clone();
      
      InventoryShape shape = context.view().getShape();
      String joinedShape = shape.getJoinedShape();
      
      for (int i = 0; i < getDrawingSize(context); i++) {
        char currShapeChar = joinedShape.charAt(i);
        if (!characters.isEmpty() && !characters.contains(currShapeChar)) continue;
        
        ItemStack item = findItem(context, i, currShapeChar);
        setItem(context, i, item);
      }
      
      drawBuffer(context);
    } finally {
      buffer = null;
    }
  }
  
  @Override
  public void updateRequired(MenuContext context) {
    if (buffer != null) return;
    
    drawChars(context, getRequiredUpdateCharacters(context));
  }
  
  protected static @NotNull List<Character> getRequiredUpdateCharacters(MenuContext context) {
    InventoryShape shape = context.view().getShape();
    
    return shape.getIngredientMap()
                .entrySet()
                .stream()
                .filter(x -> x.getValue().shouldRefresh(context))
                .map(Map.Entry::getKey)
                .toList();
  }
  
  protected void drawBuffer(MenuContext context) {
    var tmp = buffer;
    if (tmp == null) return;
    
    context.view().getInventory().setContents(tmp);
    buffer = null;
  }
  
  protected int getDrawingSize(MenuContext context) {
    return NumbersUtils.notGreater(context.view().getShape().getJoinedShape().length(),
                                   context.view().getInventory().getSize());
  }
  
  protected void setItem(MenuContext context, int slot, @Nullable ItemStack item) {
    buffer[slot] = item == null ? AIR : item;
  }
  
  public void drawItem(MenuContext context, int slot, @Nullable ItemStack item) {
    context.view().getInventory().setItem(slot, item);
  }
  
  protected ItemStack findItem(MenuContext context, int slot, char currShapeChar) {
    HashMap<Character, Ingredient> ingredientMap = context.view().getShape().getIngredientMap();
    
    if (ingredientMap.containsKey(currShapeChar)) {
      return getCachedItem(context, ingredientMap.get(currShapeChar));
    }
    
    return null;
  }
}
