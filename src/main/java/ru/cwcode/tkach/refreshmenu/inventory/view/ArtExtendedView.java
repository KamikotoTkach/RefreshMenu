package ru.cwcode.tkach.refreshmenu.inventory.view;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.ArtIngredient;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.view.drawer.ArtExtendedViewDrawer;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

@Getter
public class ArtExtendedView<T extends Ingredient, ART extends ArtIngredient> extends ExtendedView<T> {
  private final HashMap<Character, ART> arts = new HashMap<>();
  
  public void setArts(Collection<ART> arts) {
    this.arts.clear();
    for (ART art : arts) {
      this.arts.put(art.getChar(), art);
    }
  }
  
  public void setArts(ART... arts) {
    setArts(Arrays.asList(arts));
  }
  
  public void bindArts(ClickType clickType, Runnable runnable) {
    for (ArtIngredient art : this.arts.values()) {
      this.getBehavior().bind(art.getChar(), clickType, runnable);
    }
  }
  
  public void bindArts(ClickType clickType, Consumer<InventoryClickEvent> consumer) {
    for (ArtIngredient art : this.arts.values()) {
      this.getBehavior().bind(art.getChar(), clickType, consumer);
    }
  }
  
  protected Optional<ART> getArt(int slot) {
    return shape.findCharAtIndex(slot).map(this.arts::get);
  }
  
  @Override
  protected void initializeDrawer() {
    drawer = new ArtExtendedViewDrawer();
  }
  
  @Override
  public ArtExtendedViewDrawer getDrawer() {
    return (ArtExtendedViewDrawer) drawer;
  }
  
  @Override
  protected boolean handleIngredientClickAction(InventoryClickEvent event, char character) {
    boolean isHandled = super.handleIngredientClickAction(event, character);
    if (isHandled) return true;
    
    int slot = event.getSlot();
    
    if (event.getView().getInventory(event.getRawSlot()) != getInventory()) {
      slot = getInventory().getSize() + (slot < 9 ? (slot + 27) : (slot - 9));
    }
    
    Ingredient clickedIngredient = arts.containsKey(character) ? getArt(slot).orElse(null) : shape.getIngredientMap().get(character);
    if (clickedIngredient == null) return false;
    
    execute(((Player) event.getWhoClicked()), () -> {
      MenuContext context = new MenuContext(this, (Player) event.getWhoClicked());
      
      clickedIngredient.onClick(context, event);
      
      ItemStack itemStack = event.getView().getItem(event.getRawSlot());
      if (itemStack == null || itemStack.getType() == Material.AIR) return;
      
      event.getView().setItem(event.getRawSlot(), clickedIngredient.getItem(context));
    });
    
    return true;
  }
}
