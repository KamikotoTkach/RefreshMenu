package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.cwcode.cwutils.items.ItemBuilder;
import ru.cwcode.cwutils.items.ItemBuilderFactory;
import ru.cwcode.tkach.locale.Placeholders;
import ru.cwcode.tkach.refreshmenu.Utils;

import java.util.List;

public class IngredientImpl implements Ingredient {
  String name;
  List<String> description;
  int amount;
  Material type;
  int customModelData;
  
  public IngredientImpl(String name, List<String> description, int amount, Material type, int customModelData) {
    this.name = name;
    this.description = description;
    this.amount = amount;
    this.type = type;
    this.customModelData = customModelData;
  }
  
  public IngredientImpl() {
  }
  
  @Override
  public ItemStack getItem(Placeholders placeholders) {
    ItemBuilder item = ItemBuilderFactory.of(type);
    
    if (name != null) item.name(Utils.deserialize(name, placeholders).decoration(TextDecoration.ITALIC, false));
    if (description != null) item.description(Utils.deserialize(description, placeholders).stream()
                                                   .map(x -> x.decoration(TextDecoration.ITALIC, false))
                                                   .toList());
    if (amount != 0) item.amount(amount);
    if (customModelData != 0) item.customModelData(customModelData);
    
    return item.build();
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public List<String> getDescription() {
    return description;
  }
  
  public void setDescription(List<String> description) {
    this.description = description;
  }
  
  public int getAmount() {
    return amount;
  }
  
  public void setAmount(int amount) {
    this.amount = amount;
  }
  
  public Material getType() {
    return type;
  }
  
  public void setType(Material type) {
    this.type = type;
  }
  
  public int getCustomModelData() {
    return customModelData;
  }
  
  public void setCustomModelData(int customModelData) {
    this.customModelData = customModelData;
  }
}
