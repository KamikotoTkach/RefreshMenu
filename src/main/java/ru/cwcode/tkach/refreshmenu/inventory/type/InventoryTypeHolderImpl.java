package ru.cwcode.tkach.refreshmenu.inventory.type;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import ru.cwcode.tkach.refreshmenu.inventory.view.View;

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
  public Inventory createInventory(View view, Component name) {
    return Bukkit.createInventory(view, getType(), name);
  }
}
