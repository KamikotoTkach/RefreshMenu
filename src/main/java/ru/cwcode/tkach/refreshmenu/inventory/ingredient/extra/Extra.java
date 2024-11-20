package ru.cwcode.tkach.refreshmenu.inventory.ingredient.extra;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.cwcode.tkach.config.relocate.com.fasterxml.jackson.annotation.JsonSubTypes;
import ru.cwcode.tkach.config.relocate.com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.ExtraIngredient;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = ExtraState.class, name = "state"),
  @JsonSubTypes.Type(value = ExtraBind.class, name = "bind"),
})
public interface Extra {
  
  default void onClick(ExtraIngredient extraIngredient, MenuContext context, ClickType clickType) {
  
  }
  
  default boolean shouldRefresh(ExtraIngredient extraIngredient, MenuContext context) {
    return false;
  }
  
  default ItemStack getItem(ExtraIngredient extraIngredient, MenuContext context) {
    return null;
  }
  
  default boolean isHandlingOnClick(ExtraIngredient extraIngredient, MenuContext context, ClickType clickType) {
    return false;
  }
  
  default boolean isHandlingShouldRefresh(ExtraIngredient extraIngredient, MenuContext context) {
    return false;
  }
  
  default boolean isHandlingGetItem(ExtraIngredient extraIngredient, MenuContext context) {
    return false;
  }
}
