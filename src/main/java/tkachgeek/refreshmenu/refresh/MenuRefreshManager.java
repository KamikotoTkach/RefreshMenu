package tkachgeek.refreshmenu.refresh;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tkachgeek.refreshmenu.inventory.view.PagedView;
import tkachgeek.refreshmenu.inventory.view.View;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MenuRefreshManager {
  ConcurrentHashMap<PagedView<?>, Refresh> views = new ConcurrentHashMap<>();
  
  public MenuRefreshManager(JavaPlugin plugin) {
    Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::tick, 1, 1);
  }
  
  public void tryRegister(View view) {
    if (view instanceof PagedView<?> pagedView) {
      extractRefreshableAnnotation(view).ifPresent(refresh -> {
        views.put(pagedView, refresh);
      });
    }
  }
  
  public void tryUnregister(View view) {
    if (view instanceof PagedView<?>) {
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
        if (view.hasViewers() && Bukkit.getCurrentTick() % refresh.delay() == 0) {
          view.updateRequired(view.getPlayer()); //todo: было бы неплохо переписать под мульти-пользовательскую архитектуру и желательно без потери совместимости
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
}
