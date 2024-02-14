package tkachgeek.refreshmenu.inventory.ingredient;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tkachgeek.config.minilocale.Placeholders;
import tkachgeek.config.minilocale.wrapper.adventure.MiniMessageWrapper;
import tkachgeek.tkachutils.items.ItemBuilder;
import tkachgeek.tkachutils.items.ItemBuilderFactory;

import java.util.List;
import java.util.UUID;

public class HeadIngredient implements Ingredient {
  String name;
  List<String> description;
  int amount;
  String textures;
  
  @Override
  public ItemStack getItem(Placeholders placeholders) {
    ItemBuilder item = ItemBuilderFactory.of(Material.PLAYER_HEAD);
    
    if (name != null) item.name(MiniMessageWrapper.deserialize(name, placeholders).decoration(TextDecoration.ITALIC, false));
    if (description != null) item.description(MiniMessageWrapper.deserialize(description, placeholders, true));
    if (amount != 0) item.amount(amount);
    
    if (textures != null) {
      PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), "");
      profile.setProperty(new ProfileProperty("textures", textures));
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
  
  public void setTextures(String textures) {
    this.textures = textures;
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
  
  public String getTextures() {
    return textures;
  }
}
