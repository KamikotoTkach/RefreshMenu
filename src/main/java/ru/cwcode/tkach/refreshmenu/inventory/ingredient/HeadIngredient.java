package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.cwcode.cwutils.items.ItemBuilder;
import ru.cwcode.cwutils.items.ItemBuilderFactory;
import ru.cwcode.tkach.locale.Placeholders;
import ru.cwcode.tkach.refreshmenu.Utils;

import java.util.List;
import java.util.UUID;

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
  public ItemStack getItem(Placeholders placeholders) {
    ItemBuilder item = ItemBuilderFactory.of(Material.PLAYER_HEAD);
    
    if (name != null) item.name(Utils.deserialize(name, placeholders).decoration(TextDecoration.ITALIC, false));
    if (description != null) item.description(Utils.deserialize(description, placeholders).stream()
                                                   .map(x -> x.decoration(TextDecoration.ITALIC, false))
                                                   .toList());
    if (amount != 0) item.amount(amount);
    
    if (texture != null) {
      PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), "");
      profile.setProperty(new ProfileProperty("textures", texture));
      item.playerProfile(profile);
    }
    
    return item.build();
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setDescription(List<String> description) {
    this.description = description;
  }
  
  public void setAmount(int amount) {
    this.amount = amount;
  }
  
  public void setTexture(String texture) {
    this.texture = texture;
  }
  
  public String getName() {
    return name;
  }
  
  public List<String> getDescription() {
    return description;
  }
  
  public int getAmount() {
    return amount;
  }
  
  public String getTexture() {
    return texture;
  }
}
