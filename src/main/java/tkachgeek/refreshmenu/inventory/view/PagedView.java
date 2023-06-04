package tkachgeek.refreshmenu.inventory.view;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PagedView<T extends Ingredient> extends View {
  transient List<T> dynamic = new ArrayList<>();
  transient int page = 0;
  transient int maxPage = 0;
  transient int pageSize = 0;
  char dynamicChar = '#';
  
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
    
    this.maxPage = dynamic.size() / pageSize + 1;
    
    placeholders.add("maxPage", maxPage);
    updatePlaceholders();
  }
  
  private void updatePlaceholders() {
    placeholders.add("page", page + 1);
    placeholders.add("nextPage", Math.min(maxPage, page + 2));
    placeholders.add("prevPage", Math.max(1, page));
  }
  
  public Optional<T> getDynamic(int slot) {
    int index = page * pageSize + slot;
    return index >= dynamic.size() ? Optional.empty() : Optional.ofNullable(dynamic.get(index));
  }
  
  public char getDynamicChar() {
    return dynamicChar;
  }
  
  public void setDynamicChar(char dynamicIngredient) {
    this.dynamicChar = dynamicIngredient;
  }
  
  private void nextPage() {
    if (page + 1 < maxPage) {
      page++;
      updatePlaceholders();
      updateDynamicContent();
    }
  }
  
  private void updateDynamicContent() {
    ViewDrawer.drawPage(this);
  }
  
  private void prevPage() {
    if (page > 0) {
      page--;
      updatePlaceholders();
      updateDynamicContent();
    }
  }
  
  @Override
  protected void onOpen(Player player) {
    updateDynamicContent();
  }
  
  public int getPage() {
    return page;
  }
}
