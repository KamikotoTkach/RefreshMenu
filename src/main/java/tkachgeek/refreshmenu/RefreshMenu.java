package tkachgeek.refreshmenu;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class RefreshMenu extends JavaPlugin implements @NotNull Listener {
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
