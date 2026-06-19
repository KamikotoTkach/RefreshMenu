package ru.cwcode.tkach.refreshmenu;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
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
  private boolean closingActiveMenus = false;
  
  protected MenuManager(JavaPlugin plugin) {
    this.plugin = plugin;
    Bukkit.getPluginManager().registerEvents(this, plugin);
  }
  
  public void open(Menu menu, Player player) {
    menu.setMenuManager(this);
    addActiveMenu(menu);
    
    menu.open(player);
  }
  
  public boolean hasActiveMenu(Menu menu) {
    return activeMenu.contains(menu);
  }
  
  public void addActiveMenu(Menu menu) {
    if (hasActiveMenu(menu)) return;
    activeMenu.add(menu);
  }
  
  public void removeActiveMenu(Menu menu) {
    if (!activeMenu.remove(menu)) return;
    
    for (View view : menu.getViews()) {
      RefreshMenu.getMenuRefreshManager().tryUnregister(view);
      view.onUnload();
    }
  }
  
  public void onInventoryClose(InventoryCloseEvent event, View view) {
    if (closingActiveMenus || !plugin.isEnabled()) return;
    
    Bukkit.getScheduler().runTask(plugin, () -> {
      boolean needRemove = !view.getMenu().hasViewers() && view.getMenu().shouldUnload;
      if (needRemove) removeActiveMenu(view.getMenu());
    });
  }
  
  public void closeActiveMenus() {
    closingActiveMenus = true;
    
    try {
      for (Menu menu : new ArrayList<>(activeMenu)) {
        closeViewers(menu);
        removeActiveMenu(menu);
      }
    } finally {
      closingActiveMenus = false;
    }
  }
  
  private void closeViewers(Menu menu) {
    for (View view : menu.getViews()) {
      if (!view.isInventoryInitialized()) continue;
      
      for (HumanEntity viewer : new ArrayList<>(view.getInventory().getViewers())) {
        if (!(viewer instanceof Player player)) continue;
        if (player.getOpenInventory().getTopInventory().getHolder() != view) continue;
        
        view.close(player, InventoryCloseEvent.Reason.PLUGIN);
      }
    }
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
}
