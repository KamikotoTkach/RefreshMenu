package ru.cwcode.tkach.refreshmenu.inventory.ingredient.extra;

public abstract class AbstractExtraBuilder<E extends Extra, B extends AbstractExtraBuilder<E, B>> {
  protected B getThis() {
    return (B) this;
  }
  
  public abstract E build();
}
