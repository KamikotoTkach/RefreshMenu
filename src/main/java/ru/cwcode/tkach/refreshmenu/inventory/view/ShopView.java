package ru.cwcode.tkach.refreshmenu.inventory.view;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;

import java.util.Optional;

public abstract class ShopView<T extends Ingredient> extends PagedView<T> {
  protected T comboItem = null;
  protected int combo = 0;
  
  {
    getBehavior().bind(this.getDynamicChar(), ClickType.LEFT, this::operateOne);
    getBehavior().bind(this.getDynamicChar(), ClickType.RIGHT, this::operateMultiple);
    getBehavior().bind(this.getDynamicChar(), ClickType.SHIFT_RIGHT, this::operateAll);
  }
  
  protected abstract boolean makeOperation(Player player, T item, boolean silent);
  
  protected abstract boolean preValidateOperation(Player player, T item);
  
  protected abstract void operationFeedback(Player player, T item, int operations);
  
  private boolean preValidateOperation(InventoryClickEvent event) {
    T item = getClickedIngredient(event).orElse(null);
    if (item == null) return false;
    
    return preValidateOperation((Player) event.getWhoClicked(), item);
  }
  
  private void operateOne(InventoryClickEvent event) {
    combo = 0;
    comboItem = null;
    
    T item = getClickedIngredient(event).orElse(null);
    if (item == null) return;
    
    if (makeOperation((Player) event.getWhoClicked(), item, false)) {
      sendFeedback(event, 1);
    }
  }
  
  private void operateMultiple(InventoryClickEvent event) {
    if (!preValidateOperation(event)) return;
    
    T item = getClickedIngredient(event).orElse(null);
    if (item == null) return;
    
    if (comboItem == item) {
      combo++;
    } else {
      comboItem = item;
      combo = 0;
    }
    
    int operations = (int) Math.pow(2, combo + 1);
    int successOperation = 0;
    
    while (operations-- > 0 && makeOperation(((Player) event.getWhoClicked()), item, true)) {
      successOperation++;
    }
    
    sendFeedback(event, successOperation);
  }
  
  private void operateAll(InventoryClickEvent event) {
    if (!preValidateOperation(event)) return;
    
    combo = 0;
    int successOperation = 0;
    
    T item = getClickedIngredient(event).orElse(null);
    if (item == null) return;
    
    while (makeOperation(((Player) event.getWhoClicked()), item, true)) {
      successOperation++;
    }
    
    sendFeedback(event, successOperation);
  }
  
  private void sendFeedback(InventoryClickEvent event, int successOperation) {
    T item = getClickedIngredient(event).orElse(null);
    if (item == null) return;
    
    operationFeedback((Player) event.getWhoClicked(), item, successOperation);
  }
  
  @NotNull
  private Optional<T> getClickedIngredient(InventoryClickEvent event) {
    return this.getDynamic(event.getSlot());
  }
}