package tkachgeek.refreshmenu.inventory.view;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class PagedView extends View {
  transient List<Ingredient> dynamic = new ArrayList<>();
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
  
  public List<Ingredient> getDynamic() {
    return dynamic;
  }
  
  public void setDynamic(List<Ingredient> dynamic) {
    this.dynamic = dynamic;
    this.pageSize = shape.howMany(dynamicChar);
    
    if (this.pageSize == 0) return;
    
    this.maxPage = dynamic.size() / pageSize;
    
    placeholders.add("maxPage", maxPage);
    updatePlaceholders();
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
  
  private void updatePlaceholders() {
    placeholders.add("page", page + 1);
    placeholders.add("nextPage", Math.min(maxPage, page + 2));
    placeholders.add("prevPage", Math.max(1,page));
  }
  
  private void prevPage() {
    if (page > 0) {
      page--;
      updatePlaceholders();
      updateDynamicContent();
    }
  }
  
  private void updateDynamicContent() {
    ViewDrawer.drawPage(this);
  }
  
  @Override
  protected void onOpen(Player player) {
    updateDynamicContent();
  }
  
  public int getPage() {
    return page;
  }
}
