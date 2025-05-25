package ru.cwcode.tkach.refreshmenu.inventory.view.craft;

import org.bukkit.inventory.ItemStack;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.view.drawer.ViewDrawer;

import java.util.Collection;

public class CraftDrawer extends ViewDrawer {
  volatile CraftView view;
  
  @Override
  public void draw(MenuContext context) {
    if (buffer != null) return;
    view = (CraftView) context.view();
    
    super.draw(context);
  }
  
  @Override
  public void drawChars(MenuContext context, Collection<Character> characters) {
    if (buffer != null) return;
    view = (CraftView) context.view();
    
    super.drawChars(context, characters);
  }
  
  public void updateRequired(MenuContext context) { //todo refactor
    if (buffer != null) return;
    
    try {
      super.updateRequired(context);
      
      view = ((CraftView) context.view());
      
      buffer = context.view().getInventory().getContents().clone();
      
      view.getCurrentPage().getIngredients().forEach((slot, ingredient) -> {
        if (ingredient.shouldRefresh(context)) {
          ItemStack item = getCachedItem(context, ingredient);
          if (item != null) setItem(context, slot, item);
        }
      });
      
      drawBuffer(context);
    } finally {
      buffer = null;
    }
  }
  
  @Override
  protected ItemStack findItem(MenuContext context, int slot, char shapeChar) {
    ItemStack simpleIngredient = super.findItem(context, slot, shapeChar);
    if (simpleIngredient != null && !simpleIngredient.getType().isAir()) return simpleIngredient;
    
    Ingredient ingredient = view.getCurrentPage().getIngredients().get(slot);
    if (ingredient != null) return getCachedItem(context, ingredient);
    
    return AIR;
  }
}
