package tkachgeek.refreshmenu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import tkachgeek.refreshmenu.inventory.view.View;

import java.util.Optional;

public class MenuListener implements Listener {
  
  @EventHandler
  void onInventoryClick(InventoryClickEvent event) {
    Optional<View> view = ManagerRegistry.getViewForInventory(event.getInventory());
    if (!view.isPresent()) return;
    
    if (event.getClickedInventory() == null) {
      view.get().onOutsideClick(event);
      return;
    }
    
    if (event.getClickedInventory().equals(event.getWhoClicked().getInventory())) {
      view.get().onOwnInventoryClick(event);
    } else {
      view.get().onInventoryClick(event);
    }
  }
  
  @EventHandler
  void onInventoryClose(InventoryCloseEvent event) {
    Optional<ManagerRegistry.HierarchyResult> menu = ManagerRegistry.getAllForInventory(event.getInventory());
    if (!menu.isPresent()) return;
    
    menu.get().manager.onInventoryClose(event, menu.get());
    menu.get().view.onInventoryClose(event, menu.get());
    
  }
  
  @EventHandler
  void onInventoryDrag(InventoryDragEvent event) {
    Optional<View> view = ManagerRegistry.getViewForInventory(event.getInventory());
    if (!view.isPresent()) return;
    
    view.get().onDrag(event);
  }
}
