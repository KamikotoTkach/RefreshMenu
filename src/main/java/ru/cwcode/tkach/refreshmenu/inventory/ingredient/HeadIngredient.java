package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
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
  List<ItemFlag> itemFlags;

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

    Utils.applyCommon(item, name, description, amount, itemFlags, context);
    applyTexture(item);

    return item.build();
  }

  @Override
  public ItemStack getItem(Placeholders placeholders) {
    ItemBuilder item = ItemBuilderFactory.of(Material.PLAYER_HEAD);

    Utils.applyCommon(item, name, description, amount, itemFlags, placeholders, null);
    applyTexture(item);

    return item.build();
  }

  private void applyTexture(ItemBuilder item) {
    if (texture == null) return;

    PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), "");
    profile.setProperty(new ProfileProperty("textures", texture));
    item.playerProfile(profile);
  }
}
