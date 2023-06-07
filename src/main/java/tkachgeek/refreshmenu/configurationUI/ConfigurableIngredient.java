package tkachgeek.refreshmenu.configurationUI;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tkachgeek.config.minilocale.Placeholders;
import tkachgeek.refreshmenu.configurationUI.valueSupplier.ValueSupplier;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;
import tkachgeek.refreshmenu.inventory.view.View;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ConfigurableIngredient implements Ingredient {
  String name;
  List<String> description = new ArrayList<>();
  Material material;
  Field field;
  private ValueSupplier valueRequire;
  
  public ConfigurableIngredient(Object object, Field field, UIConfigurable configurable) {
    this.field = field;
    this.valueRequire = ValueSupplier.match(field.getType());
    
    name = configurable.name().isEmpty() ? field.getName() : configurable.name();
    material = configurable.material();
    
    if (!configurable.description().isEmpty()) {
      description = new ArrayList<>(List.of(configurable.description().split("\\n")));
      description.add("");
    }
    
    description.add("<gray>Сейчас установлено значение: <white>" + valueRequire.getValueFancy(object, field));
    
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
    valueRequire.supplyValue(field, object, player, view);
  }
  
 
}
