package tkachgeek.refreshmenu.inventory.view;

import org.bukkit.entity.Player;
import tkachgeek.refreshmenu.inventory.ingredient.ArtIngredient;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;
import tkachgeek.refreshmenu.inventory.shape.InventoryShape;

import java.util.ArrayList;
import java.util.List;

public class MultiShapeArtExtendedView<T extends Ingredient, ART extends ArtIngredient> extends ArtExtendedView<T, ART> {
  List<InventoryShape> shapes = new ArrayList<>();
  int shapePointer = 0;
  
  public void nextShape(Player player) {
    if (shapePointer + 1 < shapes.size()) {
      shapePointer++;
      
      shape = shapes.get(shapePointer);
      
      drawInventory(player);
    }
  }
  
  public void prevShape(Player player) {
    if (shapePointer > 0) {
      shapePointer--;
      
      shape = shapes.get(shapePointer);
      
      drawInventory(player);
    }
  }
  
  public List<InventoryShape> getShapes() {
    return shapes;
  }
  
  public int getShapePointer() {
    return shapePointer;
  }
  
  public void setShapes(List<InventoryShape> shapes) {
    this.shapes = shapes;
  }
}
