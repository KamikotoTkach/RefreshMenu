package tkachgeek.refreshmenu.inventory.view;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import tkachgeek.refreshmenu.inventory.ingredient.Ingredient;
import tkachgeek.refreshmenu.inventory.ingredient.ItemIngredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PagedView extends View {
  transient List<Ingredient> dynamic = new ArrayList<>();
  transient int page = 0;
  transient int maxPage = 0;
  transient int pageSize = 0;
  char dynamicChar = '#';
  
  public PagedView() {
    behavior.bind('<', ClickType.LEFT, this::prevPage);
    behavior.bind('>', ClickType.LEFT, this::nextPage);
    setDynamic(Arrays.stream(Material.values()).filter(Material::isFuel).map(x -> new ItemIngredient(new ItemStack(x))).collect(Collectors.toList()));
  }
  
  public List<Ingredient> getDynamic() {
    return dynamic;
  }
  
  public void setDynamic(List<Ingredient> dynamic) {
    this.dynamic = dynamic;
    this.pageSize = shape.howMany(dynamicChar);
    
    if (this.pageSize == 0) return;
    
    this.maxPage = dynamic.size() / pageSize;
  }
  
  public char getDynamicChar() {
    return dynamicChar;
  }
  
  public void setDynamicChar(char dynamicIngredient) {
    this.dynamicChar = dynamicIngredient;
  }
  
  private void nextPage() {
    if (page < maxPage) {
      page++;
      updateDynamicContent();
    }
  }
  
  private void prevPage() {
    if (page > 0) {
      page--;
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
