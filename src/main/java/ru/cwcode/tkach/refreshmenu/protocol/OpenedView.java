package ru.cwcode.tkach.refreshmenu.protocol;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.cwcode.tkach.refreshmenu.inventory.view.View;

@Getter
@RequiredArgsConstructor
public class OpenedView {
  private static final int UNBOUND = -1;

  private final View view;
  private volatile int windowId = UNBOUND;

  void bindWindow(int windowId) {
    if (this.windowId != UNBOUND) return;

    this.windowId = windowId;
  }

  boolean matches(int windowId) {
    return this.windowId == UNBOUND || this.windowId == windowId;
  }
}
