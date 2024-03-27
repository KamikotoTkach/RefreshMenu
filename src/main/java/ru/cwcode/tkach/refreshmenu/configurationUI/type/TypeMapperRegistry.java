package ru.cwcode.tkach.refreshmenu.configurationUI.type;

import java.util.HashMap;

public class TypeMapperRegistry {
  HashMap<Class<?>, TypeMapper> mappers = new HashMap<>();
  EnumTypeMapper enumTypeMapper = new EnumTypeMapper();
  
  public TypeMapperRegistry() {
    register(String.class, new StringTypeMapper());
    register(boolean.class, new BooleanTypeMapper());
    register(int.class, new IntegerTypeMapper());
    register(double.class, new DoubleTypeMapper());
  }
  
  public void register(Class<?> type, TypeMapper mapper) {
    mappers.put(type, mapper);
  }
  
  public boolean hasMapper(Class<?> type) {
    return getMapper(type) != null;
  }
  
  public TypeMapper getMapper(Class<?> type) {
    TypeMapper fromMap = mappers.get(type);
    if (fromMap != null) return fromMap;
    
    if (type.isEnum()) return enumTypeMapper;
    
    return null;
  }
}
