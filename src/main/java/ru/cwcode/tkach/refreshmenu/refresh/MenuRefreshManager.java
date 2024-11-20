package ru.cwcode.tkach.refreshmenu.refresh;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.cwcode.tkach.refreshmenu.inventory.view.View;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MenuRefreshManager {
  ConcurrentHashMap<Refreshable, Refresh> views = new ConcurrentHashMap<>();
  
  public MenuRefreshManager(JavaPlugin plugin) {
    Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::tick, 1, 1);
  }
  
  public void tryRegister(View view) {
    if (view instanceof Refreshable refreshable) {
      extractRefreshableAnnotation(view).ifPresent(refresh -> {
        views.put(refreshable, refresh);
      });
    }
  }
  
  public void tryUnregister(View view) {
    if (view instanceof Refreshable) {
      views.remove(view);
    }
  }
  
  public Optional<Refresh> extractRefreshableAnnotation(View view) {
    Refresh refresh;
    if ((refresh = view.getClass().getAnnotation(Refresh.class)) != null) {
      return Optional.of(refresh);
    }
    
    return Optional.empty();
  }
  
  private void tick() {
    views.forEach((view, refresh) -> {
      try {
        if (Bukkit.getCurrentTick() % refresh.delay() == 0) {
          view.refresh();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
}
