package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import ru.cwcode.tkach.locale.paper.data.ItemData;
import ru.cwcode.tkach.locale.platform.MiniLocale;

import java.util.Collections;
import java.util.List;

public class IngredientBuilder
  <I extends IngredientImpl, B extends IngredientBuilder<I, B>>
  extends AbstractIngredientBuilder<I, B> {
  
  protected String name = "";
  protected List<String> description;
  protected int amount = 1;
  protected Material type = Material.STONE;
  protected int customModelData = 0;
  
  public B fromItemData(ItemData itemData) {
    type = itemData.getMaterial();
    name = itemData.getName().serialize();
    description = itemData.getDescription().toList();
    customModelData = itemData.getCustomModelData();
    return getThis();
  }
  
  public B name(String name) {
    this.name = name;
    return getThis();
  }
  
  public B name(Component name) {
    this.name = MiniLocale.getInstance().miniMessageWrapper().serialize(name);
    return getThis();
  }
  
  public B description(List<String> description) {
    this.description = description;
    return getThis();
  }
  
  public B description(String... description) {
    this.description = List.of(description);
    return getThis();
  }
  
  public B description(String description) {
    this.description = Collections.singletonList(description);
    return getThis();
  }
  
  public B descriptionFromComponent(List<Component> description) {
    this.description = MiniLocale.getInstance().miniMessageWrapper().serialize(description);
    return getThis();
  }
  
  public B descriptionFromComponent(Component... description) {
    this.description = MiniLocale.getInstance().miniMessageWrapper().serialize(description);
    return getThis();
  }
  
  public B descriptionFromComponent(Component description) {
    this.description = Collections.singletonList(MiniLocale.getInstance().miniMessageWrapper().serialize(description));
    return getThis();
  }
  
  public B amount(int amount) {
    this.amount = amount;
    return getThis();
  }
  
  public B type(Material type) {
    this.type = type;
    return getThis();
  }
  
  public B customModelData(int data) {
    this.customModelData = data;
    return getThis();
  }
  
  public I build() {
    return (I) new IngredientImpl(name, description, amount, type, customModelData);
  }
}