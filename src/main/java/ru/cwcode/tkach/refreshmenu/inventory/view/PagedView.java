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
import ru.cwcode.tkach.refreshmenu.refresh.Refreshable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PagedView<T extends Ingredient> extends View implements Refreshable {
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
    
    placeholders.add("max_page", maxPage);
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
  
  @Override
  public void refresh() {
    if (getInventory().getViewers().isEmpty()) return;
    updateRequired(player);
  }
  
  @Override
  public void updateStates() {
    super.updateStates();
    setState("hasNextPage", hasNextPage() ? "true" : "false");
    setState("hasPrevPage", hasPrevPage() ? "true" : "false");
  }
  
  @Override
  protected void updatePlaceholders() {
    placeholders.add("page", page + 1);
    placeholders.add("next_page", Math.min(maxPage, page + 2));
    placeholders.add("prev_page", Math.max(1, page));
  }
  
  protected Optional<T> getDynamic(int slot) {
    int indexAtPage = (int) shape.getJoinedShape().substring(0, slot).chars().filter(ch -> ch == dynamicChar).count();
    int indexAtDynamic = page * pageSize + indexAtPage;
    
    return indexAtDynamic >= dynamic.size() ? Optional.empty() : Optional.ofNullable(dynamic.get(indexAtDynamic));
  }
  
  
  public boolean hasNextPage() {
    return page + 1 < maxPage;
  }
  
  public void nextPage() {
    if (hasNextPage()) {
      page++;
      updateDynamicContent(player);
    }
  }
  
  public boolean hasPrevPage() {
    return page > 0;
  }
  
  public void prevPage() {
    if (hasPrevPage()) {
      page--;
      updateDynamicContent(player);
    }
  }
  
  protected void updateDynamicContent(Player player) {
    prepareForDrawing();
    drawer.drawChars(new MenuContext(this, player), Set.of(getDynamicChar(), '<', '>'));
  }
  
  @Override
  protected void initializeDrawer() {
    drawer = new PagedViewDrawer();
  }
  
  @Override
  protected boolean handleIngredientClickAction(InventoryClickEvent event, char character) {
    boolean isHandled = super.handleIngredientClickAction(event, character);
    if (isHandled) return true;
    
    int slot = event.getSlot();
    
    if (event.getView().getInventory(event.getRawSlot()) != getInventory()) {
      slot = getInventory().getSize() + (slot < 9 ? (slot + 27) : (slot - 9));
    }
    
    Ingredient clickedIngredient = character == dynamicChar ? getDynamic(slot).orElse(null) : shape.getIngredientMap().get(character);
    if (clickedIngredient == null) return false;
    
    execute(((Player) event.getWhoClicked()), () -> {
      MenuContext context = new MenuContext(this, (Player) event.getWhoClicked());
      
      clickedIngredient.onClick(context, event);
      prepareForDrawing();
      event.getView().setItem(event.getRawSlot(), clickedIngredient.getItem(context));
    });
    
    return true;
  }
}
