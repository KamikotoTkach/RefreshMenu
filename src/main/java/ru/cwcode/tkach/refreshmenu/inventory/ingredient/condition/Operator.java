package ru.cwcode.tkach.refreshmenu.inventory.ingredient.condition;

public enum Operator {
  EQ("=="),
  NEQ("!="),
  GTE(">="),
  LTE("<="),
  GT(">"),
  LT("<");

  public static final Operator[] MATCH_ORDER = {EQ, NEQ, GTE, LTE, GT, LT};

  public final String token;

  Operator(String token) {
    this.token = token;
  }

  public boolean compare(String left, String right) {
    Double l = parseDouble(left);
    Double r = parseDouble(right);
    boolean numeric = l != null && r != null;

    switch (this) {
      case EQ:  return numeric ? l.doubleValue() == r.doubleValue() : left.equals(right);
      case NEQ: return numeric ? l.doubleValue() != r.doubleValue() : !left.equals(right);
      case GT:  return numeric && l > r;
      case GTE: return numeric && l >= r;
      case LT:  return numeric && l < r;
      case LTE: return numeric && l <= r;
      default:  return false;
    }
  }

  private static Double parseDouble(String s) {
    try {
      return Double.parseDouble(s.trim());
    } catch (NumberFormatException e) {
      return null;
    }
  }
}
