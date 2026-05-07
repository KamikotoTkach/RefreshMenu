package ru.cwcode.tkach.refreshmenu.inventory.view.drawer;

import org.bukkit.inventory.ItemStack;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.ArtIngredient;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.view.ArtExtendedView;

import java.util.Collection;

public class ArtExtendedViewDrawer extends ExtendedViewDrawer {
  volatile ArtExtendedView<? extends Ingredient, ? extends ArtIngredient> view;
  
  @Override
  public void drawChars(MenuContext context, Collection<Character> characters) {
    if (buffer != null) return;
    
    if (!(context.view() instanceof ArtExtendedView<? extends Ingredient, ? extends ArtIngredient> artExtendedView))
      throw new IllegalArgumentException("ArtExtendedViewDrawer can only be used with <? extends ArtExtendedView>");
    
    view = artExtendedView;
    
    super.drawChars(context, characters);
  }
  
  @Override
  protected ItemStack findItem(MenuContext context, int slot, char shapeChar) {
    ItemStack item = super.findItem(context, slot, shapeChar);
    
    if (item != null && !item.getType().isAir()) return item;
    
    ArtIngredient art = view.getArts().get(shapeChar);
    if (art == null) return null;
    
    int draws = (int) context.view().getShape().getJoinedShape().substring(0, slot).chars().filter(ch -> ch == shapeChar).count();
    if (art.getMaxDraws() > -1 && draws >= art.getMaxDraws()) return art.getEmptyItem(context);
    
    return getCachedItem(context, view.getArts().get(shapeChar));
  }
}
