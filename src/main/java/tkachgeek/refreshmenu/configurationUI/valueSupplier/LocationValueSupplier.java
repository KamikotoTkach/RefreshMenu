package tkachgeek.refreshmenu.configurationUI.valueSupplier;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import tkachgeek.refreshmenu.inventory.view.View;
import tkachgeek.tkachutils.numbers.NumbersUtils;

import java.lang.reflect.Field;

public class LocationValueSupplier implements ValueSupplier {
  @Override
  public void supplyValue(Field field, Object object, Player player, View view) {
    try {
      field.set(object, player.getLocation());
      view.open(player);
    } catch (IllegalAccessException e) {
      player.sendMessage(Component.text("Не удалось установить локацию"));
    }
  }
  
  @Override
  public String getValueFancy(Object object, Field field) {
    try {
      Location location = (Location) field.get(object);
      return "%s, %s %s %s, %s %s".formatted(location.getWorld().getName(),
                                             NumbersUtils.round(location.getX(), 1),
                                             NumbersUtils.round(location.getY(), 1),
                                             NumbersUtils.round(location.getZ(), 1),
                                             NumbersUtils.round(location.getPitch(), 1),
                                             NumbersUtils.round(location.getYaw(), 1));
    } catch (Exception e) {
      return "Не задано";
    }
  }
  
  @Override
  public boolean supports(Class<?> type) {
    return type == Location.class;
  }
}
