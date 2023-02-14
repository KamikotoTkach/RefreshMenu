package tkachgeek.refreshmenu.test;

import tkachgeek.refreshmenu.inventory.Menu;

public class TestMenu extends Menu {
  public TestMenu() {
    setView("default", new TestPagedView());
  }
}
