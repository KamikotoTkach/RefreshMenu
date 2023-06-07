package tkachgeek.refreshmenu.configurationUI.valueSupplier;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tkachgeek.refreshmenu.inventory.view.View;

import java.lang.reflect.Field;

public class ItemStackValueSupplier implements ValueSupplier{
  @Override
  public void supplyValue(Field field, Object object, Player player, View view) {
    ItemStack item = player.getInventory().getItemInMainHand();
    if(item.getType().isAir()) {
      player.sendMessage(Component.text("Возьмите предмет в руку"));
      return;
    }
    
    try {
      field.set(object,item.clone());
      view.open(player);
    } catch (Exception e) {
      player.sendMessage(e.getMessage());
    }
  }
  
  @Override
  public String getValueFancy(Object object, Field field) {
    try {
      return field.get(object).toString();
    } catch (IllegalAccessException e) {
      return "Не задано";
    }
  }
  
  @Override
  public boolean supports(Class<?> type) {
    return type == ItemStack.class;
  }
}
