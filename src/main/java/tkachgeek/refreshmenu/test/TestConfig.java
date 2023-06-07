package tkachgeek.refreshmenu.test;

import org.bukkit.Material;
import tkachgeek.refreshmenu.configurationUI.Configurable;
import tkachgeek.refreshmenu.configurationUI.UIConfigurable;

public class TestConfig implements Configurable {
  @UIConfigurable(name = "Инт", material = Material.DIAMOND)
  int intval = 1;
  @UIConfigurable
  double aDouble = 1.1;
  @UIConfigurable
  boolean bool = true;
  @UIConfigurable
  String string = "asd";
  @UIConfigurable
  Material material = Material.STONE;
  static TestConfig instance;
  private TestConfig(){}
  public static TestConfig getInstance() {
    if(instance == null) instance = new TestConfig();
    return instance;
  }
}
