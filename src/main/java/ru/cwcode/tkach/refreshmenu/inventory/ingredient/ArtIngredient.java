package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import ru.cwcode.tkach.config.relocate.com.fasterxml.jackson.annotation.JsonSubTypes;
import ru.cwcode.tkach.config.relocate.com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
      use = JsonTypeInfo.Id.NAME,
      property = "type")
@JsonSubTypes({
})
public interface ArtIngredient extends Ingredient {
  char getChar();

  default byte getMaxDraws() {
    return Byte.MAX_VALUE;
  }
}
