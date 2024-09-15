package ru.cwcode.tkach.refreshmenu.inventory.shape;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import ru.cwcode.tkach.refreshmenu.Utils;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.type.InventoryTypeHolder;
import ru.cwcode.tkach.refreshmenu.inventory.view.View;

import java.util.HashMap;
import java.util.Optional;

public class InventoryShape {
   @Setter
   @Getter
   InventoryTypeHolder type;
   @Setter
   @Getter
   String name;
   @Getter
   String[] shape;
   @Getter
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
      //<editor-fold desc="defaultPagedShape">
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
      //</editor-fold>
   }
   
   public static ShapeBuilder defaultArtExtendedShape() {
      //<editor-fold desc="defaultArtExtendedShape">
      return InventoryShape.builder()
                           .name("Меню")
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
                                                      .build());
      //</editor-fold>
   }
   
   public static ShapeBuilder defaultMultiShapeArtExtendedShape() {
      //<editor-fold desc="defaultArtExtendedShape">
      return InventoryShape.builder()
                           .name("Меню")
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
                                  "#######{}")

                           .ingredient('{', Ingredient.builder()
                                                      .type(Material.SPECTRAL_ARROW)
                                                      .name("На <prevShape> страницу")
                                                      .description("<shape>/<shapes>")
                                                      .build())

                           .ingredient('}', Ingredient.builder()
                                                      .type(Material.SPECTRAL_ARROW)
                                                      .name("На <nextShape> страницу")
                                                      .description("<shape>/<shapes>")
                                                      .build());
      //</editor-fold>
   }
   
  
  public Inventory createInventory(View view) {
    return type.createInventory(view, Utils.deserialize(getName(), view.getPlaceholders()));
   }
   
   /**
    * @throws IndexOutOfBoundsException  if the {@code index}
    * argument is negative or not less than the length of this
    * shape.<br>
    * Use the {@link #findCharAtIndex(int) findCharAtIndex} instead
    */
   @Deprecated
   public char charAtIndex(int index) {
      return getJoinedShape().charAt(index);
   }
   
   public Optional<Character> findCharAtIndex(int index) {
      if (index < 0 || index >= getJoinedShape().length()) {
         return Optional.empty();
      }
      
      return Optional.of(getJoinedShape().charAt(index));
   }

   public int howMany(char toCount) {
      int count = 0;
      for (char c : getJoinedShape().toCharArray()) {
         if (c == toCount) count++;
      }
      return count;
   }
  
  public void setShape(String... shape) {
      this.shape = shape;
      this.joinedShape = null;
   }
  
  public String getJoinedShape() {
      if (joinedShape == null) joinedShape = String.join("", getShape());
      return joinedShape;
   }

   @Override
   public InventoryShape clone() {
      return new InventoryShape(
            this.getName(),
            this.getShape().clone(),
            this.getType(),
            new HashMap<>(this.getIngredientMap())
      );
   }
}
