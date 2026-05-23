package com.zextras.pokemon.repository;

import com.zextras.pokemon.model.PokemonEggGroup;
import com.zextras.pokemon.model.PokemonEggGroupId;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PokemonEggGroupRepository implements PanacheRepositoryBase<PokemonEggGroup, PokemonEggGroupId> {

  public List<PokemonEggGroup> findBySpeciesId(Integer speciesId) {
    return list("speciesId", speciesId);
  }
}
