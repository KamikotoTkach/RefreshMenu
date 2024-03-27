package ru.cwcode.tkach.refreshmenu.inventory.view.drawer;

import ru.cwcode.tkach.refreshmenu.MenuContext;

import java.util.Set;

public abstract class AbstractDrawer {
   public abstract void draw(MenuContext context);
   public abstract void drawChars(MenuContext context, Set<Character> characters);
}
