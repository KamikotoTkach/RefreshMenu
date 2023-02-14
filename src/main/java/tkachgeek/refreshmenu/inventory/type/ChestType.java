package tkachgeek.refreshmenu.inventory.type;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class ChestType implements InventoryTypeHolder {
  int size = 27;
  
  public ChestType(int size) {
    setSize(size);
  }
  
  public ChestType() {
  }
  
  @Override
  public int getSize() {
    return size;
  }
  
  @Override
  public InventoryType getType() {
    return InventoryType.CHEST;
  }
  
  @Override
  public Inventory createInventory(Component name) {
    return Bukkit.createInventory(null, getSize(), name);
  }
  
  public int setSize(int size) {
    this.size = getNearest(size);
    return size;
  }
  
  private static int getNearest(int size) {
    if (size <= 0) return 0;
    return Math.min(54, ((size - 1) / 9 + 1) * 9);
  }
}
