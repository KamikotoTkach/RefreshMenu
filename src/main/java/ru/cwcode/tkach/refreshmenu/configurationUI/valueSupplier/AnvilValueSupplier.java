package ru.cwcode.tkach.refreshmenu.configurationUI.valueSupplier;

import net.kyori.adventure.text.Component;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import ru.cwcode.tkach.refreshmenu.RefreshMenu;
import ru.cwcode.tkach.refreshmenu.configurationUI.type.TypeMapper;
import ru.cwcode.tkach.refreshmenu.inventory.view.View;
import ru.cwcode.cwutils.items.ItemBuilderFactory;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public class AnvilValueSupplier implements ValueSupplier {
  
  public void supplyValue(Field field, Object object, Player player, View view) {
    new AnvilGUI.Builder()
       .plugin(RefreshMenu.plugin)
       
       .title("Введите новое значение")
       
       .text(getMapper(field).getValue(object, field))
       
       .itemOutput(ItemBuilderFactory.of(Material.GREEN_BANNER)
                                     .name(Component.text("Сохранить"))
                                     .build())
       
       .onClick((slot, stateSnapshot) -> {
         if (slot == AnvilGUI.Slot.OUTPUT) {
           
           if (!setValue(field, object, stateSnapshot.getText(), player)) {
             return Collections.EMPTY_LIST;
           }
         }
         
         return List.of(
            AnvilGUI.ResponseAction.close(),
            AnvilGUI.ResponseAction.run(() -> view.open(player))
         );
       })
       .open(player);
  }
  
  private static TypeMapper getMapper(Field field) {
    return TypeMapper.registry.getMapper(field.getType());
  }
  
  @Override
  public String getValueFancy(Object object, Field field) {
    return getMapper(field).getValueFancy(object, field);
  }
  
  @Override
  public boolean supports(Class<?> type) {
    return TypeMapper.registry.hasMapper(type);
  }
  
  public boolean setValue(Field field, Object object, String value, Player player) {
    TypeMapper.SetValueResult setValueResult = getMapper(field).setValue(object, field, value);
    
    if (setValueResult.hasMessage()) {
      player.sendMessage(setValueResult.getMessage());
    }
    
    return setValueResult.isOK();
  }
}
