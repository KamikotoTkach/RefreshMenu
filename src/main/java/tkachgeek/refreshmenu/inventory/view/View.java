package tkachgeek.refreshmenu.inventory.view;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import tkachgeek.config.minilocale.Placeholders;
import tkachgeek.refreshmenu.inventory.Menu;
import tkachgeek.refreshmenu.inventory.shape.InventoryShape;

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
  transient Behavior behavior = new Behavior();
  transient Placeholders placeholders = new Placeholders("coder", "TkachGeek");
  transient private Inventory inventory;
  
  public View() {
  }
  
  public void onOutsideClick(InventoryClickEvent event) {
    event.setCancelled(true);
  }
  
  public void onOwnInventoryClick(InventoryClickEvent event) {
    event.setCancelled(true);
  }
  
  public void onInventoryClick(InventoryClickEvent event) {
    behavior.execute(event, new Behavior.ClickData(shape.charAtIndex(event.getSlot()), event.getClick()));
    event.setCancelled(true);
  }
  
  public void onInventoryClose(InventoryCloseEvent event) {
  
  }
  
  public void onDrag(InventoryDragEvent event) {
    event.setCancelled(true);
  }
  
  public void open(Player player) {
    getInventory();
    player.openInventory(getInventory());
    onOpen(player);
  }
  
  protected void onOpen(Player player) {
  
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
}
