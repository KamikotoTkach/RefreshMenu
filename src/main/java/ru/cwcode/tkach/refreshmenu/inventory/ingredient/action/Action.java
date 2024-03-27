package ru.cwcode.tkach.refreshmenu.inventory.ingredient.action;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.bukkit.event.inventory.ClickType;
import ru.cwcode.tkach.refreshmenu.MenuContext;

@JsonTypeInfo(
   use = JsonTypeInfo.Id.NAME,
   include = JsonTypeInfo.As.PROPERTY,
   property = "type")
@JsonSubTypes({
   @JsonSubTypes.Type(value = ConsoleCommandAction.class, name = "consoleCommand"),
   @JsonSubTypes.Type(value = PlayerCommandAction.class, name = "playerCommand"),
   @JsonSubTypes.Type(value = SendMessageAction.class, name = "sendMessage"),
   @JsonSubTypes.Type(value = SetViewAction.class, name = "setView"),
   @JsonSubTypes.Type(value = CloseInventoryAction.class, name = "closeInventory"),
})
public interface Action {
  void accept(MenuContext context, ClickType clickType);
}
