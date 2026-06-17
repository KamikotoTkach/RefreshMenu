package ru.cwcode.tkach.refreshmenu.inventory.ingredient.condition;

import ru.cwcode.tkach.refreshmenu.MenuContext;

public class PermissionCondition implements Condition {
  final String node;

  PermissionCondition(String node) {
    this.node = node;
  }

  @Override
  public boolean matches(MenuContext context) {
    return context.player().hasPermission(node);
  }
}
