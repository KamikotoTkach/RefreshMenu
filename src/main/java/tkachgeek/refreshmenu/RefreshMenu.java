package tkachgeek.refreshmenu;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import tkachgeek.refreshmenu.test.TestMenu;

public final class RefreshMenu extends JavaPlugin implements @NotNull Listener {
  public static JavaPlugin plugin;
  MenuManager manager;
  
  public static MenuManager getManager(JavaPlugin plugin) {
    return ManagerRegistry.register(new MenuManager(plugin));
  }
  
  @Override
  public void onEnable() {
    plugin = this;
    manager = getManager(this);
    Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
    Bukkit.getPluginManager().registerEvents(this, this);
  }
  
  @EventHandler
  void onInteract(PlayerInteractEvent event) {
    manager.open(new TestMenu(), event.getPlayer());
  }
}
