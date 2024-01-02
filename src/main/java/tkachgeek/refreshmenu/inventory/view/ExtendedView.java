package tkachgeek.refreshmenu.inventory.view;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;
import tkachgeek.refreshmenu.inventory.view.drawer.ExtendedViewDrawer;
import tkachgeek.refreshmenu.inventory.view.drawer.PagedViewDrawer;
import tkachgeek.tkachutils.protocol.Packet;

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
  protected void onOpen(Player player) {
    JavaPlugin plugin = this.getMenu().getManager().getPlugin();
    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
      Packet.clearInventory(player);
      super.onOpen(player);
    });
  }
}
