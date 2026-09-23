package ru.cwcode.tkach.refreshmenu.inventory.view;

import org.bukkit.entity.Player;
import ru.cwcode.tkach.refreshmenu.MenuContext;
import ru.cwcode.tkach.refreshmenu.inventory.ingredient.Ingredient;
import ru.cwcode.tkach.refreshmenu.inventory.shape.InventoryShape;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class AsyncPageView<I extends Ingredient, DTO> extends PagedView<I> {
  public static final String UNKNOWN_MAX_PAGE = "?";

  private final Function<DTO, I> dtoToIngredient;
  private final AsyncPageProvider<DTO> asyncPageProvider;
  private final AtomicBoolean isLoadingPage = new AtomicBoolean(false);
  private int loadedPage = 0;

  public AsyncPageView(AsyncPageProvider<DTO> asyncPageProvider, Function<DTO, I> dtoToIngredient, InventoryShape shape) {
    this.shape = shape;
    this.asyncPageProvider = asyncPageProvider;
    this.dtoToIngredient = dtoToIngredient;
  }

  @Override
  public void open(Player player) {
    super.open(player);
    nextPage();
  }

  @Override
  public void nextPage() {
    if (isLoadingPage.get() || !asyncPageProvider.hasNextPage()) return;

    isLoadingPage.set(true);
    setState("loadingNextPage", "true");
    drawer.drawChars(new MenuContext(this, player), List.of('>'));

    asyncPageProvider.getNextPage().thenAccept(dtos -> {
      if (!isCurrentMenuOpen()) return;

      loadedPage++;
      setDynamic(dtos.stream().map(dtoToIngredient).toList());
      setState("hasNextPage", asyncPageProvider.hasNextPage() + "");
      setState("hasPrevPage", asyncPageProvider.hasPrevPage() + "");
      setState("loadingNextPage", "false");
      drawInventory(player);
    }).whenComplete((unused, throwable) -> isLoadingPage.set(false));
  }

  @Override
  protected void updatePlaceholders() {
    int currentPage = Math.max(1, loadedPage);
    int maxPage = asyncPageProvider.getMaxPage();
    boolean maxPageKnown = maxPage > 0;

    placeholders.add("page", currentPage);
    placeholders.add("max_page", maxPageKnown ? String.valueOf(maxPage) : UNKNOWN_MAX_PAGE);
    placeholders.add("next_page", maxPageKnown ? Math.min(maxPage, currentPage + 1) : currentPage + 1);
    placeholders.add("prev_page", Math.max(1, currentPage - 1));
  }

  @Override
  public boolean hasNextPage() {
    return asyncPageProvider.hasNextPage();
  }

  @Override
  public boolean hasPrevPage() {
    return asyncPageProvider.hasPrevPage();
  }

  private boolean isCurrentMenuOpen() {
    return this.equals(player.getOpenInventory().getTopInventory().getHolder());
  }

  @Override
  public void prevPage() {
    if (isLoadingPage.get() || !asyncPageProvider.hasPrevPage()) return;

    isLoadingPage.set(true);

    setState("loadingPrevPage", "true");
    drawer.drawChars(new MenuContext(this, player), List.of('<'));

    asyncPageProvider.getPrevPage().thenAccept(dtos -> {
      if (!isCurrentMenuOpen()) return;

      loadedPage--;
      setDynamic(dtos.stream().map(dtoToIngredient).toList());
      setState("hasNextPage", asyncPageProvider.hasNextPage() + "");
      setState("hasPrevPage", asyncPageProvider.hasPrevPage() + "");
      setState("loadingPrevPage", "false");

      drawInventory(player);
    }).whenComplete((unused, throwable) -> isLoadingPage.set(false));
  }
}
