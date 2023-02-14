package tkachgeek.refreshmenu.inventory.ingredient;

import org.bukkit.inventory.ItemStack;

public class ItemIngredient implements Ingredient {
  ItemStack item;
  
  public ItemIngredient( ItemStack item) {
    this.item = item;
  }
  
  public ItemIngredient() {
  }
  
  @Override
  public ItemStack getItem() {
    return item;
  }
  
}
