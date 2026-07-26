package ru.cwcode.tkach.refreshmenu.protocol;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.cwcode.tkach.refreshmenu.inventory.view.View;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class OpenedViewService {
  private Map<UUID, OpenedView> views = new ConcurrentHashMap<>();

  public void register(Player player, View view) {
    views.put(player.getUniqueId(), new OpenedView(view));
  }

  public void unregister(Player player, View view) {
    views.computeIfPresent(player.getUniqueId(), (uuid, opened) -> opened.getView() == view ? null : opened);
  }

  public void unregister(Player player) {
    views.remove(player.getUniqueId());
  }

  public void clear() {
    views.clear();
  }

  @Nullable
  OpenedView get(@Nullable UUID uuid) {
    return uuid == null ? null : views.get(uuid);
  }

  void bindWindow(UUID uuid, int windowId) {
    OpenedView opened = get(uuid);
    if (opened == null) return;

    opened.bindWindow(windowId);
  }
}
