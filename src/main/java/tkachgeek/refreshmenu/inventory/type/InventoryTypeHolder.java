package tkachgeek.refreshmenu.inventory.type;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

@JsonTypeInfo(
   use = JsonTypeInfo.Id.NAME,
   include = JsonTypeInfo.As.PROPERTY,
   property = "type")
@JsonSubTypes({
   @JsonSubTypes.Type(value = InventoryTypeHolderImpl.class, name = "type"),
   @JsonSubTypes.Type(value = ChestType.class, name = "chest")
})
public interface InventoryTypeHolder {
  int getSize();
  
  InventoryType getType();
  
  Inventory createInventory(Component name);
}
