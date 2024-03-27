package tkachgeek.refreshmenu.inventory.ingredient.action;

import org.bukkit.event.inventory.ClickType;
import tkachgeek.config.minilocale.Message;
import tkachgeek.config.minilocale.Placeholder;
import tkachgeek.refreshmenu.MenuContext;

public class SendMessageAction implements Action {
  Message message = new Message();
  
  public SendMessageAction(Message message) {
    this.message = message;
  }
  
  public SendMessageAction() {
  }
  
  @Override
  public void accept(MenuContext context, ClickType clickType) {
    message.send(context.player(), Placeholder.add("player", context.player().getName()));
  }
}
