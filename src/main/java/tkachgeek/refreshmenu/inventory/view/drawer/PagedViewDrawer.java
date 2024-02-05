package tkachgeek.refreshmenu.inventory.view.drawer;

import org.bukkit.inventory.ItemStack;
import tkachgeek.refreshmenu.MenuContext;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;
import tkachgeek.refreshmenu.inventory.view.PagedView;

import java.util.Set;

public class PagedViewDrawer extends ViewDrawer {
  int dynamicItemIndex;
  PagedView<? extends Ingredient> view;
  
  @Override
  public void draw(MenuContext context) {
    if (!(context.view() instanceof PagedView<? extends Ingredient> pagedView))
      throw new IllegalArgumentException("PagedViewDrawer can only be used with <? extends PagedView>");
    
    view = pagedView;
    
    int pageSize = pagedView.getShape().howMany(view.getDynamicChar());
    dynamicItemIndex = view.getPage() * pageSize;
    
    super.draw(context);
  }
  
  @Override
  public void drawChars(MenuContext context, Set<Character> characters) {
    if (!(context.view() instanceof PagedView<? extends Ingredient> pagedView))
      throw new IllegalArgumentException("PagedViewDrawer can only be used with <? extends PagedView>");
    
    view = pagedView;
    
    int pageSize = pagedView.getShape().howMany(view.getDynamicChar());
    dynamicItemIndex = view.getPage() * pageSize;
    
    super.drawChars(context, characters);
  }
  
  @Override
  protected ItemStack findItem(MenuContext context, int slot, char shapeChar) {
    ItemStack simpleIngredient = super.findItem(context, slot, shapeChar);
    
    if (simpleIngredient != null) return simpleIngredient;
    
    if (view.getDynamicChar() == shapeChar) {
      
      if (dynamicItemIndex < view.getDynamic().size()) {
        return view.getDynamic().get(dynamicItemIndex++).getItem(context);
      }
    }
    
    return AIR;
  }
}

