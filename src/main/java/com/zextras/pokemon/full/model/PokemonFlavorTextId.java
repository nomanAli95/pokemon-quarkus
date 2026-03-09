package com.zextras.pokemon.full.model;

import java.io.Serializable;
import java.util.Objects;

public class PokemonFlavorTextId implements Serializable {

  public Integer speciesId;
  public Integer versionId;

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof PokemonFlavorTextId other)) return false;
    return Objects.equals(speciesId, other.speciesId) && Objects.equals(versionId, other.versionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(speciesId, versionId);
  }
}
