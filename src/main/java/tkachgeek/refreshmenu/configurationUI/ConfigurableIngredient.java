package tkachgeek.refreshmenu.configurationUI;

import net.kyori.adventure.text.Component;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tkachgeek.config.minilocale.Placeholders;
import tkachgeek.refreshmenu.RefreshMenu;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;
import tkachgeek.refreshmenu.inventory.view.View;
import tkachgeek.tkachutils.items.ItemBuilderFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigurableIngredient implements Ingredient {
  String name;
  List<String> description;
  Material material;
  Field field;
  
  public ConfigurableIngredient(Object object, Field field, UIConfigurable configurable) {
    this.field = field;
    
    name = configurable.name();
    material = configurable.material();
    description = new ArrayList<>(List.of(configurable.description().split("\\n")));
    
    if (name.isEmpty()) name = field.getName();
    
    if (description.size() != 1 || !description.get(0).equals("")) description.add("");
    
    try {
      description.add("<gray>Сейчас установлено значение: <white>" + field.get(object).toString());
    } catch (Exception e) {
      description.add("<gray>Нет значения");
    }
    
    description.add("<gray>ЛКМ чтобы установить новое значение");
  }
  
  @Override
  public ItemStack getItem(Placeholders placeholders) {
    return Ingredient.builder()
                     .name(name)
                     .type(material)
                     .description(description)
                     .build()
                     .getItem(placeholders);
  }
  
  public void requireValue(Object object, Player player, View view) {
    new AnvilGUI.Builder()
       .plugin(RefreshMenu.plugin)
       .title("Введите новое значение")
       
       .text(getFieldValue(object, field))
       
       .itemOutput(ItemBuilderFactory.of(Material.GREEN_BANNER)
                                     .name(Component.text("Сохранить"))
                                     .build())
       
       .onClick((slot, stateSnapshot) -> {
         if (slot == AnvilGUI.Slot.OUTPUT) {
           
           if (!setValue(object, stateSnapshot.getText(), player)) {
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
  
  private String getFieldValue(Object object, Field field) {
    try {
      return field.get(object).toString();
    } catch (Exception e) {
      return "null";
    }
  }
  
  boolean setValue(Object object, String text, Player player) {
    try {
      Class<?> type = field.getType();
      
      if (text.equals("null")) {
        field.set(object, null);
        return true;
      }
      
      if (type == String.class) {
        field.set(object, text);
      } else if (type == int.class) {
        field.set(object, Integer.parseInt(text));
      } else if (type == boolean.class) {
        field.set(object, Boolean.parseBoolean(text));
      } else if (type == byte.class) {
        field.set(object, Byte.parseByte(text));
      } else if (type == short.class) {
        field.set(object, Short.parseShort(text));
      } else if (type == long.class) {
        field.set(object, Long.parseLong(text));
      } else if (type == float.class) {
        field.set(object, Float.parseFloat(text));
      } else if (type == double.class) {
        field.set(object, Double.parseDouble(text));
      } else if (type.isEnum()) {
        Object[] constants = type.getEnumConstants();
        for (Object constant : constants) {
          if (constant.toString().equals(text)) {
            field.set(object, constant);
            return true;
          }
        }
        player.sendMessage("§4Такого типа не существует");
        return false;
      }
    } catch (Exception e) {
      player.sendMessage("§4" + e.getMessage());
      return false;
    }
    return true;
  }
}
