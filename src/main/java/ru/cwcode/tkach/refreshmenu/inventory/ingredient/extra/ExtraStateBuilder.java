package ru.cwcode.tkach.refreshmenu.inventory.ingredient.extra;

import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;

public class ExtraStateBuilder extends AbstractExtraBuilder<ExtraState, ExtraStateBuilder> {
  ExtraState state = new ExtraState();
  
  public ExtraStateBuilder addState(@Subst("state:value") @Pattern("^[^:]+:[^:]+$") String state, Ingredient display) {
    this.state.addState(state, display);
    return getThis();
  }
  
  @Override
  public ExtraState build() {
    ExtraState extraState = this.state;
    
    this.state = null;
    
    return extraState;
  }
}
