package tkachgeek.refreshmenu.inventory.view.drawer;

import tkachgeek.refreshmenu.MenuContext;

import java.util.Set;

public abstract class AbstractDrawer {
   public abstract void draw(MenuContext context);
   public abstract void drawChars(MenuContext context, Set<Character> characters);
}
