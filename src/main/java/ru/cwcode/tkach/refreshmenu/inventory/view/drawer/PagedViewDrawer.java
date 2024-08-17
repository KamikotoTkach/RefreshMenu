package ru.cwcode.tkach.refreshmenu.inventory.view.drawer;

import org.bukkit.inventory.ItemStack;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.shape.InventoryShape;
import ru.cwcode.tkach.refreshmenu.inventory.view.PagedView;

import java.util.Collection;
import java.util.Set;

public class PagedViewDrawer extends ViewDrawer {
  int dynamicItemIndex;
  PagedView<? extends Ingredient> view;
  
  @Override
  public void draw(MenuContext context) {
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
  public synchronized void drawChars(MenuContext context, Collection<Character> characters) {
    setupPagedDrawer(context);
    
    super.drawChars(context, characters);
  }
  
  public synchronized void updateRequired(MenuContext context) { //todo refactor
    super.updateRequired();
    
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

