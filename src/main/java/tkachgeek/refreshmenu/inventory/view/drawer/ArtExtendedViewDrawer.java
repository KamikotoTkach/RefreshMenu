package tkachgeek.refreshmenu.inventory.view.drawer;

import org.bukkit.inventory.ItemStack;
import tkachgeek.refreshmenu.MenuContext;
import tkachgeek.refreshmenu.inventory.ingredient.ArtIngredient;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;
import tkachgeek.refreshmenu.inventory.view.ArtExtendedView;

import java.util.HashMap;
import java.util.Set;

public class ArtExtendedViewDrawer extends ExtendedViewDrawer {
  ArtExtendedView<? extends Ingredient, ? extends ArtIngredient> view;
  HashMap<Character, Integer> artDraws = new HashMap<>();
  
  @Override
  public void draw(MenuContext context) {
    if (!(context.view() instanceof ArtExtendedView<? extends Ingredient, ? extends ArtIngredient> artExtendedView))
      throw new IllegalArgumentException("ArtExtendedViewDrawer can only be used with <? extends ArtExtendedView>");
    
    view = artExtendedView;
    
    super.draw(context);
  }
  
  @Override
  public void drawChars(MenuContext context, Set<Character> characters) {
    if (!(context.view() instanceof ArtExtendedView<? extends Ingredient, ? extends ArtIngredient> artExtendedView))
      throw new IllegalArgumentException("ArtExtendedViewDrawer can only be used with <? extends ArtExtendedView>");
    
    view = artExtendedView;
    
    super.drawChars(context, characters);
  }
  
  @Override
  protected ItemStack findItem(MenuContext context, int slot, char shapeChar) {
    ItemStack item = super.findItem(context, slot, shapeChar);
    if (item != null) return item;
    
    ArtIngredient art = view.getArts().get(shapeChar);
    int draws = artDraws.getOrDefault(shapeChar, 0);
    if (art.getMaxDraws() > -1 && draws > art.getMaxDraws()) return null;
    
    artDraws.put(shapeChar, draws + 1);
    return view.getArts().get(shapeChar).getItem(context);
  }
}
