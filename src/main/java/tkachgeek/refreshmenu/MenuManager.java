package tkachgeek.refreshmenu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;
import tkachgeek.refreshmenu.inventory.Menu;
import tkachgeek.refreshmenu.inventory.view.View;

import java.util.ArrayList;
import java.util.List;

public class MenuManager {
  private final JavaPlugin plugin;
  List<Menu> activeMenu = new ArrayList<>();

  protected MenuManager(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  public JavaPlugin getPlugin() {
    return plugin;
  }

  public void addActiveMenu(Menu menu) {
    if (hasActiveMenu(menu)) return;
    activeMenu.add(menu);
  }

  public void removeActiveMenu(Menu menu) {
    activeMenu.remove(menu);
  }

  public boolean hasActiveMenu(Menu menu) {
    return activeMenu.contains(menu);
  }

  public void onInventoryClose(InventoryCloseEvent event, View view) {
    if (!view.canCloseHimself && event.getReason() == InventoryCloseEvent.Reason.PLAYER) {
      Bukkit.getScheduler().runTaskLater(plugin, () -> view.open((Player) event.getPlayer()), 1);
      return;
    }
    
    RefreshMenu.getApi().removeOpenedView(((Player) event.getPlayer()));

    if (!view.getMenu().hasViewers() && view.getMenu().shouldUnload) removeActiveMenu(view.getMenu());
  }

  public void open(Menu menu, Player player) {
    menu.setMenuManager(this);
    addActiveMenu(menu);
    menu.open(player);
  }
}
