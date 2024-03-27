package ru.cwcode.tkach.refreshmenu.configurationUI.type;

import java.lang.reflect.Field;

public class EnumTypeMapper implements TypeMapper {
  @Override
  public void setValueRaw(Object object, Field field, String value) throws Exception {
    checkNotEmpty(value);
    
    Object[] constants = field.getType().getEnumConstants();
    for (Object constant : constants) {
      if (constant.toString().equalsIgnoreCase(value)) {
        field.set(object, constant);
        return;
      }
    }
    
    throw new IllegalArgumentException("No enum constant found");
  }
}
