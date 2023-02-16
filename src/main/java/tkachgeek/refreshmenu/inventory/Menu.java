package tkachgeek.refreshmenu.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import tkachgeek.config.yaml.YmlConfig;
import tkachgeek.refreshmenu.MenuManager;
import tkachgeek.refreshmenu.inventory.view.Behavior;
import tkachgeek.refreshmenu.inventory.view.View;

import java.util.Collection;
import java.util.HashMap;

public class Menu extends YmlConfig {
  
  public transient boolean shouldUnload = true;
  private final HashMap<String, View> views = new HashMap<>();
  private MenuManager menuManager = null;
  
  {
    views.put("default", new View());
  }
  
  public void open(Player player) {
    openView(player, "default");
  }
  
  public void openView(Player player, String name) {
    View view = views.get(name);
    view.setMenu(this);
    view.open(player);
  }
  
  public void bind(char character, ClickType type, Runnable runnable) {
    for (View view : views.values()) {
      view.getBehavior().bind(character, type, runnable);
    }
  }
  
  public void bind(Behavior.ClickData clickData, Runnable runnable) {
    for (View view : views.values()) {
      view.getBehavior().bind(clickData, runnable);
    }
  }
  
  public boolean hasViewers() {
    for (View value : views.values()) {
      if (value.getInventory().getViewers().size() > 0) return true;
    }
    return false;
  }
  
  public void setView(String name, View view) {
    views.put(name, view);
  }
  
  public MenuManager getManager() {
    return menuManager;
  }
  
  public void setMenuManager(MenuManager manager) {
    this.menuManager = manager;
  }
  
  public Collection<View> getViews() {
    return views.values();
  }
}
