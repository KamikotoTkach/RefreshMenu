package tkachgeek.refreshmenu.inventory.ingredient;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.bukkit.inventory.ItemStack;
import tkachgeek.config.minilocale.Placeholders;
import tkachgeek.refreshmenu.MenuContext;

@JsonTypeInfo(
   use = JsonTypeInfo.Id.NAME,
   property = "type")
@JsonSubTypes({
   @JsonSubTypes.Type(value = IngredientImpl.class, name = "simple"),
   @JsonSubTypes.Type(value = ItemIngredient.class, name = "item"),
   @JsonSubTypes.Type(value = HeadIngredient.class, name = "head")
})
public interface Ingredient {
  static IngredientBuilder builder() {
    return new IngredientBuilder();
  }
  static HeadIngredientBuilder head() {
    return new HeadIngredientBuilder();
  }
  
  static ItemIngredient of(ItemStack item) {
    return new ItemIngredient(item);
  }
  
  ItemStack getItem(Placeholders placeholders);
  
  default ItemStack getItem(MenuContext context) {
    return getItem(context.view().getPlaceholders());
  }
}
