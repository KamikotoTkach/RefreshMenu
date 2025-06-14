package ru.cwcode.tkach.refreshmenu.inventory.view;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import ru.cwcode.cwutils.event.DragType;
import ru.cwcode.tkach.refreshmenu.inventory.Menu;

public class AbstractView implements InventoryHolder {
  protected boolean canCloseHimself = true;
  @Setter
  @Getter
  protected Menu menu;
  @Setter
  @Getter
  protected transient Inventory inventory;
  
  public void onOutsideClick(InventoryClickEvent event) {
    event.setCancelled(true);
  }
  
  public void onOwnInventoryClick(InventoryClickEvent event) {
    event.setCancelled(true);
  }
  
  public void onDrag(InventoryDragEvent event) {
    DragType dragType = DragType.getDragType(event);
    onDrag(dragType, event);
  }
  
  public void onDrag(DragType dragType, InventoryDragEvent event) {
    event.setCancelled(true);
  }
  
  public void onInventoryClick(InventoryClickEvent event) {
    event.setCancelled(true);
  }
  
  /**
   * @return true if closed, false if re-opened
   */
  public boolean onInventoryClose(InventoryCloseEvent event) {
    if (!canCloseHimself && event.getReason() == InventoryCloseEvent.Reason.PLAYER) {
      Bukkit.getScheduler().runTaskLater(menu.getManager().getPlugin(), () -> this.open((Player) event.getPlayer()), 1);
      return false;
    }
    
    return true;
  }
  
  public void open(Player player) {
    ItemStack cursor = player.getOpenInventory().getCursor();
    player.getOpenInventory().setCursor(null);
    
    player.openInventory(getInventory());
    
    player.getOpenInventory().setCursor(cursor);
  }
}
