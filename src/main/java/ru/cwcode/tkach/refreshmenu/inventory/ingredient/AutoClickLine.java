package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import org.bukkit.event.inventory.ClickType;
import ru.cwcode.tkach.locale.Message;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.action.*;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.condition.Condition;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Одна скомпилированная строка click-DSL из {@link AutoIngredient}.
 * <p>
 * Грамматика: {@code [<модификаторы>] (<гард-условие>) <ключевое-слово> <аргумент>}
 * — гард, ключевое слово и аргумент опциональны.
 * <p>
 * Модификаторы: кнопки ({@code LMB/RMB/MMB/DROP/DOUBLE/SWAP}, {@code SHIFT/CTRL},
 * {@code ANY}/{@code *}) и поведенческие флаги {@code close}/{@code refresh}.
 * Переиспользует существующие {@link Action} как исполнители.
 */
public class AutoClickLine {
  private static final Pattern PATTERN = Pattern.compile(
    "^\\s*\\[(?<mods>[^\\]]*)\\]\\s*(?:\\((?<guard>[^)]*)\\)\\s*)?(?:(?<kw>\\S+)\\s*(?<arg>.*))?$");

  private final Set<ClickType> types;
  private final Condition guard;   // null → без гарда
  private final Action action;     // null → только close/refresh
  private final boolean close;
  private final boolean refresh;

  private AutoClickLine(Set<ClickType> types, Condition guard, Action action, boolean close, boolean refresh) {
    this.types = types;
    this.guard = guard;
    this.action = action;
    this.close = close;
    this.refresh = refresh;
  }

  /** @return скомпилированную строку, либо {@code null}, если синтаксис не распознан. */
  public static AutoClickLine parse(String raw) {
    if (raw == null) return null;
    Matcher m = PATTERN.matcher(raw);
    if (!m.matches()) return null;

    Set<String> tokens = tokenize(m.group("mods"));
    boolean close = tokens.remove("CLOSE");
    boolean refresh = tokens.remove("REFRESH");
    Set<ClickType> types = resolveTypes(tokens);

    String guardStr = m.group("guard");
    Condition guard = guardStr == null ? null : Condition.parse(guardStr);

    String kw = m.group("kw");
    String arg = m.group("arg") == null ? "" : m.group("arg").trim();
    Action action = toAction(kw, arg);

    return new AutoClickLine(types, guard, action, close, refresh);
  }

  public boolean handles(ClickType type) {
    return types.contains(type);
  }

  public void execute(MenuContext context, ClickType clickType) {
    if (guard != null && !guard.matches(context)) return;
    if (action != null) action.accept(context, clickType);
    if (refresh) context.view().updateRequired(context.player());
    if (close) context.player().closeInventory();
  }

  private static Set<String> tokenize(String mods) {
    Set<String> tokens = new LinkedHashSet<>();
    for (String t : mods.trim().split("\\s+")) {
      if (!t.isEmpty()) tokens.add(t.toUpperCase());
    }
    return tokens;
  }

  private static Set<ClickType> resolveTypes(Set<String> tokens) {
    if (tokens.contains("ANY") || tokens.contains("*")) {
      return EnumSet.allOf(ClickType.class);
    }

    boolean shift = tokens.contains("SHIFT");
    boolean ctrl = tokens.contains("CTRL") || tokens.contains("CONTROL");
    Set<ClickType> result = EnumSet.noneOf(ClickType.class);

    if (tokens.contains("LMB") || tokens.contains("LEFT")) result.add(shift ? ClickType.SHIFT_LEFT : ClickType.LEFT);
    if (tokens.contains("RMB") || tokens.contains("RIGHT")) result.add(shift ? ClickType.SHIFT_RIGHT : ClickType.RIGHT);
    if (tokens.contains("MMB") || tokens.contains("MIDDLE")) result.add(ClickType.MIDDLE);
    if (tokens.contains("DROP") || tokens.contains("Q")) result.add(ctrl ? ClickType.CONTROL_DROP : ClickType.DROP);
    if (tokens.contains("DOUBLE") || tokens.contains("DCLICK")) result.add(ClickType.DOUBLE_CLICK);
    if (tokens.contains("SWAP") || tokens.contains("F")) result.add(ClickType.SWAP_OFFHAND);

    return result;
  }

  private static Action toAction(String keyword, String arg) {
    if (keyword == null) return null;
    switch (keyword.toLowerCase()) {
      case "playercommand":  return new PlayerCommandAction(arg);
      case "consolecommand": return new ConsoleCommandAction(arg);
      case "sendmessage":    return new SendMessageAction(new Message(arg));
      case "openview":
      case "setview":        return new SetViewAction(arg);
      case "closeinventory": return new CloseInventoryAction();
      case "nextpage":       return new NextPageIngredientAction();
      case "prevpage":       return new PrevPageIngredientAction();
      case "sound":          return new SoundAction(arg);
      default:               return null;
    }
  }
}
