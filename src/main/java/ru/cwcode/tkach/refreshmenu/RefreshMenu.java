package ru.cwcode.tkach.refreshmenu;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.cwcode.tkach.refreshmenu.protocol.PacketListener;
import ru.cwcode.tkach.refreshmenu.refresh.MenuRefreshManager;

public final class RefreshMenu extends JavaPlugin {
  public static JavaPlugin plugin;
  @Getter
  private static MenuRefreshManager menuRefreshManager;
  
  public static MenuManager getManager(JavaPlugin plugin) {
    return new MenuManager(plugin);
  }
  
  @Override
  public void onEnable() {
    plugin = this;
    menuRefreshManager = new MenuRefreshManager(this);
    
    Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
    
    new PacketListener();
  }
}
