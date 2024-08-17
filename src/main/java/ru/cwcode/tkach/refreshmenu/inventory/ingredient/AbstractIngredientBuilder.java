package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

public abstract class AbstractIngredientBuilder<I extends Ingredient, B extends AbstractIngredientBuilder<I, B>> {
  protected B getThis() {
    return (B) this;
  };
  
  public abstract I build();
}