package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import ru.cwcode.tkach.locale.Placeholders;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.condition.Condition;
import ru.cwcode.tkach.refreshmenu.inventory.view.drawer.ViewDrawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AutoIngredient implements Ingredient {
  String name;
  List<String> description;
  Integer amount;
  String type;                    // "STONE" | "ns:key" (IA) | "head:<tex>" | "head-name:<ник>"
  Integer customModelData;        // только int в v1
  Boolean glow;

  String show;                    // Condition: ингредиент виден, только если истинно
  String hide;                    // Condition: ингредиент скрыт, если истинно

  List<String> click;             // строковый DSL, см. ClickLine (TODO)

  LinkedHashMap<String, AutoIngredient> state = new LinkedHashMap<>();   // condition -> patch

  transient String lastState = null;   // для shouldRefresh: последний сматчивший ключ
  transient Map<String, Condition> conditionCache = new HashMap<>();   // строка условия -> распарсенный Condition
  transient List<AutoClickLine> compiledClickCache = null;            // лениво скомпилированные строки click

  public AutoIngredient() {
  }

  // === Рендер ===

  @Override
  public ItemStack getItem(MenuContext context) {
    if (isHidden(context)) return ViewDrawer.AIR;

    AutoIngredient effective = resolveState(context);
    return effective.applyMeta(effective.buildDelegate().getItem(context));
  }

  @Override
  public ItemStack getItem(Placeholders placeholders) {
    // Placeholders-вариант не знает про MenuContext → без стейтов/видимости.
    return applyMeta(buildDelegate().getItem(placeholders));
  }

  private Ingredient buildDelegate() {
    String t = type == null ? "STONE" : type;
    int amt = amount == null ? 0 : amount;
    int cmd = customModelData == null ? 0 : customModelData;

    Ingredient delegate;
    if (t.startsWith("head:")) {
      delegate = new HeadIngredient(name, description, amt, t.substring("head:".length()));
    } else if (t.startsWith("head-name:")) {
      // TODO: построить текстуру по нику игрока, пока fallback на пустую голову
      delegate = new HeadIngredient(name, description, amt, null);
    } else if (t.contains(":")) {
      delegate = new ItemsAdderIngredient(name, description, amt, t);
    } else {
      delegate = new IngredientImpl(name, description, amt, Material.valueOf(t.toUpperCase()), cmd);
    }

    return delegate;
  }

  private ItemStack applyMeta(ItemStack item) {
    if (Boolean.TRUE.equals(glow) && item != null) {
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
        meta.addEnchant(Enchantment.LURE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
      }
    }
    return item;
  }

  private AutoIngredient resolveState(MenuContext context) {
    for (Map.Entry<String, AutoIngredient> entry : state.entrySet()) {
      if (matchesCondition(entry.getKey(), context)) {
        lastState = entry.getKey();
        return mergedWith(entry.getValue());
      }
    }
    lastState = null;
    return this;
  }

  private AutoIngredient mergedWith(AutoIngredient patch) {
    AutoIngredient r = new AutoIngredient();
    r.name = patch.name != null ? patch.name : this.name;
    r.description = patch.description != null ? patch.description : this.description;
    r.amount = patch.amount != null ? patch.amount : this.amount;
    r.type = patch.type != null ? patch.type : this.type;
    r.customModelData = patch.customModelData != null ? patch.customModelData : this.customModelData;
    r.glow = patch.glow != null ? patch.glow : this.glow;
    r.show = patch.show != null ? patch.show : this.show;
    r.hide = patch.hide != null ? patch.hide : this.hide;
    r.click = patch.click != null ? patch.click : this.click;
    return r;
  }

  private boolean isHidden(MenuContext context) {
    if (hide != null && matchesCondition(hide, context)) return true;
    if (show != null && !matchesCondition(show, context)) return true;
    return false;
  }

  @Override
  public void onClick(MenuContext context, InventoryClickEvent event) {
    AutoIngredient effective = resolveState(context);

    boolean handled = false;
    for (AutoClickLine line : effective.compiledClick()) {
      if (line.handles(event.getClick())) {
        line.execute(context, event.getClick());
        handled = true;
      }
    }

    if (!handled) Ingredient.super.onClick(context, event);
  }

  private List<AutoClickLine> compiledClick() {
    if (compiledClickCache == null) {
      compiledClickCache = new ArrayList<>();
      if (click != null) {
        for (String raw : click) {
          AutoClickLine line = AutoClickLine.parse(raw);
          if (line != null) compiledClickCache.add(line);
        }
      }
    }
    return compiledClickCache;
  }


  @Override
  public boolean shouldRefresh(MenuContext context) {
    // Рефреш, когда сменился сматчивший стейт (как lastState в ExtraState).
    // TODO: учесть также смену резолва динамических полей (amount %ph%, show/hide условия).
    for (Map.Entry<String, AutoIngredient> entry : state.entrySet()) {
      boolean matches = matchesCondition(entry.getKey(), context);
      if (matches != entry.getKey().equals(lastState)) return true;
    }
    return false;
  }

  private boolean matchesCondition(String condition, MenuContext context) {
    return conditionCache.computeIfAbsent(condition, Condition::parse).matches(context);
  }
}
