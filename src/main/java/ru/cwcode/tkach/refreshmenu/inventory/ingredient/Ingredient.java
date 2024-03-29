package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.cwcode.tkach.locale.Placeholders;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.cwutils.items.ItemBuilderFactory;

@JsonTypeInfo(
   use = JsonTypeInfo.Id.NAME,
   property = "type")
@JsonSubTypes({
   @JsonSubTypes.Type(value = IngredientImpl.class, name = "simple"),
   @JsonSubTypes.Type(value = ItemIngredient.class, name = "item"),
   @JsonSubTypes.Type(value = HeadIngredient.class, name = "head"),
   @JsonSubTypes.Type(value = ActionIngredient.class, name = "action")
})
public interface Ingredient {
  static IngredientBuilder builder() {
    return new IngredientBuilder();
  }
  
  static HeadIngredientBuilder head() {
    return new HeadIngredientBuilder();
  }
  
  static ActionIngredientBuilder action() {
    return new ActionIngredientBuilder();
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
  default void onClick(MenuContext context, ClickType clickType) {
  
  }
}
