package ru.cwcode.tkach.refreshmenu.protocol;

import com.comphenix.protocol.events.PacketContainer;
import ru.cwcode.cwutils.server.PaperServerUtils;

import java.util.Map;

public enum WindowClickPacketReader {
  V1_16_5_AND_OLDER(false) {
    @Override
    int readSlot(PacketContainer packet) {
      return packet.getIntegers().read(1);
    }
    
    @Override
    int readButton(PacketContainer packet) {
      return packet.getIntegers().read(2);
    }
  },
  V1_17_TO_1_21_7(true) {
    @Override
    int readSlot(PacketContainer packet) {
      return packet.getIntegers().read(2);
    }
    
    @Override
    int readButton(PacketContainer packet) {
      return packet.getIntegers().read(3);
    }
  },
  V1_21_8_AND_NEWER(true) {
    @Override
    int readSlot(PacketContainer packet) {
      return packet.getShorts().read(0);
    }
    
    @Override
    int readButton(PacketContainer packet) {
      return packet.getBytes().read(0);
    }
  };
  
  private final boolean hasChangedSlots;
  
  WindowClickPacketReader(boolean hasChangedSlots) {
    this.hasChangedSlots = hasChangedSlots;
  }
  
  abstract int readSlot(PacketContainer packet);
  
  abstract int readButton(PacketContainer packet);
  
  boolean hasChangedSlots() {
    return hasChangedSlots;
  }
  
  @SuppressWarnings("unchecked")
  Map<Integer, Object> readChangedSlots(PacketContainer packet) {
    Map<Integer, Object> changedSlots = (Map<Integer, Object>) packet.getSpecificModifier(Map.class).readSafely(0);
    return changedSlots == null ? Map.of() : changedSlots;
  }
  
  public static WindowClickPacketReader create() {
    if (PaperServerUtils.isVersionGreater("1.21.7")) return V1_21_8_AND_NEWER;
    if (PaperServerUtils.isVersionGreater("1.16.5")) return V1_17_TO_1_21_7;
    return V1_16_5_AND_OLDER;
  }
}