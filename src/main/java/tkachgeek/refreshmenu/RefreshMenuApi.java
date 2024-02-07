package tkachgeek.refreshmenu;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import tkachgeek.refreshmenu.inventory.view.View;

import java.util.WeakHashMap;

public class RefreshMenuApi {
  WeakHashMap<Player, View> openedView = new WeakHashMap<>();
  public @Nullable View getOpenedView(Player player) {
    return openedView.get(player);
  }
  public void setOpenedView(Player player, View view) {
    openedView.put(player,view);
  }
  public void removeOpenedView(Player player) {
    openedView.remove(player);
  }
}
