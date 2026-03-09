package com.zextras.pokemon.full.model;

import java.io.Serializable;
import java.util.Objects;

public class PokemonAbilityId implements Serializable {

  public Integer pokemonId;
  public Integer slot;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PokemonAbilityId other)) return false;
    return Objects.equals(pokemonId, other.pokemonId) && Objects.equals(slot, other.slot);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pokemonId, slot);
  }
}
