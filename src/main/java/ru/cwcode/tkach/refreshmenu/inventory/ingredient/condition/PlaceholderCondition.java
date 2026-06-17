package ru.cwcode.tkach.refreshmenu.inventory.ingredient.condition;

import ru.cwcode.tkach.locale.paper.papi.PapiWrapper;
import ru.cwcode.tkach.refreshmenu.MenuContext;

public class PlaceholderCondition implements Condition {
  final String left;
  final Operator op;
  final String right;

  PlaceholderCondition(String left, Operator op, String right) {
    this.left = left;
    this.op = op;
    this.right = right;
  }

  static Condition parse(String raw) {
    for (Operator op : Operator.MATCH_ORDER) {
      int idx = raw.indexOf(op.token);
      if (idx >= 0) {
        String left = raw.substring(0, idx).trim();
        String right = raw.substring(idx + op.token.length()).trim();
        return new PlaceholderCondition(left, op, right);
      }
    }

    // сокращение %ph%:value — двоеточие после закрывающего '%'
    int closePct = raw.lastIndexOf('%');
    int colon = closePct >= 0 ? raw.indexOf(':', closePct + 1) : -1;
    if (colon > closePct) {
      return new PlaceholderCondition(raw.substring(0, colon).trim(), Operator.EQ, raw.substring(colon + 1).trim());
    }

    return ALWAYS_FALSE;
  }

  @Override
  public boolean matches(MenuContext context) {
    String l = PapiWrapper.process(left, context.player());
    String r = PapiWrapper.process(right, context.player());
    return op.compare(l, r);
  }
}
