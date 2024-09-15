package ru.cwcode.tkach.refreshmenu;

import net.kyori.adventure.text.Component;
import ru.cwcode.tkach.locale.Placeholders;
import ru.cwcode.tkach.locale.platform.MessagePreprocessor;
import ru.cwcode.tkach.locale.platform.MiniLocale;
import ru.cwcode.tkach.locale.wrapper.adventure.MiniMessageWrapper;

import java.util.List;

public class Utils {
  
  public static final MessagePreprocessor MESSAGE_PREPROCESSOR = MiniLocale.getInstance().messagePreprocessor();
  public static final MiniMessageWrapper MINI_MESSAGE_WRAPPER = MiniLocale.getInstance().miniMessageWrapper();
  
  public static Component deserialize(String string, Placeholders placeholders) {
    String preprocess = MESSAGE_PREPROCESSOR.preprocess(string, null);
    return MINI_MESSAGE_WRAPPER.deserialize(preprocess, placeholders);
  }
  
  public static List<Component> deserialize(List<String> string, Placeholders placeholders) {
    return string.stream().map(x -> deserialize(x, placeholders))
                 .toList();
  }
}
