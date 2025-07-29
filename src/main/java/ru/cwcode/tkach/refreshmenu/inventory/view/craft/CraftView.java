package ru.cwcode.tkach.refreshmenu.inventory.view.craft;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.view.View;
import ru.cwcode.tkach.refreshmenu.refresh.Refreshable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CraftView extends View implements Refreshable {
  List<CraftViewPage> pages = new ArrayList<>(List.of(new CraftViewPage()));
  int page = 0;
  
  {
    behavior.bind('<', ClickType.LEFT, x -> prevPage(((Player) x.getWhoClicked())));
    behavior.bind('>', ClickType.LEFT, x -> nextPage(((Player) x.getWhoClicked())));
  }
  
  @Override
  public void open(Player player) {
    updatePlaceholders();
    super.open(player);
  }
  
  @Override
  protected void initializeDrawer() {
    drawer = new CraftDrawer();
  }
  
  protected void updatePlaceholders() {
    placeholders.add("page", page + 1);
    placeholders.add("next_page", Math.min(pages.size(), page + 2));
    placeholders.add("prev_page", Math.max(1, page));
    placeholders.add("max_page", pages.size());
  }
  
  public CraftViewPage getCurrentPage() {
    return pages.get(page);
  }
  
  /**
   * @return an instance of page if page exists, if page == pages.size() creates a new page
   * @throws IllegalArgumentException if page < 0 or page > pages.size()
   */
  public CraftViewPage getPage(int page) {
    if (page < 0 || page > pages.size()) throw new IllegalArgumentException("No such page");
    
    if (page == pages.size()) {
      CraftViewPage newPage = new CraftViewPage();
      pages.add(newPage);
      return newPage;
    }
    
    return pages.get(page);
  }
  
  @Override
  public void onInventoryClick(InventoryClickEvent event) {
    super.onInventoryClick(event);
    
    Ingredient ingredient = getCurrentPage().getIngredients().get(event.getSlot());
    if (ingredient != null) {
      MenuContext context = new MenuContext(this, ((Player) event.getWhoClicked()));
      
      ingredient.onClick(context, event);
      
      ItemStack item = ingredient.getItem(context);
      if (item != null) getInventory().setItem(event.getSlot(), item);
    }
  }
  
  @Override
  public void refresh() {
    getInventory().getViewers().stream().findFirst().ifPresent(humanEntity -> {
      updateRequired(((Player) humanEntity));
    });
  }
  
  protected void nextPage(Player player) {
    if (page + 1 < pages.size()) {
      page++;
      updatePlaceholders();
      updateDynamicContent(player);
    }
  }
  
  private void updateDynamicContent(Player player) {
    drawer.drawChars(new MenuContext(this, player), Set.of('#', '<', '>'));
  }
  
  protected void prevPage(Player player) {
    if (page > 0) {
      page--;
      updatePlaceholders();
      updateDynamicContent(player);
    }
  }
}
