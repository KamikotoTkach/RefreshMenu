package ru.cwcode.tkach.refreshmenu.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
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
      PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.OPEN_WINDOW);
      
      packet.getIntegers().write(0, window.id());
      packet.getModifier().write(1, window.type());
      packet.getChatComponents().write(0, WrappedChatComponent.fromJson(GsonComponentSerializer.gson().serialize(title)));
      
      ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    });
  }
  
  void handleOpen(Player player, Integer windowID, Object type) {
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
