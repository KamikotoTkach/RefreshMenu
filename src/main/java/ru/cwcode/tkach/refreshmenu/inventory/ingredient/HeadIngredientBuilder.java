package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import net.kyori.adventure.text.Component;
import ru.cwcode.tkach.locale.platform.MiniLocale;

import java.util.Collections;
import java.util.List;

public class HeadIngredientBuilder {
  private String name = "";
  private List<String> description;
  private int amount = 1;
  private String texture;
  
  public HeadIngredientBuilder name(String name) {
    this.name = name;
    return this;
  }
  
  public HeadIngredientBuilder texture(String texture) {
    this.texture = texture;
    return this;
  }
  
  public HeadIngredientBuilder name(Component name) {
    this.name = MiniLocale.getInstance().miniMessageWrapper().serialize(name);
    return this;
  }
  
  public HeadIngredientBuilder description(List<String> description) {
    this.description = description;
    return this;
  }
  
  public HeadIngredientBuilder description(String... description) {
    this.description = List.of(description);
    return this;
  }
  
  public HeadIngredientBuilder description(String description) {
    this.description = Collections.singletonList(description);
    return this;
  }
  
  public HeadIngredientBuilder descriptionFromComponent(List<Component> description) {
    this.description = MiniLocale.getInstance().miniMessageWrapper().serialize(description);
    return this;
  }
  
  public HeadIngredientBuilder descriptionFromComponent(Component... description) {
    this.description = MiniLocale.getInstance().miniMessageWrapper().serialize(description);
    return this;
  }
  
  public HeadIngredientBuilder descriptionFromComponent(Component description) {
    this.description = Collections.singletonList(MiniLocale.getInstance().miniMessageWrapper().serialize(description));
    return this;
  }
  
  public HeadIngredientBuilder amount(int amount) {
    this.amount = amount;
    return this;
  }
  
  public HeadIngredient build() {
    return new HeadIngredient(name, description, amount, texture);
  }
}
