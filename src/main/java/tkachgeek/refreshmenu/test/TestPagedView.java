package tkachgeek.refreshmenu.test;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;
import tkachgeek.refreshmenu.inventory.ingredient.ItemIngredient;
import tkachgeek.refreshmenu.inventory.shape.InventoryShape;
import tkachgeek.refreshmenu.inventory.view.PagedView;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TestPagedView extends PagedView<Ingredient> {
  {
    InventoryShape.builder()
                  .chest(27)
                  .shape("#########",
                         "#########",
                         "X######<>")
    
                  .ingredient('<', Ingredient.builder()
                                             .type(Material.ARROW)
                                             .name("На <prevPage> страницу")
                                             .description("<page>/<maxPage>")
                                             .build())
    
                  .ingredient('>', Ingredient.builder()
                                             .type(Material.ARROW)
                                             .name("На <nextPage> страницу")
                                             .description("<page>/<maxPage>")
                                             .build())
    
                  .ingredient('X', Ingredient.builder()
                                             .type(Material.ARROW)
                                             .name("Сохранить")
                                             .build())
    
                  .build(this);
  }
  
  public TestPagedView() {
    setDynamic(Arrays.stream(Material.values())
                     .filter(Material::isFuel)
                     .map(x -> new ItemIngredient(new ItemStack(x)))
                     .collect(Collectors.toList())
    );
  }
}
