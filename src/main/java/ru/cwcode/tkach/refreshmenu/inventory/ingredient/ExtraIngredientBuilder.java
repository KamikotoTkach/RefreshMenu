package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import ru.cwcode.tkach.locale.paper.data.ItemData;
import ru.cwcode.tkach.locale.platform.MiniLocale;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.action.Action;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.extra.Extra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

public class ExtraIngredientBuilder {
  private List<Extra> extras = new ArrayList<>();
  private String name = "";
  private List<String> description;
  private int amount = 1;
  private Material type = Material.STONE;
  private int customModelData = 0;
  
  
  public ExtraIngredientBuilder fromItemData(ItemData itemData) {
    type = itemData.getMaterial();
    name = itemData.getName().serialize();
    description = itemData.getDescription().toList();
    customModelData = itemData.getCustomModelData();
    return this;
  }
  
  public ExtraIngredientBuilder name(String name) {
    this.name = name;
    return this;
  }
  
  public ExtraIngredientBuilder name(Component name) {
    this.name = MiniLocale.getInstance().miniMessageWrapper().serialize(name);
    return this;
  }
  
  public ExtraIngredientBuilder description(List<String> description) {
    this.description = description;
    return this;
  }
  
  public ExtraIngredientBuilder description(String... description) {
    this.description = List.of(description);
    return this;
  }
  
  public ExtraIngredientBuilder description(String description) {
    this.description = Collections.singletonList(description);
    return this;
  }
  
  public ExtraIngredientBuilder descriptionFromComponent(List<Component> description) {
    this.description = MiniLocale.getInstance().miniMessageWrapper().serialize(description);
    return this;
  }
  
  public ExtraIngredientBuilder descriptionFromComponent(Component... description) {
    this.description = MiniLocale.getInstance().miniMessageWrapper().serialize(description);
    return this;
  }
  
  public ExtraIngredientBuilder descriptionFromComponent(Component description) {
    this.description = Collections.singletonList(MiniLocale.getInstance().miniMessageWrapper().serialize(description));
    return this;
  }
  
  public ExtraIngredientBuilder amount(int amount) {
    this.amount = amount;
    return this;
  }
  
  public ExtraIngredientBuilder type(Material type) {
    this.type = type;
    return this;
  }
  
  public ExtraIngredientBuilder customModelData(int data) {
    this.customModelData = data;
    return this;
  }
  
  public ExtraIngredientBuilder extra(Extra extra) {
    this.extras.add(extra);
    return this;
  }
  
  public ExtraIngredient build() {
    return new ExtraIngredient(name, description, amount, type, customModelData, extras);
  }
}
