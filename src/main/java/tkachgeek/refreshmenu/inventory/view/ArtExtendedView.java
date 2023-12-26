package tkachgeek.refreshmenu.inventory.view;

import org.bukkit.entity.Player;
import tkachgeek.refreshmenu.inventory.ingredient.ArtIngredient;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;

import java.util.Collection;
import java.util.HashMap;

public class ArtExtendedView<T extends Ingredient> extends ExtendedView<T> {
  private final HashMap<Character, ArtIngredient> arts = new HashMap<>();

  @Override
  protected void updateDynamicContent(Player player) {
    ViewDrawer.drawArtExtendedPage(this, player);
  }

  public void setArts(Collection<ArtIngredient> arts) {
    this.arts.clear();
    for (ArtIngredient art : arts) {
      this.arts.put(art.getChar(), art);
    }
  }

  public HashMap<Character, ArtIngredient> getArts() {
    return arts;
  }
}
