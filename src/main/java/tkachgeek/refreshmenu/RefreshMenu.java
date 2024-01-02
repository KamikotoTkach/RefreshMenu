package tkachgeek.refreshmenu;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class RefreshMenu extends JavaPlugin implements Listener {
  public static JavaPlugin plugin;
  
  public static MenuManager getManager(JavaPlugin plugin) {
    return ManagerRegistry.register(new MenuManager(plugin));
  }
  
  @Override
  public void onEnable() {
    plugin = this;
    Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
  }
}
