package ru.cwcode.tkach.refreshmenu.inventory.ingredient.action;

import org.bukkit.event.inventory.ClickType;
import ru.cwcode.tkach.config.relocate.com.fasterxml.jackson.annotation.JsonSubTypes;
import ru.cwcode.tkach.config.relocate.com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.cwcode.tkach.refreshmenu.MenuContext;

@JsonTypeInfo(
   use = JsonTypeInfo.Id.NAME,
   include = JsonTypeInfo.As.PROPERTY,
   property = "@type")
@JsonSubTypes({
   @JsonSubTypes.Type(value = ConsoleCommandAction.class, name = "consoleCommand"),
   @JsonSubTypes.Type(value = PlayerCommandAction.class, name = "playerCommand"),
   @JsonSubTypes.Type(value = SendMessageAction.class, name = "sendMessage"),
   @JsonSubTypes.Type(value = SetViewAction.class, name = "setView"),
   @JsonSubTypes.Type(value = CloseInventoryAction.class, name = "closeInventory"),
   @JsonSubTypes.Type(value = NextPageIngredientAction.class, name = "nextPage"),
   @JsonSubTypes.Type(value = PrevPageIngredientAction.class, name = "prevPage"),
})
public interface Action {
  void accept(MenuContext context, ClickType clickType);
}
