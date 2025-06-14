package ru.cwcode.tkach.refreshmenu.inventory.view;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cwcode.cwutils.messages.MessageReturn;
import ru.cwcode.cwutils.messages.TargetableMessageReturn;
import ru.cwcode.tkach.config.relocate.com.fasterxml.jackson.annotation.JsonSubTypes;
import ru.cwcode.tkach.config.relocate.com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.cwcode.tkach.locale.Placeholder;
import ru.cwcode.tkach.locale.Placeholders;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.Utils;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.shape.InventoryShape;
import ru.cwcode.tkach.refreshmenu.inventory.view.drawer.AbstractDrawer;
import ru.cwcode.tkach.refreshmenu.inventory.view.drawer.ViewDrawer;
import ru.cwcode.tkach.refreshmenu.protocol.OpenedWindowService;

import java.util.HashMap;

@JsonTypeInfo(
   use = JsonTypeInfo.Id.NAME,
   property = "type")
@JsonSubTypes({
   @JsonSubTypes.Type(value = View.class, name = "View"),
})
public class View extends AbstractView {
  @Getter
  @Setter
  protected InventoryShape shape = InventoryShape.builder()
                                                 .name("Не настроено, vk.com/cwcode")
                                                 .shape("-")
                                                 .type(InventoryType.HOPPER)
                                                 .build();
  
  @Getter
  protected transient Behavior behavior = new Behavior();
  protected transient AbstractDrawer drawer;
  @Getter
  protected transient Placeholders placeholders = Placeholder.add("coder", "TkachGeek");
  protected transient HashMap<String, String> states = new HashMap<>();
  
  {
    initializeDrawer();
  }
  
  @Override
  public void onInventoryClick(InventoryClickEvent event) {
    super.onInventoryClick(event);
    
    shape.findCharAtIndex(event.getSlot()).ifPresent(character -> {
      handleIngredientClickAction(event, character);
      
      execute(((Player) event.getWhoClicked()), () -> {
        behavior.execute(event, new Behavior.ClickData(character, event.getClick()));
      });
    });
  }
  
  @Override
  public void onOwnInventoryClick(InventoryClickEvent event) {
    super.onOwnInventoryClick(event);
    
    int playerInvSlot = event.getSlot();
    
    shape.findCharAtIndex(inventory.getSize() + (playerInvSlot < 9 ? playerInvSlot + 27 : (playerInvSlot - 9))).ifPresent(character -> {
      handleIngredientClickAction(event, character);
    });
  }
  
  public ViewDrawer getDrawer() {
    return (ViewDrawer) drawer;
  }
  
  public void open(Player player) {
    drawInventory(player);
    super.open(player);
  }
  
  public void drawInventory(Player player) {
    drawer.draw(new MenuContext(this, player));
  }
  
  public void updateInventoryTitle(Player player) {
    OpenedWindowService.setInventoryTitle(player, Utils.deserialize(shape.getName(), getPlaceholders(), player, false));
  }
  
  public void setState(String state, String value) {
    states.put(state, value);
  }
  
  public @Nullable String getState(String state) {
    return states.get(state);
  }
  
  public void updateRequired(Player player) {
    drawer.updateRequired(new MenuContext(this, player));
  }
  
  protected void initializeDrawer() {
    drawer = new ViewDrawer();
  }
  
  protected boolean handleIngredientClickAction(InventoryClickEvent event, char character) {
    Ingredient clickedIngredient = shape.getIngredientMap().get(character);
    if (clickedIngredient == null) return false;
    
    execute((Player) event.getWhoClicked(), () -> {
      clickedIngredient.onClick(new MenuContext(this, (Player) event.getWhoClicked()), event);
    });
    
    return true;
  }
  
  protected void execute(Player player, Runnable runnable) {
    try {
      runnable.run();
    } catch (Exception e) {
      handleException(e, player);
    }
  }
  
  protected void handleException(Exception exception, Player player) {
    if (exception instanceof MessageReturn messageReturn) {
      player.sendMessage(messageReturn.getMessage());
    } else if (exception instanceof TargetableMessageReturn targetableMessageReturn) {
      player.sendMessage(targetableMessageReturn.getMessage(player));
    } else {
      player.sendMessage(exception.getLocalizedMessage());
      exception.printStackTrace();
    }
  }
  
  @Override
  public @NotNull Inventory getInventory() {
    if (inventory == null) {
      inventory = shape.createInventory(this);
    }
    
    return inventory;
  }
}
