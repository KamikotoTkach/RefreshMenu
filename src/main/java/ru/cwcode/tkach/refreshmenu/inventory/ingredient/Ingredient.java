package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.cwcode.cwutils.items.ItemBuilderFactory;
import ru.cwcode.tkach.config.relocate.com.fasterxml.jackson.annotation.JsonSubTypes;
import ru.cwcode.tkach.config.relocate.com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.cwcode.tkach.locale.Placeholders;
import ru.cwcode.tkach.refreshmenu.MenuContext;

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
  static <I extends IngredientImpl, B extends IngredientBuilder<I, B>> B builder() {
    return (B) new IngredientBuilder<I, B>();
  }
  
  static HeadIngredientBuilder head() {
    return new HeadIngredientBuilder();
  }
  
  /**
   * @deprecated <p> Use {@link Ingredient#extra()} instead
   */
  @Deprecated
  static ActionIngredientBuilder action() {
    return new ActionIngredientBuilder();
  }
  
  static ExtraIngredientBuilder extra() {
    return new ExtraIngredientBuilder();
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
