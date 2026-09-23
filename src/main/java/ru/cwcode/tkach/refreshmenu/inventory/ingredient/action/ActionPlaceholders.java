package ru.cwcode.tkach.refreshmenu.inventory.ingredient.action;

import org.bukkit.OfflinePlayer;
import ru.cwcode.tkach.locale.Placeholders;
import ru.cwcode.tkach.locale.placeholders.UnparsedString;
import ru.cwcode.tkach.locale.platform.MiniLocale;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionPlaceholders {
  private static final Pattern TAG = Pattern.compile("<([A-Za-z0-9_-]+)>");

  public static String apply(String line, MenuContext context) {
    if (line == null || line.indexOf('<') < 0) return line;

    Placeholders placeholders = context.view().getPlaceholders().copy().add("player", context.player().getName());
    placeholders.getRaw().replaceAll((key, value) -> raw(value));

    Matcher matcher = TAG.matcher(line);
    StringBuilder result = new StringBuilder();
    while (matcher.find()) {
      String tag = matcher.group();
      String value = placeholders.getRaw().containsKey(matcher.group(1).toLowerCase())
         ? MiniLocale.getInstance().plain(Utils.MINI_MESSAGE_WRAPPER.deserialize(tag, placeholders))
         : tag;
      matcher.appendReplacement(result, Matcher.quoteReplacement(value));
    }
    matcher.appendTail(result);

    return result.toString();
  }

  private static Object raw(Object value) {
    if (value instanceof Number || value instanceof Boolean) return new UnparsedString(String.valueOf(value));
    if (value instanceof OfflinePlayer player) return new UnparsedString(player.getName());

    return value;
  }
}
