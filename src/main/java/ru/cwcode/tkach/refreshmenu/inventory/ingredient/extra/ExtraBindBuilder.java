package ru.cwcode.tkach.refreshmenu.inventory.ingredient.extra;

import org.bukkit.event.inventory.ClickType;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.action.Action;

public class ExtraBindBuilder extends AbstractExtraBuilder<ExtraBind, ExtraBindBuilder> {
  ExtraBind extraBind = new ExtraBind();
  
  public ExtraBindBuilder bind(ClickType clickType, Action action) {
    extraBind.bind(clickType, action);
    return getThis();
  }
  
  @Override
  public ExtraBind build() {
    ExtraBind extraBind = this.extraBind;
    
    this.extraBind = null;
    
    return extraBind;
  }
}
