package ru.cwcode.tkach.refreshmenu.inventory.ingredient.action;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import ru.cwcode.tkach.refreshmenu.MenuContext;

/**
 * Проигрывает звук нажавшему игроку.
 * <p>
 * В click-DSL: {@code [LMB] sound ENTITY_EXP_ORB_PICKUP} либо с громкостью/питчем:
 * {@code [LMB] sound ENTITY_EXP_ORB_PICKUP 1.0 1.2}.
 */
public class SoundAction implements Action {
  String sound = "UI_BUTTON_CLICK";
  float volume = 1f;
  float pitch = 1f;

  public SoundAction() {
  }

  public SoundAction(String raw) {
    String[] parts = raw.trim().split("\\s+");
    if (parts.length > 0 && !parts[0].isEmpty()) sound = parts[0];
    if (parts.length > 1) volume = parseFloat(parts[1], 1f);
    if (parts.length > 2) pitch = parseFloat(parts[2], 1f);
  }

  @Override
  public void accept(MenuContext context, ClickType clickType) {
    try {
      Sound parsed = Sound.valueOf(sound.toUpperCase());
      Player player = context.player();
      player.playSound(player.getLocation(), parsed, volume, pitch);
    } catch (IllegalArgumentException ignored) {
      // неизвестный звук — молча игнорируем, чтобы не ронять отрисовку меню
    }
  }

  private static float parseFloat(String s, float def) {
    try {
      return Float.parseFloat(s);
    } catch (NumberFormatException e) {
      return def;
    }
  }
}
