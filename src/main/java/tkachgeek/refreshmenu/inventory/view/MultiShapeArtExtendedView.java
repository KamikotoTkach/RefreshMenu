package tkachgeek.refreshmenu.inventory.view;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import tkachgeek.refreshmenu.inventory.ingredient.ArtIngredient;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;
import tkachgeek.refreshmenu.inventory.shape.InventoryShape;

import java.util.ArrayList;
import java.util.List;

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
  
  public List<InventoryShape> getShapes() {
    return shapes;
  }
  
  public int getShapePointer() {
    return shapePointer;
  }
  
  public void setShapes(List<InventoryShape> shapes) {
    this.shapes = shapes;
    this.shape = shapes.get(0);
    
    updatePlaceholders();
  }
}
