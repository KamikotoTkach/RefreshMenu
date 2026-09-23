package ru.cwcode.tkach.refreshmenu.inventory.view;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AsyncPageProvider<T> {
  int UNKNOWN_MAX_PAGE = -1;

  CompletableFuture<List<T>> getNextPage();
  CompletableFuture<List<T>> getPrevPage();

  boolean hasNextPage();
  boolean hasPrevPage();

  default int getMaxPage() {
    return UNKNOWN_MAX_PAGE;
  }
}
