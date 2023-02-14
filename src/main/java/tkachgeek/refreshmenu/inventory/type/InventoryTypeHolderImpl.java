package tkachgeek.refreshmenu.inventory.type;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class InventoryTypeHolderImpl implements InventoryTypeHolder {
  InventoryType type = InventoryType.CHEST;
  
  public InventoryTypeHolderImpl(InventoryType type) {
    this.type = type;
  }
  
  public InventoryTypeHolderImpl() {
  }
  
  @Override
  public int getSize() {
    return type.getDefaultSize();
  }
  
  @Override
  public InventoryType getType() {
    return type;
  }
  
  @Override
  public Inventory createInventory(Component name) {
    return Bukkit.createInventory(null, getType(), name);
  }
}
