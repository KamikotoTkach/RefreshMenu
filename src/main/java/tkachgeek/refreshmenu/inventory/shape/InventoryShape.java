package tkachgeek.refreshmenu.inventory.shape;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import tkachgeek.config.minilocale.wrapper.adventure.MiniMessageWrapper;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;
import tkachgeek.refreshmenu.inventory.type.InventoryTypeHolder;
import tkachgeek.refreshmenu.inventory.view.View;
import tkachgeek.refreshmenu.inventory.view.ViewDrawer;

import java.util.HashMap;

public class InventoryShape {
   InventoryTypeHolder type;
   String name;
   String[] shape;
   HashMap<Character, Ingredient> ingredientMap = new HashMap<>();
   transient String joinedShape = null;

   public InventoryShape(String name, String[] shape, InventoryTypeHolder type, HashMap<Character, Ingredient> ingredients) {
      this.name = name;
      this.shape = shape;
      this.type = type;
      this.ingredientMap = ingredients;
   }

   public InventoryShape() {
   }

   public static ShapeBuilder builder() {
      return new ShapeBuilder();
   }

   public static ShapeBuilder defaultPagedShape() {
      return InventoryShape.builder()
                           .name("Меню")
                           .chest(54)
                           .shape("#########",
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
                                                      .build());
   }

   public InventoryTypeHolder getType() {
      return type;
   }

   public Inventory createInventory(View view) {
      Inventory inventory = type.createInventory(view, MiniMessageWrapper.deserialize(getName(), view.getPlaceholders()));
      ViewDrawer.fillInventory(view, inventory);
      return inventory;
   }

   public char charAtIndex(int index) {
      return getJoinedShape().charAt(index);
   }

   public int howMany(char toCount) {
      int count = 0;
      for (String line : getShape()) {
         for (char c : line.toCharArray()) {
            if (c == toCount) count++;
         }
      }
      return count;
   }

   public void setType(InventoryTypeHolder type) {
      this.type = type;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String[] getShape() {
      return shape;
   }

   public void setShape(String... shape) {
      this.shape = shape;
      this.joinedShape = null;
   }

   public HashMap<Character, Ingredient> getIngredientMap() {
      return ingredientMap;
   }

   public String getJoinedShape() {
      if (joinedShape == null) joinedShape = String.join("", getShape());
      return joinedShape;
   }
}
