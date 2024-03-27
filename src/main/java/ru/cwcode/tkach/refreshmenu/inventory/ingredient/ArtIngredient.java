package ru.cwcode.tkach.refreshmenu.inventory.ingredient;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
