package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.cwcode.cwutils.items.ItemBuilder;
import ru.cwcode.cwutils.items.ItemBuilderFactory;
import ru.cwcode.tkach.locale.Placeholders;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.Utils;

import java.util.List;

@Setter
@Getter
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
  public ItemStack getItem(MenuContext context) {
    ItemBuilder item = ItemBuilderFactory.of(type);
    
    if (name != null) item.name(Utils.deserialize(name, context.view().getPlaceholders(), context.player(), true));
    if (description != null) item.description(Utils.deserialize(description, context.view().getPlaceholders(), context.player(), true));
    if (amount != 0) item.amount(amount);
    if (customModelData != 0) item.customModelData(customModelData);
    
    return item.build();
  }
  
  @Override
  public ItemStack getItem(Placeholders placeholders) {
    ItemBuilder item = ItemBuilderFactory.of(type);
    
    if (name != null) item.name(Utils.deserialize(name, placeholders, null, true));
    if (description != null) item.description(Utils.deserialize(description, placeholders, null, true));
    if (amount != 0) item.amount(amount);
    if (customModelData != 0) item.customModelData(customModelData);
    
    return item.build();
  }
}
