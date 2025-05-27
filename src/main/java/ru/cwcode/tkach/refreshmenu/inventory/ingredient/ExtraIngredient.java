package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.extra.Extra;

import java.util.ArrayList;
import java.util.List;

public class ExtraIngredient extends IngredientImpl {
  List<Extra> extras = new ArrayList<>();
  
  public ExtraIngredient() {
  }
  
  public ExtraIngredient(String name, List<String> description, int amount, Material type, int customModelData, List<Extra> extras) {
    super(name, description, amount, type, customModelData);
    this.extras = extras;
  }
  
  @Override
  public void onClick(MenuContext context, InventoryClickEvent event) {
    extras.stream()
          .filter(x -> x.isHandlingOnClick(this, context, event))
          .findFirst()
          .ifPresentOrElse(x -> x.onClick(this, context, event),
                           () -> super.onClick(context, event));
  }
  
  @Override
  public boolean shouldRefresh(MenuContext context) {
    return extras.stream()
                 .filter(x -> x.isHandlingShouldRefresh(this, context))
                 .findFirst()
                 .map(x -> x.shouldRefresh(this, context))
                 .orElseGet(() -> super.shouldRefresh(context));
  }
  
  @Override
  public ItemStack getItem(MenuContext context) {
    return extras.stream()
                 .filter(x -> x.isHandlingGetItem(this, context))
                 .findFirst()
                 .map(x -> x.getItem(this, context))
                 .orElseGet(() -> super.getItem(context));
  }
}
