package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.cwcode.tkach.config.relocate.com.fasterxml.jackson.annotation.JsonSubTypes;
import ru.cwcode.tkach.config.relocate.com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.cwcode.tkach.locale.Placeholders;
import ru.cwcode.tkach.refreshmenu.MenuContext;

@JsonTypeInfo(
   use = JsonTypeInfo.Id.NAME,
   property = "@type")
@JsonSubTypes({
})
public interface ArtIngredient extends Ingredient {
  char getChar();
  
  default byte getMaxDraws() {
    return Byte.MAX_VALUE;
  }
  
  default ItemStack getEmptyItem(Placeholders placeholders) {
    return new ItemStack(Material.AIR);
  }
  
  default ItemStack getEmptyItem(MenuContext context) {
    return getEmptyItem(context.view().getPlaceholders());
  }
}
