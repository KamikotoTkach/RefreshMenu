package ru.cwcode.tkach.refreshmenu.inventory.view;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.function.Consumer;

public class Behavior {
  HashMap<ClickData, Consumer<InventoryClickEvent>> binds = new HashMap<>();
  
  public Behavior(Behavior toClone) {
    this.binds = toClone.binds;
  }
  
  public Behavior() {
  }
  
  public void bind(char character, ClickType clickType, Runnable runnable) {
    bind(new ClickData(character, clickType), runnable);
  }
  
  public void bind(char character, ClickType clickType, Consumer<InventoryClickEvent> runnable) {
    bind(new ClickData(character, clickType), runnable);
  }
  
  public void bind(ClickData clickData, Runnable runnable) {
    binds.put(clickData, (e) -> runnable.run());
  }
  public void bind(ClickData clickData, Consumer<InventoryClickEvent> consumer) {
    binds.put(clickData, consumer);
  }
  
  public void unbind(char character, ClickType clickType) {
    unbind(new ClickData(character, clickType));
  }
  
  public void unbind(ClickData clickData) {
    binds.remove(clickData);
  }
  
  public void execute(InventoryClickEvent event, char character, ClickType clickType) {
    execute(event, new ClickData(character, clickType));
  }
  
  public void execute(InventoryClickEvent event, ClickData clickData) {
    if (binds.containsKey(clickData)) {
      binds.get(clickData).accept(event);
    }
  }
  
  public static class ClickData {
    char character;
    ClickType clickType;
    
    public ClickData(char character, ClickType clickType) {
      this.character = character;
      this.clickType = clickType;
    }
    
    public char getCharacter() {
      return character;
    }
    
    public ClickType getClickType() {
      return clickType;
    }
    
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      
      ClickData clickData = (ClickData) o;
      
      if (character != clickData.character) return false;
      return clickType == clickData.clickType;
    }
    
    @Override
    public int hashCode() {
      int result = character;
      result = 31 * result + (clickType != null ? clickType.hashCode() : 0);
      return result;
    }
  }
}
