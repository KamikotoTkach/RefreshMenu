package tkachgeek.refreshmenu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import tkachgeek.refreshmenu.inventory.view.View;

public class MenuListener implements Listener {
  
  @EventHandler
  void onInventoryClick(InventoryClickEvent event) {
    if (event.getInventory().getHolder() instanceof View view) {
      if (event.getClickedInventory() == null) {
        view.onOutsideClick(event);
      } else if (event.getClickedInventory().equals(event.getWhoClicked().getInventory())) {
        view.onOwnInventoryClick(event);
      } else {
        view.onInventoryClick(event);
      }
    }
  }
  
  @EventHandler
  void onInventoryClose(InventoryCloseEvent event) {
    if (event.getInventory().getHolder() instanceof View view) {
      view.getMenu().getManager().onInventoryClose(event, view);
      view.onInventoryClose(event);
    }
  }
  
  @EventHandler
  void onInventoryDrag(InventoryDragEvent event) {
    if (event.getInventory().getHolder() instanceof View view) {
      view.onDrag(event);
    }
  }
}
