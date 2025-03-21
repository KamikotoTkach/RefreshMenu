package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.cwcode.cwutils.items.ItemBuilder;
import ru.cwcode.cwutils.items.ItemBuilderFactory;
import ru.cwcode.tkach.locale.Placeholders;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.Utils;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class HeadIngredient implements Ingredient {
  String name;
  List<String> description;
  int amount;
  String texture;
  
  public HeadIngredient() {
  }
  
  public HeadIngredient(String name, List<String> description, int amount, String texture) {
    this.name = name;
    this.description = description;
    this.amount = amount;
    this.texture = texture;
  }
  
  @Override
  public ItemStack getItem(MenuContext context) {
    ItemBuilder item = ItemBuilderFactory.of(Material.PLAYER_HEAD);
    
    if (name != null) item.name(Utils.deserialize(name, context.view().getPlaceholders(), context.player(), true));
    if (description != null) item.description(Utils.deserialize(description, context.view().getPlaceholders(), context.player(), true));
    if (amount != 0) item.amount(amount);
    
    if (texture != null) {
      PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), "");
      profile.setProperty(new ProfileProperty("textures", texture));
      item.playerProfile(profile);
    }
    
    return item.build();
  }
  
  @Override
  public ItemStack getItem(Placeholders placeholders) {
    ItemBuilder item = ItemBuilderFactory.of(Material.PLAYER_HEAD);
    
    if (name != null) item.name(Utils.deserialize(name, placeholders, null, true));
    if (description != null) item.description(Utils.deserialize(description, placeholders, null, true));
    if (amount != 0) item.amount(amount);
    
    if (texture != null) {
      PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), "");
      profile.setProperty(new ProfileProperty("textures", texture));
      item.playerProfile(profile);
    }
    
    return item.build();
  }
}
