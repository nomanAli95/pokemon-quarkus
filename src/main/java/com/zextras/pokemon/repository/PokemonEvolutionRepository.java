package com.zextras.pokemon.repository;

import com.zextras.pokemon.model.PokemonEvolution;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class PokemonEvolutionRepository implements PanacheRepositoryBase<PokemonEvolution, Integer> {

  public Optional<PokemonEvolution> findByEvolvedSpeciesId(Integer speciesId) {
    return find("evolvedSpeciesId", speciesId).firstResultOptional();
  }
}
