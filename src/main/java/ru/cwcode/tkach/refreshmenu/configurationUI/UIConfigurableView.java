package ru.cwcode.tkach.refreshmenu.configurationUI;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import ru.cwcode.tkach.refreshmenu.configurationUI.valueSupplier.ValueSupplier;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.shape.InventoryShape;
import ru.cwcode.tkach.refreshmenu.inventory.type.ChestType;
import ru.cwcode.tkach.refreshmenu.inventory.view.PagedView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UIConfigurableView extends PagedView<ConfigurableIngredient> {
  private final Consumer<Player> onExit;
  Configurable configurable;
  
  public UIConfigurableView(Configurable configurable) {
    this(configurable, __ -> {});
  }
  
  public UIConfigurableView(Configurable configurable, Consumer<Player> onExit) {
    this.configurable = configurable;
    this.onExit = onExit;
    
    updateFields();
    
    InventoryShape.builder()
                  .chest(ChestType.getNearest(getDynamic().size()))
                  .name("Настройка")
                  .shape(
                     "X########",
                     "#########",
                     "#########",
                     "#########",
                     "#########",
                     "#########"
                  )
                  .ingredient('X', Ingredient.builder()
                                             .name("Назад")
                                             .type(Material.SPECTRAL_ARROW)
                                             .build())
                  .build(this);
    
    getBehavior().bind('#', ClickType.LEFT, this::interact);
    getBehavior().bind('X', ClickType.LEFT, this::back);
  }
  
  private void back(InventoryClickEvent event) {
    onExit.accept((Player) event.getWhoClicked());
  }
  
  
  private void interact(InventoryClickEvent event) {
    getDynamic(event.getSlot()).ifPresent(field -> {
      field.requireValue(configurable, (Player) event.getWhoClicked(), this);
    });
  }
  
  private void updateFields() {
    List<ConfigurableIngredient> ingredients = new ArrayList<>();
    
    for (Field field : configurable.getClass().getDeclaredFields()) {
      UIConfigurable annotation = field.getAnnotation(UIConfigurable.class);
      
      if (annotation == null || !ValueSupplier.registry.supports(field.getType())) continue;
      field.setAccessible(true);
      
      ingredients.add(new ConfigurableIngredient(configurable, field, annotation));
    }
    
    setDynamic(ingredients);
  }
}
