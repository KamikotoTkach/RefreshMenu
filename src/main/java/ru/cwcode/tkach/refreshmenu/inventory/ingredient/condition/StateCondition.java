package ru.cwcode.tkach.refreshmenu.inventory.ingredient.condition;

import ru.cwcode.tkach.refreshmenu.MenuContext;

public class StateCondition implements Condition {
  final String key;
  final String value;

  StateCondition(String key, String value) {
    this.key = key;
    this.value = value;
  }

  static Condition parse(String raw) {
    int idx = raw.indexOf(':');
    if (idx < 0) return ALWAYS_FALSE;
    return new StateCondition(raw.substring(0, idx).trim(), raw.substring(idx + 1).trim());
  }

  @Override
  public boolean matches(MenuContext context) {
    String stateValue = context.view().getState(key);
    return stateValue != null && stateValue.equals(value);
  }
}
