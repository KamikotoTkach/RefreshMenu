package tkachgeek.refreshmenu.inventory.ingredient;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tkachgeek.config.minilocale.Placeholders;
import tkachgeek.refreshmenu.MenuContext;
import tkachgeek.tkachutils.items.ItemBuilderFactory;

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
  
  default ItemStack getItem(Placeholders placeholders) {
    return ItemBuilderFactory.of(Material.BARRIER)
                             .name(Component.text("Переопредели getItem(...)"))
                             .build();
  }
  
  default ItemStack getItem(MenuContext context) {
    return getItem(context.view().getPlaceholders());
  }
  
  default boolean shouldRefresh(MenuContext context) {
    return false;
  }
}
