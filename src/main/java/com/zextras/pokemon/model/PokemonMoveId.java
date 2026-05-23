package com.zextras.pokemon.model;

import java.io.Serializable;
import java.util.Objects;

public class PokemonMoveId implements Serializable {

  public Integer pokemonId;
  public Integer versionGroupId;
  public Integer moveId;
  public String learnMethod;

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof PokemonMoveId other)) return false;
    return Objects.equals(pokemonId, other.pokemonId)
        && Objects.equals(versionGroupId, other.versionGroupId)
        && Objects.equals(moveId, other.moveId)
        && Objects.equals(learnMethod, other.learnMethod);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pokemonId, versionGroupId, moveId, learnMethod);
  }
}
