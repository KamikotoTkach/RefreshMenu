package ru.cwcode.tkach.refreshmenu.inventory.type;

import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import ru.cwcode.tkach.config.relocate.com.fasterxml.jackson.annotation.JsonSubTypes;
import ru.cwcode.tkach.config.relocate.com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.cwcode.tkach.refreshmenu.inventory.view.View;

@JsonTypeInfo(
   use = JsonTypeInfo.Id.NAME,
   property = "type")
@JsonSubTypes({
   @JsonSubTypes.Type(value = InventoryTypeHolderImpl.class, name = "type"),
   @JsonSubTypes.Type(value = ChestType.class, name = "chest")
})
public interface InventoryTypeHolder {
  int getSize();
  
  InventoryType getType();
  
  Inventory createInventory(View view, Component name);
}
