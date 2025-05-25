package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import ru.cwcode.tkach.refreshmenu.MenuContext;

/**
 * If {@link #getItem(MenuContext context) getItem} returns null or <code>{@link #shouldRefresh(MenuContext context) shouldRefresh} == false</code>, the cached version will be used. <br>
 * <b>Important:</b> unlike {@link Ingredient}, {@link #shouldRefresh(MenuContext context) shouldRefresh} can be called twice in some cases.<br>
 * <b>Important</b>: unlike {@link Ingredient}, {@link #shouldRefresh(MenuContext context) shouldRefresh} may be called before the first rendering. <br>
 * Therefore, it is necessary to initialize all necessary variables for shouldRefresh not in getItem
 */
public interface CachedIngredient extends Ingredient {
}
