package ru.cwcode.tkach.refreshmenu.inventory.view;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import ru.cwcode.tkach.refreshmenu.RefreshMenu;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.view.drawer.ExtendedViewDrawer;

public class ExtendedView<T extends Ingredient> extends PagedView<T> {
  @Override
  public void onOwnInventoryClick(InventoryClickEvent event) {
    behavior.execute(event, new Behavior.ClickData(shape.charAtIndex(event.getRawSlot()), event.getClick()));
    super.onOwnInventoryClick(event);
  }

  @Override
  protected void initializeDrawer() {
    drawer = new ExtendedViewDrawer();
  }
  
  @Override
  public void onInventoryClose(InventoryCloseEvent event) {
    super.onInventoryClose(event);
    
    Bukkit.getScheduler().runTaskLater(RefreshMenu.plugin,() -> {
      ((Player) event.getPlayer()).updateInventory();
    },1);
  }
}
