package tkachgeek.refreshmenu.inventory.view.drawer;

import tkachgeek.refreshmenu.MenuContext;

public abstract class AbstractDrawer {
   public abstract void draw(MenuContext context);
   public abstract void drawChar(MenuContext context, char character);
}
