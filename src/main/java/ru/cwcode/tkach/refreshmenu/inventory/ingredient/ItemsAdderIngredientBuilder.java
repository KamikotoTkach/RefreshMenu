package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import net.kyori.adventure.text.Component;
import ru.cwcode.tkach.locale.platform.MiniLocale;

import java.util.Collections;
import java.util.List;

public class ItemsAdderIngredientBuilder extends AbstractIngredientBuilder<ItemsAdderIngredient, ItemsAdderIngredientBuilder> {
  
  protected String name = "";
  protected List<String> description;
  protected int amount = 1;
  protected String type = "EMPTY";
  
  public ItemsAdderIngredientBuilder name(String name) {
    this.name = name;
    return getThis();
  }
  
  public ItemsAdderIngredientBuilder name(Component name) {
    this.name = MiniLocale.getInstance().miniMessageWrapper().serialize(name);
    return getThis();
  }
  
  public ItemsAdderIngredientBuilder description(List<String> description) {
    this.description = description;
    return getThis();
  }
  
  public ItemsAdderIngredientBuilder description(String... description) {
    this.description = List.of(description);
    return getThis();
  }
  
  public ItemsAdderIngredientBuilder description(String description) {
    this.description = Collections.singletonList(description);
    return getThis();
  }
  
  public ItemsAdderIngredientBuilder descriptionFromComponent(List<Component> description) {
    this.description = MiniLocale.getInstance().miniMessageWrapper().serialize(description);
    return getThis();
  }
  
  public ItemsAdderIngredientBuilder descriptionFromComponent(Component... description) {
    this.description = MiniLocale.getInstance().miniMessageWrapper().serialize(description);
    return getThis();
  }
  
  public ItemsAdderIngredientBuilder descriptionFromComponent(Component description) {
    this.description = Collections.singletonList(MiniLocale.getInstance().miniMessageWrapper().serialize(description));
    return getThis();
  }
  
  public ItemsAdderIngredientBuilder amount(int amount) {
    this.amount = amount;
    return getThis();
  }
  
  public ItemsAdderIngredientBuilder type(String type) {
    this.type = type;
    return getThis();
  }
  
  public ItemsAdderIngredient build() {
    return new ItemsAdderIngredient(name, description, amount, type);
  }
}
