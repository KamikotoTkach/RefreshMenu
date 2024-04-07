package ru.cwcode.tkach.refreshmenu.inventory.view;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import ru.cwcode.tkach.config.relocate.com.fasterxml.jackson.annotation.JsonSubTypes;
import ru.cwcode.tkach.config.relocate.com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.cwcode.tkach.locale.Placeholder;
import ru.cwcode.tkach.locale.Placeholders;
import ru.cwcode.tkach.locale.platform.MiniLocale;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.Menu;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.shape.InventoryShape;
import ru.cwcode.tkach.refreshmenu.inventory.view.drawer.AbstractDrawer;
import ru.cwcode.tkach.refreshmenu.inventory.view.drawer.ViewDrawer;
import ru.cwcode.tkach.refreshmenu.protocol.PacketListener;

@JsonTypeInfo(
   use = JsonTypeInfo.Id.NAME,
   property = "type")
@JsonSubTypes({
   @JsonSubTypes.Type(value = View.class, name = "View"),
})
public class View implements InventoryHolder {
  public boolean canCloseHimself = true;
  protected InventoryShape shape = InventoryShape.builder()
                                                 .name("Не настроено, vk.com/cwcode")
                                                 .shape("-")
                                                 .type(InventoryType.HOPPER)
                                                 .build();
  protected transient Menu menu = null;
  protected transient Behavior behavior = new Behavior();
  protected transient AbstractDrawer drawer;
  protected transient Placeholders placeholders = Placeholder.add("coder", "TkachGeek");
  transient private Inventory inventory;
  
  {
    initializeDrawer();
  }
  
  public View() {
  }
  
  public AbstractDrawer getDrawer() {
    return drawer;
  }
  
  public void onOutsideClick(InventoryClickEvent event) {
    event.setCancelled(true);
  }
  
  public void onOwnInventoryClick(InventoryClickEvent event) {
    event.setCancelled(true);
    int playerInvSlot = event.getSlot();
    
    shape.findCharAtIndex(inventory.getSize() + (playerInvSlot < 9 ? playerInvSlot + 27 : (playerInvSlot - 9))).ifPresent(character -> {
      handleIngredientClickAction(event, character);
    });
  }
  
  public void onDrag(InventoryDragEvent event) {
    event.setCancelled(true);
  }
  
  public void onInventoryClick(InventoryClickEvent event) {
    event.setCancelled(true);
    
    shape.findCharAtIndex(event.getSlot()).ifPresent(character -> {
      handleIngredientClickAction(event, character);
      
      behavior.execute(event, new Behavior.ClickData(character, event.getClick()));
    });
  }
  
  public void onInventoryClose(InventoryCloseEvent event) {
  }
  
  public void open(Player player) {
    drawInventory(player);
    player.openInventory(getInventory());
    onOpen(player);
  }
  
  public void drawInventory(Player player) {
    drawer.draw(new MenuContext(this, player));
  }
  
  public void updateInventoryTitle(Player player) {
    PacketListener.setInventoryTitle(player, MiniLocale.getInstance().miniMessageWrapper().deserialize(shape.getName(), getPlaceholders()));
  }
  
  public Behavior getBehavior() {
    return behavior;
  }
  
  public InventoryShape getShape() {
    return shape;
  }
  
  public void setShape(InventoryShape shape) {
    this.shape = shape;
  }
  
  /**
   * @return empty inventory with proper size and title
   */
  @Override
  public @NotNull Inventory getInventory() {
    if (inventory == null) inventory = shape.createInventory(this);
    return inventory;
  }
  
  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }
  
  public Placeholders getPlaceholders() {
    return placeholders;
  }
  
  public Menu getMenu() {
    return menu;
  }
  
  public void setMenu(Menu menu) {
    this.menu = menu;
  }
  
  protected void initializeDrawer() {
    drawer = new ViewDrawer();
  }
  
  protected void handleIngredientClickAction(InventoryClickEvent event, char character) {
    Ingredient clickedIngredient = shape.getIngredientMap().get(character);
    
    if (clickedIngredient != null) {
      clickedIngredient.onClick(new MenuContext(this, (Player) event.getWhoClicked()), event.getClick());
    }
  }
  
  protected void onOpen(Player player) {
  
  }
}
