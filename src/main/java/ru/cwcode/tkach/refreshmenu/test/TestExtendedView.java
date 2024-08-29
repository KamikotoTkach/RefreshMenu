package ru.cwcode.tkach.refreshmenu.test;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import ru.cwcode.cwutils.items.ItemBuilderFactory;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.ArtIngredient;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.ItemIngredient;
import ru.cwcode.tkach.refreshmenu.inventory.shape.InventoryShape;
import ru.cwcode.tkach.refreshmenu.inventory.view.ArtExtendedView;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TestExtendedView extends ArtExtendedView<Ingredient, ArtIngredient> {
  {
    InventoryShape.builder()
                  .chest(54)
                  .shape("#########",
                         "#########",
                         "#########",
                         "#########",
                         "#########",
                         "#########",
                         
                         "#########",
                         "#########",
                         "#########",
                         "#######<>")
                  
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
                  
                  .build(this);
  }
  
  public TestExtendedView() {
    AtomicInteger i = new AtomicInteger(0);
    
    setDynamic(Arrays.stream(Material.values())
                     .filter(Material::isFuel)
                     .map(x -> new ItemIngredient(ItemBuilderFactory.of(x)
                                                                    .name(Component.text(i.getAndIncrement()))
                                                                    .build()))
                     .collect(Collectors.toList())
    );
  }
}
