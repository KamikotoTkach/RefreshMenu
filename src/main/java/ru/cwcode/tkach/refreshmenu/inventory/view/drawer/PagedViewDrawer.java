package ru.cwcode.tkach.refreshmenu.inventory.view.drawer;

import org.bukkit.inventory.ItemStack;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.shape.InventoryShape;
import ru.cwcode.tkach.refreshmenu.inventory.view.PagedView;

import java.util.Collection;

public class PagedViewDrawer extends ViewDrawer {
  volatile int dynamicItemIndex;
  volatile PagedView<? extends Ingredient> view;
  
  @Override
  public void draw(MenuContext context) {
    if (buffer != null) return;
    
    setupPagedDrawer(context);
    
    super.draw(context);
  }
  
  private void setupPagedDrawer(MenuContext context) {
    if (!(context.view() instanceof PagedView<? extends Ingredient> pagedView))
      throw new IllegalArgumentException("PagedViewDrawer can only be used with <? extends PagedView>");
    
    view = pagedView;
    
    int pageSize = pagedView.getShape().howMany(view.getDynamicChar());
    dynamicItemIndex = view.getPage() * pageSize;
  }
  
  @Override
  public void drawChars(MenuContext context, Collection<Character> characters) {
    if (buffer != null) return;
    
    setupPagedDrawer(context);
    
    super.drawChars(context, characters);
  }
  
  public void updateRequired(MenuContext context) { //todo refactor
    if (buffer != null) return;
    
    try {
      super.updateRequired(context);
      
      setupPagedDrawer(context);
      
      buffer = context.view().getInventory().getContents().clone();
      
      InventoryShape shape = context.view().getShape();
      String joinedShape = shape.getJoinedShape();
      
      for (int i = 0; i < getDrawingSize(context) && dynamicItemIndex < view.getDynamic().size(); i++) {
        if (joinedShape.charAt(i) == view.getDynamicChar()) {
          
          Ingredient ingredient = view.getDynamic().get(dynamicItemIndex++);
          
          if (ingredient.shouldRefresh(context)) {
            ItemStack item = ingredient.getItem(context);
            
            setItem(context, i, item);
          }
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
    
    if (view.getDynamicChar() == shapeChar) {
      
      if (dynamicItemIndex < view.getDynamic().size()) {
        return view.getDynamic().get(dynamicItemIndex++).getItem(context);
      }
    }
    
    return AIR;
  }
}

