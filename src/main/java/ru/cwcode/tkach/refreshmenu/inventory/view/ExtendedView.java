package ru.cwcode.tkach.refreshmenu.inventory.view;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.RefreshMenu;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.view.drawer.ExtendedViewDrawer;

import java.util.Set;

public class ExtendedView<T extends Ingredient> extends PagedView<T> {
  @Override
  public void onOwnInventoryClick(InventoryClickEvent event) {
    shape.findCharAtIndex(event.getRawSlot()).ifPresent(character -> {
      execute(((Player) event.getWhoClicked()), () -> {
        boolean success = behavior.execute(event, new Behavior.ClickData(character, event.getClick()));
        if (success) {
          prepareForDrawing();
          drawer.drawChars(new MenuContext(this, (Player) event.getWhoClicked()), Set.of(character));
        }
      });
      
      super.onOwnInventoryClick(event);
    });
  }
  
  @Override
  public boolean onInventoryClose(InventoryCloseEvent event) {
    boolean isClosed = super.onInventoryClose(event);
    
    if (isClosed) { //update players phantom inventory to real
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
