package ru.cwcode.tkach.refreshmenu.inventory.view.drawer;

import org.bukkit.inventory.ItemStack;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.shape.InventoryShape;
import ru.cwcode.tkach.refreshmenu.inventory.view.DynamicView;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicViewDrawer extends ViewDrawer {
  Map<Character, Integer> dynamicItemIndex;
  
  volatile DynamicView view;
  
  @Override
  public void draw(MenuContext context) {
    if (buffer != null) return;
    
    setupDynamicDrawer(context);
    
    super.draw(context);
  }
  
  @Override
  public void drawChars(MenuContext context, Collection<Character> characters) {
    if (buffer != null) return;
    
    setupDynamicDrawer(context);
    
    super.drawChars(context, characters);
  }
  
  public void updateRequired(MenuContext context) { //todo refactor
    if (buffer != null) return;
    
    try {
      
      super.updateRequired(context);
      
      setupDynamicDrawer(context);
      
      buffer = context.view().getInventory().getContents().clone();
      
      InventoryShape shape = context.view().getShape();
      String joinedShape = shape.getJoinedShape();
      
      for (int i = 0; i < getDrawingSize(context); i++) {
        char character = joinedShape.charAt(i);
        
        List<? extends Ingredient> dynamic = view.getDynamicIngredients(character);
        if (dynamic == null) continue;
        
        Integer index = dynamicItemIndex.getOrDefault(character, 0);
        if (index >= dynamic.size()) continue;
        
        dynamicItemIndex.put(character, index + 1);
        
        Ingredient ingredient = dynamic.get(index);
        
        if (ingredient.shouldRefresh(context)) {
          ItemStack item = ingredient.getItem(context);
          
          setItem(context, i, item);
        }
      }
      
      drawBuffer(context);
      
    } finally {
      buffer = null;
    }
  }
  
  @Override
  protected ItemStack findItem(MenuContext context, int slot, char shapeChar) {
    ItemStack simpleIngredient = super.findItem(context, slot, shapeChar);
    
    if (simpleIngredient != null && !simpleIngredient.getType().isAir()) return simpleIngredient;
    
    if (view.getDynamicCharacters().contains(shapeChar)) {
      Integer index = dynamicItemIndex.getOrDefault(shapeChar, 0);
      List<? extends Ingredient> dynamicIngredients = view.getDynamicIngredients(shapeChar);
      
      if (index < dynamicIngredients.size()) {
        dynamicItemIndex.put(shapeChar, index + 1);
        return dynamicIngredients.get(index).getItem(context);
      }
    }
    
    return AIR;
  }
  
  private void setupDynamicDrawer(MenuContext context) {
    if (!(context.view() instanceof DynamicView dynamicView))
      throw new IllegalArgumentException("DynamicViewDrawer can only be used with <? extends DynamicView>");
    
    view = dynamicView;
    dynamicItemIndex = new HashMap<>();
  }
}
