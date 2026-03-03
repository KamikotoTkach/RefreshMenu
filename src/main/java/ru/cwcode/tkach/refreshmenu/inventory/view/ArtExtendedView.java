package ru.cwcode.tkach.refreshmenu.inventory.view;

import lombok.Getter;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.ArtIngredient;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.view.drawer.ArtExtendedViewDrawer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
    setArts(List.of(arts));
  }
  
  public void bindArt(ArtIngredient art, ClickType clickType, Runnable runnable) {
    this.getBehavior().bind(art.getChar(), clickType, runnable);
  }
  
  public void bindArt(ArtIngredient art, ClickType clickType, Consumer<InventoryClickEvent> consumer) {
    this.getBehavior().bind(art.getChar(), clickType, consumer);
  }
  
  public void bindArts(ClickType clickType, Runnable runnable) {
    for (ArtIngredient art : this.arts.values()) {
      bindArt(art, clickType, runnable);
    }
  }
  
  public void bindArts(ClickType clickType, Consumer<InventoryClickEvent> consumer) {
    for (ArtIngredient art : this.arts.values()) {
      bindArt(art, clickType, consumer);
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
  public Optional<Ingredient> getIngredient(char character, int slot) {
    Supplier<Optional<Ingredient>> def = () -> super.getIngredient(character, slot);
    boolean isArt = arts.containsKey(character);
    
    return isArt ? def.get().or(() -> getArt(slot)) : def.get();
  }
}
