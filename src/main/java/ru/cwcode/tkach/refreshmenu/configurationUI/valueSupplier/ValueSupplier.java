package ru.cwcode.tkach.refreshmenu.configurationUI.valueSupplier;

import org.bukkit.entity.Player;
import ru.cwcode.tkach.refreshmenu.inventory.view.View;

import java.lang.reflect.Field;

public interface ValueSupplier {
  ValueSupplierRegistry registry = new ValueSupplierRegistry();
  
  static ValueSupplier match(Class<?> type) {
    return registry.match(type);
  }
  
  void supplyValue(Field field, Object object, Player player, View view);
  
  String getValueFancy(Object object, Field field);
  
  boolean supports(Class<?> type);
}
