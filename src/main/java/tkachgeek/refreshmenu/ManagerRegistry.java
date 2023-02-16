package tkachgeek.refreshmenu;

import java.util.ArrayList;
import java.util.List;

public class ManagerRegistry {
  static List<MenuManager> managerList = new ArrayList<>();
  
  public static MenuManager register(MenuManager manager) {
    managerList.add(manager);
    return manager;
  }
  
  public static void unregister(MenuManager manager) {
    managerList.remove(manager);
  }
}
