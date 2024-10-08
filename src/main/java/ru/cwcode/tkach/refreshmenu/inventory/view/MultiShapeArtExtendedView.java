package ru.cwcode.tkach.refreshmenu.inventory.view;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.ArtIngredient;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.shape.InventoryShape;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MultiShapeArtExtendedView<T extends Ingredient, ART extends ArtIngredient> extends ArtExtendedView<T, ART> {
  List<InventoryShape> shapes = new ArrayList<>();
  int shapePointer = 0;
  
  {
    getBehavior().bind('{', ClickType.LEFT, event -> prevShape((Player) event.getWhoClicked()));
    getBehavior().bind('}', ClickType.LEFT, event -> nextShape((Player) event.getWhoClicked()));
  }
  public void nextShape(Player player) {
    if (shapePointer + 1 < shapes.size()) {
      shapePointer++;
      
      shape = shapes.get(shapePointer);
      
      updatePlaceholders();
      drawInventory(player);
      updateInventoryTitle(player);
    }
  }
  
  public void prevShape(Player player) {
    if (shapePointer > 0) {
      shapePointer--;
      
      shape = shapes.get(shapePointer);
      
      updatePlaceholders();
      drawInventory(player);
      updateInventoryTitle(player);
    }
  }
  
  @Override
  protected void updatePlaceholders() {
    super.updatePlaceholders();
    
    placeholders.add("shape", shapePointer + 1);
    placeholders.add("shapes", shapes.size());
    placeholders.add("nextShape", Math.min(shapes.size(), shapePointer + 2));
    placeholders.add("prevShape", Math.max(1, shapePointer));
  }
  
  public void setShapes(List<InventoryShape> shapes) {
    this.shapes = shapes;
    this.shape = shapes.get(0);
    
    updatePlaceholders();
  }
}
