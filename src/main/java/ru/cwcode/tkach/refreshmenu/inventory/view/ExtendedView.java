package ru.cwcode.tkach.refreshmenu.inventory.view;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import ru.cwcode.cwutils.event.DragType;
import ru.cwcode.tkach.refreshmenu.RefreshMenu;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.view.drawer.ExtendedViewDrawer;

public class ExtendedView<T extends Ingredient> extends PagedView<T> {
  @Override
  public void onOwnInventoryClick(InventoryClickEvent event) {
    if (!getDrawer().isRawSlotControlled(event.getRawSlot())) return;

    shape.findCharAtIndex(event.getRawSlot()).ifPresent(character -> {
      event.setCancelled(true);
      
      handleIngredientClickActionDeferred(event, character);
      handleBehaviorClickAction(event, character);
    });
  }

  @Override
  public void onDrag(DragType dragType, InventoryDragEvent event) {
    for (int rawSlot : event.getRawSlots()) {
      if (rawSlot < getInventory().getSize() || getDrawer().isRawSlotControlled(rawSlot)) {
        super.onDrag(dragType, event);
        return;
      }
    }
  }
  
  @Override
  public boolean onInventoryClose(InventoryCloseEvent event) {
    boolean isClosed = super.onInventoryClose(event);
    
    if (isClosed && RefreshMenu.plugin.isEnabled()) { //update players phantom inventory to real
      Bukkit.getScheduler().runTaskLater(RefreshMenu.plugin, () -> {
        ((Player) event.getPlayer()).updateInventory();
      }, 1);
    }
    
    return isClosed;
  }
  
  @Override
  protected void initializeDrawer() {
    drawer = new ExtendedViewDrawer();
  }
  
  @Override
  public ExtendedViewDrawer getDrawer() {
    return (ExtendedViewDrawer) drawer;
  }
}
