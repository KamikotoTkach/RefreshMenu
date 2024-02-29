package tkachgeek.refreshmenu.inventory.view;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import tkachgeek.refreshmenu.MenuContext;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;
import tkachgeek.refreshmenu.inventory.view.drawer.PagedViewDrawer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PagedView<T extends Ingredient> extends View {
  protected transient List<T> dynamic = new ArrayList<>();
  protected transient int page = 0;
  protected transient int maxPage = 0;
  protected transient int pageSize = 0;
  protected char dynamicChar = '#';
  protected transient Player player;
  
  {
    behavior.bind('<', ClickType.LEFT, this::prevPage);
    behavior.bind('>', ClickType.LEFT, this::nextPage);
  }
  
  public PagedView() {
  }
  
  public List<T> getDynamic() {
    return dynamic;
  }
  
  public void setDynamic(List<T> dynamic) {
    this.dynamic = dynamic;
    this.pageSize = shape.howMany(dynamicChar);
    
    if (this.pageSize == 0) return;
    
    this.maxPage = dynamic.size() / pageSize + (dynamic.size() % pageSize != 0 ? 1 : 0);
    
    placeholders.add("maxPage", maxPage);
    updatePlaceholders();
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
  
  public char getDynamicChar() {
    return dynamicChar;
  }
  
  protected void setDynamicChar(char dynamicIngredient) {
    this.dynamicChar = dynamicIngredient;
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
  protected void onOpen(Player player) {
    this.player = player;
  }
  
  public int getPage() {
    return page;
  }
  
  @Override
  protected void initializeDrawer() {
    drawer = new PagedViewDrawer();
  }
  
  public Player getPlayer() {
    return player;
  }
  
  public boolean hasViewers() {
    return !getInventory().getViewers().isEmpty();
  }
  
  public void updateRequired(Player player) {
    ((PagedViewDrawer) drawer).updateRequired(new MenuContext(this, player));
  }
}
