package ru.cwcode.tkach.refreshmenu.configurationUI.type;

import net.kyori.adventure.text.Component;

import java.lang.reflect.Field;

public interface TypeMapper {
  TypeMapperRegistry registry = new TypeMapperRegistry();
  
  default String getValue(Object object, Field field) {
    try {
      return getValueRaw(object, field);
    } catch (Exception e) {
      return "null";
    }
  }
  default String getValueFancy(Object object, Field field) {
    try {
      return getValueRaw(object, field);
    } catch (Exception e) {
      return "<gray>Значение не задано";
    }
  }
  
  default String getValueRaw(Object object, Field field) throws Exception {
    return field.get(object).toString();
  }
  
  default SetValueResult setValue(Object object, Field field, String value) {
    try {
      setValueRaw(object, field, value);
      return SetValueResult.OK;
    } catch (Exception e) {
      return new SetValueResult(e.getMessage());
    }
  }
  default void checkNotEmpty(String value) throws IllegalArgumentException {
    if(value.isEmpty()) throw new IllegalArgumentException("Value is empty");
  }
  
  void setValueRaw(Object object, Field field, String value) throws Exception;
  
  class SetValueResult {
    static SetValueResult OK = new SetValueResult();
    String message = null;
    boolean ok = true;
    
    public SetValueResult() {
    }
    
    public SetValueResult(String message) {
      this.message = message;
      this.ok = false;
    }
    
    public boolean hasMessage() {
      return message != null;
    }
    
    public Component getMessage() {
      return Component.text(message);
    }
    
    public boolean isOK() {
      return ok;
    }
  }
}
