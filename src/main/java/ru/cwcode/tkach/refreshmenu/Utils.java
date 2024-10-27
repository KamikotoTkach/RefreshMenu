package ru.cwcode.tkach.refreshmenu;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import ru.cwcode.tkach.locale.Placeholders;
import ru.cwcode.tkach.locale.platform.MessagePreprocessor;
import ru.cwcode.tkach.locale.platform.MiniLocale;
import ru.cwcode.tkach.locale.wrapper.adventure.MiniMessageWrapper;

import java.util.List;

public class Utils {
  
  public static final MessagePreprocessor MESSAGE_PREPROCESSOR = MiniLocale.getInstance().messagePreprocessor();
  public static final MiniMessageWrapper MINI_MESSAGE_WRAPPER = MiniLocale.getInstance().miniMessageWrapper();
  
  public static Component deserialize(String string, Placeholders placeholders, Audience viewer, boolean disableItalic) {
    return MINI_MESSAGE_WRAPPER.deserialize(MESSAGE_PREPROCESSOR.preprocess(string, viewer), placeholders, disableItalic);
  }
  
  public static List<Component> deserialize(List<String> strings, Placeholders placeholders, Audience viewer, boolean disableItalic) {
    return MINI_MESSAGE_WRAPPER.deserialize(MESSAGE_PREPROCESSOR.preprocess(strings, viewer), placeholders, disableItalic);
  }
}
