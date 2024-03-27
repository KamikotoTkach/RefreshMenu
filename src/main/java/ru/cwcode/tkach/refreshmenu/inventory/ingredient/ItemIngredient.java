package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import org.bukkit.inventory.ItemStack;
import ru.cwcode.tkach.locale.Placeholders;

public class ItemIngredient implements Ingredient {
  ItemStack item;
  
  public ItemIngredient(ItemStack item) {
    this.item = item;
  }
  
  public ItemIngredient() {
  }
  
  @Override
  public ItemStack getItem(Placeholders placeholders) {
    return item; //todo сделать замену плейсхолдеров в готовом айтемстаке
  }
}
