package ru.cwcode.tkach.refreshmenu.inventory.view.drawer;

import org.bukkit.inventory.ItemStack;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.CachedIngredient;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public abstract class AbstractDrawer {
  Map<Ingredient, ItemStack> ingredientCache = Collections.synchronizedMap(new WeakHashMap<>());
  
  public abstract void draw(MenuContext context);
  public abstract void drawChars(MenuContext context, Collection<Character> characters);
  public abstract void updateRequired(MenuContext context);
  
  public ItemStack getCachedItem(MenuContext menuContext, Ingredient ingredient) {
    if (!(ingredient instanceof CachedIngredient)) return ingredient.getItem(menuContext);
    
    if (ingredient.shouldRefresh(menuContext)) {
      ItemStack newItem = ingredient.getItem(menuContext);
      
      if (newItem == null) {
        newItem = ingredientCache.get(ingredient);
      } else {
        ingredientCache.put(ingredient, newItem);
      }
      
      return newItem;
    }
    
    ItemStack cached = ingredientCache.get(ingredient);
    if (cached == null) {
      cached = ingredient.getItem(menuContext);
      
      if (cached != null) {
        ingredientCache.put(ingredient, cached);
      }
    }
    
    return cached;
  }
}
