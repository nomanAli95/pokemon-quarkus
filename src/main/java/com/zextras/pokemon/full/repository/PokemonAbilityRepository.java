package com.zextras.pokemon.full.repository;

import com.zextras.pokemon.full.model.PokemonAbility;
import com.zextras.pokemon.full.model.PokemonAbilityId;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PokemonAbilityRepository implements PanacheRepositoryBase<PokemonAbility, PokemonAbilityId> {

  public List<PokemonAbility> findByPokemonId(Integer pokemonId) {
    return list("pokemonId", pokemonId);
  }
}
