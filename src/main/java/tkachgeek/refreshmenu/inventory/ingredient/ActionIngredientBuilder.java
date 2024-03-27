package tkachgeek.refreshmenu.inventory.ingredient;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import tkachgeek.config.minilocale.wrapper.adventure.MiniMessageWrapper;
import tkachgeek.config.yaml.data.ItemData;
import tkachgeek.refreshmenu.inventory.ingredient.action.Action;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

public class ActionIngredientBuilder {
  private EnumMap<ClickType, Action> actions = new EnumMap<>(ClickType.class);
  private String name = "";
  private List<String> description;
  private int amount = 1;
  private Material type = Material.STONE;
  private int customModelData = 0;
  
  
  public ActionIngredientBuilder fromItemData(ItemData itemData) {
    type = itemData.getMaterial();
    name = itemData.getName().serialize();
    description = itemData.getDescription().toList();
    customModelData = itemData.getCustomModelData();
    return this;
  }
  
  public ActionIngredientBuilder name(String name) {
    this.name = name;
    return this;
  }
  
  public ActionIngredientBuilder name(Component name) {
    this.name = MiniMessageWrapper.serialize(name);
    return this;
  }
  
  public ActionIngredientBuilder description(List<String> description) {
    this.description = description;
    return this;
  }
  
  public ActionIngredientBuilder description(String... description) {
    this.description = List.of(description);
    return this;
  }
  
  public ActionIngredientBuilder description(String description) {
    this.description = Collections.singletonList(description);
    return this;
  }
  
  public ActionIngredientBuilder descriptionFromComponent(List<Component> description) {
    this.description = MiniMessageWrapper.serialize(description);
    return this;
  }
  
  public ActionIngredientBuilder descriptionFromComponent(Component... description) {
    this.description = MiniMessageWrapper.serialize(description);
    return this;
  }
  
  public ActionIngredientBuilder descriptionFromComponent(Component description) {
    this.description = Collections.singletonList(MiniMessageWrapper.serialize(description));
    return this;
  }
  
  public ActionIngredientBuilder amount(int amount) {
    this.amount = amount;
    return this;
  }
  
  public ActionIngredientBuilder type(Material type) {
    this.type = type;
    return this;
  }
  
  public ActionIngredientBuilder customModelData(int data) {
    this.customModelData = data;
    return this;
  }
  
  public ActionIngredient build() {
    return new ActionIngredient(name, description, amount, type, customModelData, actions);
  }
}
