package ru.cwcode.tkach.refreshmenu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import ru.cwcode.tkach.refreshmenu.inventory.view.View;

import java.util.WeakHashMap;

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
      boolean isClosed = view.onInventoryClose(event);
      
      if (isClosed) {
        view.getMenu().getManager().onInventoryClose(event, view);
      }
    }
  }
  @EventHandler
  void onInventoryOpen(InventoryOpenEvent event) {
    if (event.getInventory().getHolder() instanceof View view) {
      RefreshMenu.getApi().setOpenedView((Player) event.getPlayer(), view);
    }
  }
  
  @EventHandler
  void onInventoryDrag(InventoryDragEvent event) {
    if (event.getInventory().getHolder() instanceof View view) {
      view.onDrag(event);
    }
  }
}
