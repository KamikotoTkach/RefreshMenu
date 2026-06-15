package ru.cwcode.tkach.refreshmenu;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ru.cwcode.tkach.refreshmenu.inventory.Menu;
import ru.cwcode.tkach.refreshmenu.inventory.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class MenuManager implements Listener {
  private final JavaPlugin plugin;
  List<Menu> activeMenu = new ArrayList<>();

  protected MenuManager(JavaPlugin plugin) {
    this.plugin = plugin;
    Bukkit.getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    UUID id = event.getPlayer().getUniqueId();

    for (Menu menu : new ArrayList<>(activeMenu)) {
      if (!menu.isParticipant(id)) continue;
      if (menu.hasOnlineParticipantExcept(id)) continue;

      removeActiveMenu(menu);
    }
  }
  
  public void addActiveMenu(Menu menu) {
    if (hasActiveMenu(menu)) return;
    activeMenu.add(menu);
  }

  public void removeActiveMenu(Menu menu) {
    activeMenu.remove(menu);
    
    for (View view : menu.getViews()) {
      RefreshMenu.getMenuRefreshManager().tryUnregister(view);
      view.onUnload();
    }
  }

  public boolean hasActiveMenu(Menu menu) {
    return activeMenu.contains(menu);
  }

  public void onInventoryClose(InventoryCloseEvent event, View view) {
    Bukkit.getScheduler().runTask(plugin, () -> {
      if (!view.getMenu().hasViewers() && view.getMenu().shouldUnload) removeActiveMenu(view.getMenu());
    });
  }

  public void open(Menu menu, Player player) {
    menu.setMenuManager(this);
    addActiveMenu(menu);
    menu.open(player);
  }
}
