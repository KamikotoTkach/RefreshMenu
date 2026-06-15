package ru.cwcode.tkach.refreshmenu.inventory;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import ru.cwcode.tkach.refreshmenu.MenuManager;
import ru.cwcode.tkach.refreshmenu.RefreshMenu;
import ru.cwcode.tkach.refreshmenu.inventory.view.Behavior;
import ru.cwcode.tkach.refreshmenu.inventory.view.View;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Menu {

  private final HashMap<String, View> views = new HashMap<>();
  private final Set<UUID> participants = new HashSet<>();
  public transient boolean shouldUnload = true;
  @Setter
  transient private MenuManager menuManager = null;
  
  public void open(Player player) {
    openView(player, "default");
  }
  
  public void openView(Player player, String name) {
    View view = views.get(name);
    
    if (view == null) {
      menuManager.getPlugin().getLogger().warning("No view with name `" + name + "`");
      return;
    }
    
    participants.add(player.getUniqueId());

    view.setMenu(this);
    view.open(player);
  }

  public boolean hasOnlineParticipantExcept(UUID except) {
    for (UUID id : participants) {
      if (id.equals(except)) continue;

      Player player = Bukkit.getPlayer(id);
      if (player != null && player.isOnline()) return true;
    }

    return false;
  }

  public boolean isParticipant(UUID id) {
    return participants.contains(id);
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
      if (!value.isInventoryInitialized()) continue;
      if (!value.getInventory().getViewers().isEmpty()) return true;
    }
    
    return false;
  }
  
  public void setView(String name, View view) {
    View prevView = views.put(name, view);
    
    RefreshMenu.getMenuRefreshManager().tryUnregister(prevView);

    view.setMenu(this);
    RefreshMenu.getMenuRefreshManager().tryRegister(view);
  }
  
  public MenuManager getManager() {
    return menuManager;
  }
  
  public Collection<View> getViews() {
    return views.values();
  }
  
  public boolean hasView(String key) {
    return views.containsKey(key);
  }
  
  public View getView(String key) {
    return views.get(key);
  }
}
