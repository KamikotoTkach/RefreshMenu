package ru.cwcode.tkach.refreshmenu.protocol;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class OpenedWindowService {
  private HashMap<UUID, OpenedWindow> windows = new HashMap<>();

  public Optional<OpenedWindow> getWindow(Player player) {
    return Optional.ofNullable(windows.get(player.getUniqueId()));
  }

  public void setInventoryTitle(Player player, Component title) {
    getWindow(player).ifPresent(window -> {
      WrapperPlayServerOpenWindow packet = new WrapperPlayServerOpenWindow(window.id(), window.type(), title);
      PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    });
  }

  void handleOpen(Player player, int windowID, int type) {
    UUID uuid = player.getUniqueId();
    windows.put(uuid, new OpenedWindow(windowID, type));
  }

  void handleClose(Player player, int windowID) {
    getWindow(player).ifPresent(openedWindow -> {
      if (openedWindow.id() != windowID) return;

      UUID uuid = player.getUniqueId();
      windows.remove(uuid);
    });
  }
}
