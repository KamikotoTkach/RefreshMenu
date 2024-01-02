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
import tkachgeek.refreshmenu.MenuContext;
import tkachgeek.refreshmenu.inventory.Menu;
import tkachgeek.refreshmenu.inventory.shape.InventoryShape;
import tkachgeek.refreshmenu.inventory.view.drawer.ViewDrawer;

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
  transient ViewDrawer drawer;
  transient Placeholders placeholders = new Placeholders("coder", "TkachGeek");
  transient private Inventory inventory;
  
  {
    initializeDrawer();
  }
  
  protected void initializeDrawer() {
    drawer = new ViewDrawer();
  }
  
  public View() {
  }
  
  public void onOutsideClick(InventoryClickEvent event) {
    event.setCancelled(true);
  }
  
  public void onOwnInventoryClick(InventoryClickEvent event) {
    event.setCancelled(true);
  }
  
  public void onDrag(InventoryDragEvent event) {
    event.setCancelled(true);
  }
  
  public void onInventoryClick(InventoryClickEvent event) {
    event.setCancelled(true);
    
    behavior.execute(event, new Behavior.ClickData(shape.charAtIndex(event.getSlot()), event.getClick()));
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
  
  protected void onOpen(Player player) {
  
  }
}
