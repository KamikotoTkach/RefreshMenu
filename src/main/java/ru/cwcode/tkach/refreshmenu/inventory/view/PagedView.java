package ru.cwcode.tkach.refreshmenu.inventory.view;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.view.drawer.PagedViewDrawer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PagedView<T extends Ingredient> extends View {
  @Getter
  protected transient List<T> dynamic = new ArrayList<>();
  @Getter
  protected transient int page = 0;
  protected transient int maxPage = 0;
  protected transient int pageSize = 0;
  
  @Getter
  @Setter(AccessLevel.PROTECTED)
  protected char dynamicChar = '#';
  
  @Getter()
  @Setter(AccessLevel.PROTECTED)
  protected Player player;
  
  {
    behavior.bind('<', ClickType.LEFT, this::prevPage);
    behavior.bind('>', ClickType.LEFT, this::nextPage);
  }
  
  @Override
  public PagedViewDrawer getDrawer() {
    return (PagedViewDrawer) drawer;
  }
  
  public void setDynamic(List<T> dynamic) {
    this.dynamic = dynamic;
    this.pageSize = shape.howMany(dynamicChar);
    
    if (this.pageSize == 0) return;
    
    this.maxPage = dynamic.size() / pageSize + (dynamic.size() % pageSize != 0 ? 1 : 0);
    
    placeholders.add("maxPage", maxPage);
    updatePlaceholders();
  }
  
  @Override
  public void open(Player player) {
    this.player = player;
    super.open(player);
  }
  
  public boolean hasViewers() {
    return !getInventory().getViewers().isEmpty();
  }
  
  public void updateRequired(Player player) {
    super.updateRequired(player);
  }
  
  protected void updatePlaceholders() {
    placeholders.add("page", page + 1);
    placeholders.add("nextPage", Math.min(maxPage, page + 2));
    placeholders.add("prevPage", Math.max(1, page));
  }
  
  protected Optional<T> getDynamic(int slot) {
    int indexAtPage = (int) shape.getJoinedShape().substring(0, slot).chars().filter(ch -> ch == dynamicChar).count();
    int indexAtDynamic = page * pageSize + indexAtPage;
    
    return indexAtDynamic >= dynamic.size() ? Optional.empty() : Optional.ofNullable(dynamic.get(indexAtDynamic));
  }
  
  protected void nextPage() {
    if (page + 1 < maxPage) {
      page++;
      updatePlaceholders();
      updateDynamicContent(player);
    }
  }
  
  protected void updateDynamicContent(Player player) {
    drawer.drawChars(new MenuContext(this, player), Set.of(getDynamicChar(), '<', '>'));
  }
  
  protected void prevPage() {
    if (page > 0) {
      page--;
      updatePlaceholders();
      updateDynamicContent(player);
    }
  }
  
  @Override
  protected void initializeDrawer() {
    drawer = new PagedViewDrawer();
  }
  
  @Override
  protected void handleIngredientClickAction(InventoryClickEvent event, char character) {
    int slot = event.getSlot();
    
    if (event.getView().getInventory(event.getRawSlot()) != getInventory()) {
      slot = getInventory().getSize() + (slot < 9 ? (slot + 27) : (slot - 9));
    }
    
    Ingredient clickedIngredient = character == dynamicChar ? getDynamic(slot).orElse(null) : shape.getIngredientMap().get(character);
    
    if (clickedIngredient != null) {
      execute(((Player) event.getWhoClicked()), () -> {
        clickedIngredient.onClick(new MenuContext(this, (Player) event.getWhoClicked()), event.getClick());
      });
    }
  }
}
