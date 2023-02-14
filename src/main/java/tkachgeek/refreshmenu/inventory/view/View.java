package tkachgeek.refreshmenu.inventory.view;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import tkachgeek.config.minilocale.Placeholders;
import tkachgeek.refreshmenu.ManagerRegistry;
import tkachgeek.refreshmenu.inventory.shape.InventoryShape;

public class View {
  private Inventory inventory;
  public boolean canCloseHimself = true;
  transient Behavior behavior = new Behavior();
  transient Placeholders placeholders = new Placeholders("coder", "TkachGeek");
  InventoryShape shape = InventoryShape.builder()
                                       .name("Меню не настроено vk.com/cwcode")
                                       .shape("---",
                                              "---",
                                              "---")
                                       .type(InventoryType.DISPENSER)
                                       .build();
  
  public View() {
    setInventory(ViewDrawer.shaped(shape));
  }
  
  public void onOutsideClick(InventoryClickEvent event) {
    event.setCancelled(true);
  }
  
  public void onOwnInventoryClick(InventoryClickEvent event) {
    event.setCancelled(true);
  }
  
  public void onInventoryClick(InventoryClickEvent event) {
    behavior.execute(new Behavior.ClickData(shape.charAtIndex(event.getSlot()), event.getClick()));
    event.setCancelled(true);
  }
  
  public void onInventoryClose(InventoryCloseEvent event, ManagerRegistry.HierarchyResult hierarchy) {
  
  }
  
  public void onDrag(InventoryDragEvent event) {
    event.setCancelled(true);
  }
  
  public void open(Player player) {
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
  
  public Inventory getInventory() {
    if(inventory == null) inventory = shape.createInventory();
    return inventory;
  }
  
  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }
}
