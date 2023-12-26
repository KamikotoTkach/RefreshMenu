package tkachgeek.refreshmenu.inventory.view;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;
import tkachgeek.tkachutils.protocol.Packet;

public class ExtendedView<T extends Ingredient> extends PagedView<T> {
  @Override
  protected void updateDynamicContent(Player player) {
    ViewDrawer.drawExtendedPage(this, player);
  }

  @Override
  protected void onOpen(Player player) {
    JavaPlugin plugin = this.getMenu().getManager().getPlugin();
    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
      Packet.clearInventory(player);
      super.onOpen(player);
    });
  }
}
