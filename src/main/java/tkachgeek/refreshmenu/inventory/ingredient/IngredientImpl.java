package tkachgeek.refreshmenu.inventory.ingredient;

import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tkachgeek.config.minilocale.Placeholders;
import tkachgeek.config.minilocale.wrapper.MiniMessageWrapper;
import tkachgeek.tkachutils.items.ItemBuilder;
import tkachgeek.tkachutils.items.ItemBuilderFactory;

import java.util.List;

public class IngredientImpl implements Ingredient {
  String name;
  List<String> description;
  int amount;
  Material type;
  
  public IngredientImpl(String name, List<String> description, int amount, Material type) {
    this.name = name;
    this.description = description;
    this.amount = amount;
    this.type = type;
  }
  
  public IngredientImpl() {
  }
  
  @Override
  public ItemStack getItem(Placeholders placeholders) {
    ItemBuilder item = ItemBuilderFactory.of(type);
    
    if (name != null) item.name(MiniMessageWrapper.deserialize(name, placeholders).decoration(TextDecoration.ITALIC, false));
    if (description != null) item.description(MiniMessageWrapper.deserialize(description, placeholders, true));
    if (amount != 0) item.amount(amount);
    
    return item.build();
  }
}
