package ru.cwcode.tkach.refreshmenu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.cwcode.tkach.refreshmenu.inventory.view.View;
import ru.cwcode.tkach.refreshmenu.protocol.OpenedViewService;

public class MenuListener implements Listener {

  @EventHandler(priority = EventPriority.LOW)
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

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  void onInventoryOpen(InventoryOpenEvent event) {
    if (!(event.getInventory().getHolder() instanceof View view)) return;
    if (!(event.getPlayer() instanceof Player player)) return;

    OpenedViewService.register(player, view);
  }

  @EventHandler(priority = EventPriority.LOW)
  void onInventoryClose(InventoryCloseEvent event) {
    if (event.getInventory().getHolder() instanceof View view) {
      if (event.getPlayer() instanceof Player player) {
        OpenedViewService.unregister(player, view);
      }

      boolean isClosed = view.onInventoryClose(event);

      if (isClosed) {
        view.getMenu().getManager().onInventoryClose(event, view);
      }
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  void onPlayerQuit(PlayerQuitEvent event) {
    OpenedViewService.unregister(event.getPlayer());
  }


  @EventHandler(priority = EventPriority.LOW)
  void onInventoryDrag(InventoryDragEvent event) {
    if (event.getInventory().getHolder() instanceof View view) {
      view.onDrag(event);
    }
  }
}
