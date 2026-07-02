package ru.cwcode.tkach.refreshmenu;

import com.github.retrooper.packetevents.PacketEvents;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.cwcode.tkach.refreshmenu.protocol.PacketListener;
import ru.cwcode.tkach.refreshmenu.refresh.MenuRefreshManager;

import java.util.ArrayList;
import java.util.List;

public final class RefreshMenu extends JavaPlugin {
  private static final List<MenuManager> menuManagers = new ArrayList<>();
  
  @Getter
  private static MenuRefreshManager menuRefreshManager;
  public static JavaPlugin plugin;
  
  public static MenuManager getManager(JavaPlugin plugin) {
    MenuManager menuManager = new MenuManager(plugin);
    menuManagers.add(menuManager);
    
    return menuManager;
  }
  
  @Override
  public void onEnable() {
    plugin = this;
    menuRefreshManager = new MenuRefreshManager(this);
    
    Bukkit.getPluginManager().registerEvents(new MenuListener(), this);

    //packetevents runs as a standalone plugin, so the API is already loaded/initialized — we only register the listener
    PacketEvents.getAPI().getEventManager().registerListener(new PacketListener());
  }
  
  @Override
  public void onDisable() {
    new ArrayList<>(menuManagers).forEach(MenuManager::closeActiveMenus);
  }
}
