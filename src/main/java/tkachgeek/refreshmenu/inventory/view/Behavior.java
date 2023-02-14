package tkachgeek.refreshmenu.inventory.view;

import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;

public class Behavior {
  HashMap<ClickData, Runnable> binds = new HashMap<>();
  
  public Behavior(Behavior toClone) {
    this.binds = toClone.binds;
  }
  
  public Behavior() {
  }
  
  public void bind(char character, ClickType clickType, Runnable runnable) {
    bind(new ClickData(character, clickType), runnable);
  }
  
  public void bind(ClickData clickData, Runnable runnable) {
    binds.put(clickData, runnable);
  }
  
  public void unbind(char character, ClickType clickType) {
    unbind(new ClickData(character, clickType));
  }
  
  public void unbind(ClickData clickData) {
    binds.remove(clickData);
  }
  
  public void execute(char character, ClickType clickType) {
    execute(new ClickData(character, clickType));
  }
  
  public void execute(ClickData clickData) {
    if (binds.containsKey(clickData)) {
      binds.get(clickData).run();
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
