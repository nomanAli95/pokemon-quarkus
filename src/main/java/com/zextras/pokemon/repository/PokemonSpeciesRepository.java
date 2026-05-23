package com.zextras.pokemon.repository;

import com.zextras.pokemon.model.PokemonSpecies;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PokemonSpeciesRepository implements PanacheRepositoryBase<PokemonSpecies, Integer> {

  public List<PokemonSpecies> findEvolutionChain(Integer chainId) {
    return list("evolutionChainId = ?1 ORDER BY sortOrder", chainId);
  }
}
