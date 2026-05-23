package com.zextras.pokemon.model;

import java.io.Serializable;
import java.util.Objects;

public class PokemonEggGroupId implements Serializable {

  public Integer speciesId;
  public Integer eggGroupId;

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof PokemonEggGroupId other)) return false;
    return Objects.equals(speciesId, other.speciesId) && Objects.equals(eggGroupId, other.eggGroupId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(speciesId, eggGroupId);
  }
}
