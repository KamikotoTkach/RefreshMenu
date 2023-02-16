package tkachgeek.refreshmenu.inventory.ingredient;

import org.bukkit.inventory.ItemStack;
import tkachgeek.config.minilocale.Placeholders;

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
