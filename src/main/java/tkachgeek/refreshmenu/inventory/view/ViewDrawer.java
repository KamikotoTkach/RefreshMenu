package tkachgeek.refreshmenu.inventory.view;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tkachgeek.refreshmenu.MenuContext;
import tkachgeek.refreshmenu.inventory.ingredient.ArtIngredient;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;
import tkachgeek.tkachutils.numbers.NumbersUtils;
import tkachgeek.tkachutils.protocol.Packet;

import java.util.HashMap;

public class ViewDrawer {
  public static void drawPage(PagedView<? extends Ingredient> view, Player player) {
    int pageSize = view.getShape().howMany(view.getDynamicChar());
    int dynamicItemIndex = view.getPage() * pageSize;
    String joinedShape = view.getShape().getJoinedShape();
    HashMap<Character, Ingredient> ingredientMap = view.getShape().getIngredientMap();
    MenuContext context = new MenuContext(view, player);

    Inventory inventory = view.getInventory();
    inventory.clear();

    char currShapeChar;
    for (int i = 0; i < NumbersUtils.notGreater(joinedShape.length(), inventory.getSize()); i++) {
      currShapeChar = joinedShape.charAt(i);

      if (ingredientMap.containsKey(currShapeChar)) {
        inventory.setItem(i, ingredientMap.get(currShapeChar).getItem(context));
      } else if (view.getDynamicChar() == currShapeChar && dynamicItemIndex < view.getDynamic().size()) {
        inventory.setItem(i, view.getDynamic().get(dynamicItemIndex++).getItem(context));
      }
    }
  }

  public static void drawExtendedPage(ExtendedView<? extends Ingredient> view, Player player) {
    int pageSize = view.getShape().howMany(view.getDynamicChar());
    int dynamicItemIndex = view.getPage() * pageSize;
    String joinedShape = view.getShape().getJoinedShape();
    HashMap<Character, Ingredient> ingredientMap = view.getShape().getIngredientMap();
    MenuContext context = new MenuContext(view, player);

    Inventory inventory = view.getInventory();
    inventory.clear();

    char currShapeChar;
    for (int i = 0; i < NumbersUtils.notGreater(joinedShape.length(), inventory.getSize() + 36); i++) {
      currShapeChar = joinedShape.charAt(i);

      ItemStack item;
      if (ingredientMap.containsKey(currShapeChar)) {
        item = ingredientMap.get(currShapeChar).getItem(context);
      } else if (view.getDynamicChar() == currShapeChar && dynamicItemIndex < view.getDynamic().size()) {
        item = view.getDynamic().get(dynamicItemIndex++).getItem(context);
      } else {
        continue;
      }

      if (i < inventory.getSize()) {
        inventory.setItem(i, item);
      } else {
        int slot = i - inventory.getSize();
        slot = slot >= 27 ? slot - 27 : slot + 9;
        Packet.setSlot(player, slot, item);
      }
    }
  }

  public static void drawArtExtendedPage(ArtExtendedView<? extends Ingredient> view, Player player) {
    int pageSize = view.getShape().howMany(view.getDynamicChar());
    int dynamicItemIndex = view.getPage() * pageSize;
    String joinedShape = view.getShape().getJoinedShape();
    HashMap<Character, Ingredient> ingredientMap = view.getShape().getIngredientMap();
    HashMap<Character, Integer> artDraws = new HashMap<>();
    MenuContext context = new MenuContext(view, player);

    Inventory inventory = view.getInventory();
    inventory.clear();

    char currShapeChar;
    for (int i = 0; i < NumbersUtils.notGreater(joinedShape.length(), inventory.getSize() + 36); i++) {
      currShapeChar = joinedShape.charAt(i);

      ItemStack item;
      if (view.getArts().containsKey(currShapeChar)) {
        ArtIngredient art = view.getArts().get(currShapeChar);
        int draws = artDraws.getOrDefault(currShapeChar, 0);
        if (draws > art.getMaxDraws()) continue;

        artDraws.put(currShapeChar, draws + 1);
        item = view.getArts().get(currShapeChar).getItem(context);
      } else if (ingredientMap.containsKey(currShapeChar)) {
        item = ingredientMap.get(currShapeChar).getItem(context);
      } else if (view.getDynamicChar() == currShapeChar && dynamicItemIndex < view.getDynamic().size()) {
        item = view.getDynamic().get(dynamicItemIndex++).getItem(context);
      } else {
        continue;
      }

      if (i < inventory.getSize()) {
        inventory.setItem(i, item);
      } else {
        int slot = i - inventory.getSize();
        slot = slot >= 27 ? slot - 27 : slot + 9;
        Packet.setSlot(player, slot, item);
      }
    }
  }

  public static Inventory createFilledInventory(View view) {
    Inventory inventory = view.getShape().createInventory(view);
    fillInventory(view, inventory);
    return inventory;
  }

  public static void fillInventory(View view, Inventory inventory) {
    HashMap<Character, Ingredient> ingredientMap = view.getShape().getIngredientMap();
    String joinedShape = view.getShape().getJoinedShape();

    char currShapeChar;
    for (int i = 0; i < NumbersUtils.notGreater(joinedShape.length(), inventory.getSize()); i++) {
      currShapeChar = joinedShape.charAt(i);

      if (ingredientMap.containsKey(currShapeChar)) {
        inventory.setItem(i, ingredientMap.get(currShapeChar).getItem(view.placeholders)); //todo: возможно стоит тоже тут контекстом делать
      }
    }
  }

  public static void redrawIngredient(View view, char character, Player player) {
    String joinedShape = view.getShape().getJoinedShape();
    HashMap<Character, Ingredient> ingredientMap = view.getShape().getIngredientMap();
    Inventory inventory = view.getInventory();
    MenuContext context = new MenuContext(view, player);

    char currShapeChar;
    for (int i = 0; i < NumbersUtils.notGreater(joinedShape.length(), inventory.getSize()); i++) {
      currShapeChar = joinedShape.charAt(i);

      if (ingredientMap.containsKey(currShapeChar)) {
        if (currShapeChar == character) {
          inventory.setItem(i, ingredientMap.get(currShapeChar).getItem(context));
        }
      } else {
        inventory.setItem(i, null);
      }
    }
  }
}
