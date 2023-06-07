package tkachgeek.refreshmenu.configurationUI.type;

import java.lang.reflect.Field;

public class DoubleTypeMapper implements TypeMapper {
  @Override
  public void setValueRaw(Object object, Field field, String value) throws Exception {
    checkNotEmpty(value);
    
    try {
      field.set(object, Double.parseDouble(value));
    } catch (Exception e) {
      throw new Exception(value + " is not integer value");
    }
  }
}
