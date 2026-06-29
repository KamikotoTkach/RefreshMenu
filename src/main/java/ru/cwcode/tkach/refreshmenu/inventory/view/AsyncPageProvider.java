package ru.cwcode.tkach.refreshmenu.inventory.view;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AsyncPageProvider<T> {
  CompletableFuture<List<T>> getNextPage();
  CompletableFuture<List<T>> getPrevPage();

  boolean hasNextPage();
  boolean hasPrevPage();
}
