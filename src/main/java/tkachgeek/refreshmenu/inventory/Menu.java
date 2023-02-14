package tkachgeek.refreshmenu.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import tkachgeek.config.yaml.YmlConfig;
import tkachgeek.refreshmenu.inventory.view.Behavior;
import tkachgeek.refreshmenu.inventory.view.View;

import java.util.HashMap;

public class Menu extends YmlConfig {
  
  public transient boolean shouldUnload = true;
  public HashMap<String, View> views = new HashMap<>();
  
  {
    views.put("default", new View());
  }
  
  public void open(Player player) {
    views.get("default").open(player);
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
}
