package tkachgeek.refreshmenu;

import org.bukkit.inventory.Inventory;
import tkachgeek.refreshmenu.inventory.Menu;
import tkachgeek.refreshmenu.inventory.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManagerRegistry {
  static List<MenuManager> managerList = new ArrayList<>();
  
  public static MenuManager register(MenuManager manager) {
    managerList.add(manager);
    return manager;
  }
  
  public static void unregister(MenuManager manager) {
    managerList.remove(manager);
  }
  
  public static Optional<View> getViewForInventory(Inventory inventory) {
    for (MenuManager menuManager : managerList) {
      for (Menu activeMenu : menuManager.activeMenu) {
        for (View view : activeMenu.views.values()) {
          if (view.getInventory().equals(inventory)) {
            return Optional.of(view);
          }
        }
      }
    }
    return Optional.empty();
  }
  
  public static Optional<HierarchyResult> getAllForInventory(Inventory inventory) {
    for (MenuManager menuManager : managerList) {
      for (Menu activeMenu : menuManager.activeMenu) {
        for (View view : activeMenu.views.values()) {
          if (view.getInventory().equals(inventory)) {
            return Optional.of(new HierarchyResult(menuManager, activeMenu, view));
          }
        }
      }
    }
    return Optional.empty();
  }
  
  public static class HierarchyResult {
    MenuManager manager;
    Menu menu;
    View view;
  
    public HierarchyResult(MenuManager manager, Menu menu, View view) {
      this.manager = manager;
      this.menu = menu;
      this.view = view;
    }
  }
}
