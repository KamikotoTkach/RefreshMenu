package ru.cwcode.tkach.refreshmenu.configurationUI;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.cwcode.tkach.locale.Placeholders;
import ru.cwcode.tkach.refreshmenu.configurationUI.valueSupplier.ValueSupplier;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.view.View;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ConfigurableIngredient implements Ingredient {
  private final Object object;
  Field field;
  private ValueSupplier valueRequire;
  private final UIConfigurable configurable;
  
  public ConfigurableIngredient(Object object, Field field, UIConfigurable configurable) {
    this.object = object;
    this.field = field;
    this.valueRequire = ValueSupplier.match(field.getType());
    this.configurable = configurable;
  }
  
  @Override
  public ItemStack getItem(Placeholders placeholders) {
    List<String> description = getDescription();
    
    return Ingredient.builder()
                     .name(configurable.name().isEmpty() ? field.getName() : configurable.name())
                     .type(configurable.material())
                     .description(description)
                     .build()
                     .getItem(placeholders);
  }
  
  @NotNull
  private List<String> getDescription() {
    List<String> description = new ArrayList<>();
    
    if (!configurable.description().isEmpty()) {
      description = new ArrayList<>(List.of(configurable.description().split("\\n")));
      description.add("");
    }
    
    description.add("<gray>Сейчас установлено значение: <white>" + valueRequire.getValueFancy(object, field));
    
    description.add("<gray>ЛКМ чтобы установить новое значение");
    return description;
  }
  
  public void requireValue(Object object, Player player, View view) {
    valueRequire.supplyValue(field, object, player, view);
  }
}
