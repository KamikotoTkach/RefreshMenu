package ru.cwcode.tkach.refreshmenu.inventory.view.drawer;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import ru.cwcode.cwutils.numbers.NumbersUtils;
import ru.cwcode.cwutils.protocol.Packet;
import ru.cwcode.tkach.refreshmenu.MenuContext;

import java.util.Arrays;
import java.util.Collection;

public class ExtendedViewDrawer extends PagedViewDrawer {
  volatile int inventorySize;
  @Getter
  volatile ItemStack[] playerInventoryBuffer = new ItemStack[36];
  @Getter
  volatile boolean[] playerInventoryControlledSlots = new boolean[36];
  private volatile boolean[] previouslyControlledPlayerInventorySlots = new boolean[36];
  
  @Override
  public void drawChars(MenuContext context, Collection<Character> characters) {
    if (buffer != null) return;
    
    inventorySize = context.view().getInventory().getSize();
    if (characters.isEmpty()) {
      previouslyControlledPlayerInventorySlots = playerInventoryControlledSlots.clone();
      Arrays.fill(playerInventoryControlledSlots, false);
    }
    
    super.drawChars(context, characters);
  }
  
  public void sendSlotPacket(Player player, int slot) {
    sendSlotPacket(player, slot, isValidPlayerInventorySlot(slot) ? playerInventoryBuffer[slot] : null);
  }
  
  private void sendRealSlotPacket(Player player, int slot) {
    sendSlotPacket(player, slot, getRealPlayerInventoryItem(player, slot));
  }
  
  private void sendSlotPacket(Player player, int slot, ItemStack item) {
    if (!isValidPlayerInventorySlot(slot)) return;
    
    try {
      Packet.setSlot(player, slot > 27 ? slot - 27 : slot + 9, item == null ? AIR : item, -2);
    } catch (Exception e) {
    }
  }
  
  @Override
  protected int getDrawingSize(MenuContext context) {
    return NumbersUtils.notGreater(context.view().getShape().getJoinedShape().length(),
                                   inventorySize + 36);
  }
  
  @Override
  protected void setItem(MenuContext context, int slot, @Nullable ItemStack item) {
    if (slot < inventorySize) {
      super.setItem(context, slot, item);
    } else {
      int playerInventorySlot = slot - inventorySize;
      if (!isValidPlayerInventorySlot(playerInventorySlot)) return;
      
      playerInventoryBuffer[playerInventorySlot] = item;
      playerInventoryControlledSlots[playerInventorySlot] = true;
    }
  }
  
  @Override
  public void drawItem(MenuContext context, int slot, @Nullable ItemStack item) {
    if (slot < inventorySize) {
      super.drawItem(context, slot, item);
    } else {
      int playerInventorySlot = slot - inventorySize;
      if (!isValidPlayerInventorySlot(playerInventorySlot)) return;
      
      playerInventoryBuffer[playerInventorySlot] = item;
      playerInventoryControlledSlots[playerInventorySlot] = true;
      sendSlotPacket(context.player(), playerInventorySlot);
    }
  }
  
  @Override
  protected void drawBuffer(MenuContext context) {
    super.drawBuffer(context);
    
    for (int i = 0; i < playerInventoryBuffer.length; i++) {
      if (playerInventoryControlledSlots[i]) {
        sendSlotPacket(context.player(), i);
      } else if (previouslyControlledPlayerInventorySlots[i]) {
        sendRealSlotPacket(context.player(), i);
      }
    }
    
    Arrays.fill(previouslyControlledPlayerInventorySlots, false);
  }
  
  public int getTopInventorySize() {
    return inventorySize;
  }
  
  public boolean isPlayerInventorySlotControlled(int slot) {
    return isValidPlayerInventorySlot(slot) && playerInventoryControlledSlots[slot];
  }
  
  public boolean isRawSlotControlled(int rawSlot) {
    return rawSlot >= inventorySize && isPlayerInventorySlotControlled(rawSlot - inventorySize);
  }
  
  public ItemStack getDisplayedPlayerInventoryItem(Player player, int slot) {
    if (!isValidPlayerInventorySlot(slot)) return AIR;
    return isPlayerInventorySlotControlled(slot) ? playerInventoryBuffer[slot] : getRealPlayerInventoryItem(player, slot);
  }
  
  private ItemStack getRealPlayerInventoryItem(Player player, int slot) {
    return player.getInventory().getItem(slot > 26 ? slot - 27 : slot + 9);
  }
  
  private boolean isValidPlayerInventorySlot(int slot) {
    return slot >= 0 && slot < playerInventoryBuffer.length;
  }
  
}
