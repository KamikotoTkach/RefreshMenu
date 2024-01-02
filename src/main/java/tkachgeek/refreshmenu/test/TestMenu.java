package tkachgeek.refreshmenu.test;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import tkachgeek.refreshmenu.configurationUI.UIConfigurableView;
import tkachgeek.refreshmenu.inventory.Menu;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;
import tkachgeek.refreshmenu.inventory.shape.InventoryShape;
import tkachgeek.refreshmenu.inventory.view.View;

public class TestMenu extends Menu {
  public TestMenu() {
    setView("default", new NavigateView());
    setView("config", new UIConfigurableView(TestConfig.getInstance()));
    
    bind('S', ClickType.LEFT, this::save);
  }
  
  private void save() {
  }
  
  private static class NavigateView extends View {
    public NavigateView() {
      InventoryShape.builder()
                    .chest(27)
                    .shape("#########",
                           "##C###S##",
                           "#########")
                    
                    .ingredient('C', Ingredient.builder()
                                               .type(Material.ARROW)
                                               .name("Конфигурабле")
                                               .build())
                    
                    .ingredient('S', Ingredient.builder()
                                               .type(Material.ARROW)
                                               .name("Сохранить")
                                               .build())
                    
                    .build(this);
      
      getBehavior().bind('C', ClickType.LEFT, this::configurable);
    }
    
    private void configurable(InventoryClickEvent event) {
      getMenu().openView((Player) event.getWhoClicked(), "config");
    }
  }
}
