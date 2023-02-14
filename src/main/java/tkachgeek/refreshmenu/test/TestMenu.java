package tkachgeek.refreshmenu.test;

import org.bukkit.event.inventory.ClickType;
import tkachgeek.config.yaml.YmlConfigManager;
import tkachgeek.refreshmenu.RefreshMenu;
import tkachgeek.refreshmenu.inventory.Menu;

public class TestMenu extends Menu {
  public TestMenu() {
    setView("default", new TestPagedView());
    bind('X', ClickType.LEFT, this::save);
    
  }
  private void save() {
    YmlConfigManager manager = new YmlConfigManager(RefreshMenu.plugin);
    manager.store("test", this);
  }
}
