package ru.cwcode.tkach.refreshmenu;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemFlag;
import ru.cwcode.cwutils.items.ItemBuilder;
import ru.cwcode.tkach.locale.Placeholders;
import ru.cwcode.tkach.locale.platform.MiniLocale;
import ru.cwcode.tkach.locale.preprocessor.MessagePreprocessors;
import ru.cwcode.tkach.locale.wrapper.adventure.MiniMessageWrapper;

import java.util.List;

public class Utils {
  
  public static final MessagePreprocessors MESSAGE_PREPROCESSOR = MiniLocale.getInstance().messagePreprocessors();
  public static final MiniMessageWrapper MINI_MESSAGE_WRAPPER = MiniLocale.getInstance().miniMessageWrapper();
  
  public static Component deserialize(String string, Placeholders placeholders, Audience viewer, boolean disableItalic) {
    return MINI_MESSAGE_WRAPPER.deserialize(MESSAGE_PREPROCESSOR.preprocess(string, viewer), placeholders, disableItalic);
  }
  
  public static List<Component> deserialize(List<String> strings, Placeholders placeholders, Audience viewer, boolean disableItalic) {
    return MINI_MESSAGE_WRAPPER.deserialize(MESSAGE_PREPROCESSOR.preprocess(strings, viewer), placeholders, disableItalic);
  }

  public static void applyItemFlags(ItemBuilder item, List<ItemFlag> itemFlags) {
    if (itemFlags != null && !itemFlags.isEmpty()) item.flags(itemFlags.toArray(new ItemFlag[0]));
  }

  public static void applyCommon(ItemBuilder item, String name, List<String> description, int amount, List<ItemFlag> itemFlags, MenuContext context) {
    applyCommon(item, name, description, amount, itemFlags, context.view().getPlaceholders(), context.player());
  }

  public static void applyCommon(ItemBuilder item, String name, List<String> description, int amount, List<ItemFlag> itemFlags, Placeholders placeholders, Audience viewer) {
    if (name != null) item.name(deserialize(name, placeholders, viewer, true));
    if (description != null) item.description(deserialize(description, placeholders, viewer, true));
    if (amount != 0) item.amount(amount);
    applyItemFlags(item, itemFlags);
  }
}
