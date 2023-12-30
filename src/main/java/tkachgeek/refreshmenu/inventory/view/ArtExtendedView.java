package tkachgeek.refreshmenu.inventory.view;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import tkachgeek.refreshmenu.inventory.ingredient.ArtIngredient;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

public class ArtExtendedView<T extends Ingredient, ART extends ArtIngredient> extends ExtendedView<T> {
  private final HashMap<Character, ART> arts = new HashMap<>();

  @Override
  protected void updateDynamicContent(Player player) {
    ViewDrawer.drawArtExtendedPage(this, player);
  }

  public void setArts(Collection<ART> arts) {
    this.arts.clear();
    for (ART art : arts) {
      this.arts.put(art.getChar(), art);
    }
  }

  protected Optional<ART> getArt(int slot) {
    char artChar = shape.charAtIndex(slot);

    return Optional.ofNullable(this.arts.get(artChar));
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
}
