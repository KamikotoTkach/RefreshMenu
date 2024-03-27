package ru.cwcode.tkach.refreshmenu.configurationUI.type;

import java.lang.reflect.Field;
import java.util.Set;

public class BooleanTypeMapper implements TypeMapper {
  public static final Set<String> TRUE = Set.of("true", "TRUE", "1", "+", "да", "yes", "on");
  public static final Set<String> FALSE = Set.of("false", "FALSE", "0", "-", "нет", "no", "off");
  
  @Override
  public void setValueRaw(Object object, Field field, String value) throws Exception {
    checkNotEmpty(value);
    
    if (TRUE.contains(value)) {
      field.set(object, true);
    } else if (FALSE.contains(value)) {
      field.set(object, false);
      return;
    }
    
    throw new IllegalArgumentException("`" + value + "` is not a boolean value");
  }
}
