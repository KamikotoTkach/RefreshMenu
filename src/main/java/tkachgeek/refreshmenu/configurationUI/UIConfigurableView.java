package tkachgeek.refreshmenu.configurationUI;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import tkachgeek.refreshmenu.configurationUI.valueSupplier.ValueSupplier;
import tkachgeek.refreshmenu.inventory.shape.InventoryShape;
import tkachgeek.refreshmenu.inventory.type.ChestType;
import tkachgeek.refreshmenu.inventory.view.PagedView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UIConfigurableView extends PagedView<ConfigurableIngredient> {
  Configurable configurable;
  public UIConfigurableView(Configurable configurable) {
    this.configurable = configurable;
    
    updateFields();
    
    InventoryShape.builder()
                  .chest(ChestType.getNearest(getDynamic().size()))
                  .name("Настройка")
                  .shape(
                     "#########",
                     "#########",
                     "#########",
                     "#########",
                     "#########",
                     "#########"
                  ).build(this);
    
    getBehavior().bind('#', ClickType.LEFT, this::interact);
  }
  
  private void interact(InventoryClickEvent event) {
    Optional<ConfigurableIngredient> field = getDynamic(event.getSlot());
    if (field.isEmpty()) return;
    
    field.get().requireValue(configurable, (Player) event.getWhoClicked(), this);
  }
  
  @Override
  protected void onOpen(Player player) {
    updateFields();
    
    super.onOpen(player);
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
