package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import ru.cwcode.tkach.locale.paper.data.ItemData;
import ru.cwcode.tkach.locale.platform.MiniLocale;

import java.util.Collections;
import java.util.List;

public class IngredientBuilder {
  private String name = "";
  private List<String> description;
  private int amount = 1;
  private Material type = Material.STONE;
  private int customModelData = 0;
  
  public IngredientBuilder fromItemData(ItemData itemData) {
    type = itemData.getMaterial();
    name = itemData.getName().serialize();
    description = itemData.getDescription().toList();
    customModelData = itemData.getCustomModelData();
    return this;
  }
  
  public IngredientBuilder name(String name) {
    this.name = name;
    return this;
  }
  
  public IngredientBuilder name(Component name) {
    this.name = MiniLocale.getInstance().miniMessageWrapper().serialize(name);
    return this;
  }
  
  public IngredientBuilder description(List<String> description) {
    this.description = description;
    return this;
  }
  
  public IngredientBuilder description(String... description) {
    this.description = List.of(description);
    return this;
  }
  
  public IngredientBuilder description(String description) {
    this.description = Collections.singletonList(description);
    return this;
  }
  
  public IngredientBuilder descriptionFromComponent(List<Component> description) {
    this.description = MiniLocale.getInstance().miniMessageWrapper().serialize(description);
    return this;
  }
  
  public IngredientBuilder descriptionFromComponent(Component... description) {
    this.description = MiniLocale.getInstance().miniMessageWrapper().serialize(description);
    return this;
  }
  
  public IngredientBuilder descriptionFromComponent(Component description) {
    this.description = Collections.singletonList(MiniLocale.getInstance().miniMessageWrapper().serialize(description));
    return this;
  }
  
  public IngredientBuilder amount(int amount) {
    this.amount = amount;
    return this;
  }
  
  public IngredientBuilder type(Material type) {
    this.type = type;
    return this;
  }
  
  public IngredientBuilder customModelData(int data) {
    this.customModelData = data;
    return this;
  }
  
  public IngredientImpl build() {
    return new IngredientImpl(name, description, amount, type, customModelData);
  }
}