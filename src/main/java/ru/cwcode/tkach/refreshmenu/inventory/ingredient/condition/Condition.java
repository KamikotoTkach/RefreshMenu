package ru.cwcode.tkach.refreshmenu.inventory.ingredient.condition;

import ru.cwcode.tkach.refreshmenu.MenuContext;

/**
 * Условие, вычисляемое на {@link MenuContext}. Парсится из строки и переиспользуется
 * в нескольких местах {@code !<auto>}: ключи стейтов, {@code show}/{@code hide},
 * гарды на клики.
 * <p>
 * Поддерживаемые формы:
 * <ul>
 *   <li>{@code key:value} — {@code view().getState(key).equals(value)}</li>
 *   <li>{@code perm:node} / {@code permission:node} — {@code player.hasPermission(node)}</li>
 *   <li>{@code %placeholder% OP value} — PAPI-резолв + сравнение, {@code OP} ∈ {@code == != > >= < <=}</li>
 *   <li>{@code %placeholder%:value} — сокращение для {@code %placeholder% == value}</li>
 * </ul>
 */
public interface Condition {
  Condition ALWAYS_FALSE = context -> false;
  Condition ALWAYS_TRUE = context -> true;

  boolean matches(MenuContext context);

  static Condition parse(String raw) {
    if (raw == null) return ALWAYS_FALSE;

    String s = raw.trim();
    if (s.isEmpty()) return ALWAYS_FALSE;

    if (s.startsWith("perm:")) return new PermissionCondition(s.substring("perm:".length()).trim());
    if (s.startsWith("permission:")) return new PermissionCondition(s.substring("permission:".length()).trim());

    if (s.indexOf('%') >= 0) return PlaceholderCondition.parse(s);

    return StateCondition.parse(s);
  }
}
