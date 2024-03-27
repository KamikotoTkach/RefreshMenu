package ru.cwcode.tkach.refreshmenu.configurationUI.type;

import java.lang.reflect.Field;

public class StringTypeMapper implements TypeMapper {
  
  @Override
  public void setValueRaw(Object object, Field field, String value) throws Exception {
    field.set(object, value);
  }
}
