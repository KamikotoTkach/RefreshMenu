package ru.cwcode.tkach.refreshmenu.inventory.view;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.ArtIngredient;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.view.drawer.ArtExtendedViewDrawer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

public class ArtExtendedView<T extends Ingredient, ART extends ArtIngredient> extends ExtendedView<T> {
  private final HashMap<Character, ART> arts = new HashMap<>();
  
  public void setArts(Collection<ART> arts) {
    this.arts.clear();
    for (ART art : arts) {
      this.arts.put(art.getChar(), art);
    }
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
  
  public HashMap<Character, ART> getArts() {
    return arts;
  }
  
  @Override
  protected void initializeDrawer() {
    drawer = new ArtExtendedViewDrawer();
  }
  
  protected Optional<ART> getArt(int slot) {
    return shape.findCharAtIndex(slot).map(this.arts::get);
  }
}
