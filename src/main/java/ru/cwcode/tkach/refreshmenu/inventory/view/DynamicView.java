package ru.cwcode.tkach.refreshmenu.inventory.view;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.view.drawer.DynamicViewDrawer;
import ru.cwcode.tkach.refreshmenu.inventory.view.drawer.ViewDrawer;
import ru.cwcode.tkach.refreshmenu.refresh.Refreshable;

import java.util.*;

public class DynamicView extends View implements Refreshable {
  Map<Character, List<? extends Ingredient>> dynamicIngredients = new HashMap<>();
  
  @Getter
  Player viewer;
  
  @Override
  public void open(Player player) {
    this.viewer = player;
    
    super.open(player);
  }
  
  public List<? extends Ingredient> getDynamicIngredients(char character) {
    return dynamicIngredients.get(character);
  }
  
  public Set<Character> getDynamicCharacters() {
    return dynamicIngredients.keySet();
  }
  
  public void setDynamic(char character, List<? extends Ingredient> ingredients) {
    dynamicIngredients.put(character, ingredients);
  }
  
  @Override
  public DynamicViewDrawer getDrawer() {
    return (DynamicViewDrawer) drawer;
  }
  
  @Override
  protected void initializeDrawer() {
    drawer = new DynamicViewDrawer();
  }
  
  public Optional<Ingredient> getDynamic(int slot) {
    String joinedShape = shape.getJoinedShape();
    char countedChar = joinedShape.charAt(slot);
    
    if (!dynamicIngredients.containsKey(countedChar)) return Optional.empty();
    
    int index = -1;
    for (int i = 0; i <= slot; i++) {
      if (joinedShape.charAt(i) == countedChar) {
        index++;
      }
    }
    
    if (index == -1) return Optional.empty();
    
    List<? extends Ingredient> ingredients = getDynamicIngredients(countedChar);
    if (ingredients.size() <= index) return Optional.empty();
    
    return Optional.ofNullable(ingredients.get(index));
  }
  
  @Override
  public void refresh() {
    if (getInventory().getViewers().isEmpty()) return;
    updateRequired(viewer);
  }
  
  @Override
  protected boolean handleIngredientClickAction(InventoryClickEvent event, char character) {
    boolean isHandled = super.handleIngredientClickAction(event, character);
    if (isHandled) return true;
    
    int slot = event.getSlot();
    
    if (event.getView().getInventory(event.getRawSlot()) != getInventory()) {
      slot = getInventory().getSize() + (slot < 9 ? (slot + 27) : (slot - 9));
    }
    
    int finalSlot = slot;
    
    List<? extends Ingredient> dynamic = dynamicIngredients.get(character);
    
    Ingredient clickedIngredient = dynamic != null ? getDynamic(slot).orElse(null) : shape.getIngredientMap().get(character);
    if (clickedIngredient == null) return false;
    
    execute(((Player) event.getWhoClicked()), () -> {
      MenuContext context = new MenuContext(this, (Player) event.getWhoClicked());
      
      clickedIngredient.onClick(context, event);
      
      ItemStack updatedItem = getDynamic(finalSlot).map(x -> x.getItem(context)).orElse(ViewDrawer.AIR);
      event.getView().setItem(event.getSlot(), updatedItem);
    });
    
    return true;
  }
}
