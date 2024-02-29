package tkachgeek.refreshmenu;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tkachgeek.refreshmenu.protocol.PacketListener;
import tkachgeek.refreshmenu.refresh.MenuRefreshManager;

public final class RefreshMenu extends JavaPlugin {
  public static JavaPlugin plugin;
  private static RefreshMenuApi api = new RefreshMenuApi();
  private static MenuRefreshManager menuRefreshManager;
  
  public static MenuManager getManager(JavaPlugin plugin) {
    return ManagerRegistry.register(new MenuManager(plugin));
  }
  
  public static MenuRefreshManager getMenuRefreshManager() {
    return menuRefreshManager;
  }
  
  public static RefreshMenuApi getApi() {
    return api;
  }
  
  @Override
  public void onEnable() {
    plugin = this;
    menuRefreshManager = new MenuRefreshManager(this);
    
    Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
    
    new PacketListener();
    
  }
}
