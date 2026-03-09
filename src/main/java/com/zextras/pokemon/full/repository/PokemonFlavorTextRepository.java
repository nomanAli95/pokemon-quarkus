package com.zextras.pokemon.full.repository;

import com.zextras.pokemon.full.model.PokemonFlavorText;
import com.zextras.pokemon.full.model.PokemonFlavorTextId;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class PokemonFlavorTextRepository implements PanacheRepositoryBase<PokemonFlavorText, PokemonFlavorTextId> {

  public Optional<String> findLatestBySpeciesId(Integer speciesId) {
    return find("speciesId = ?1 ORDER BY versionId DESC", speciesId)
        .firstResultOptional()
        .map(ft -> ft.flavorText);
  }
}
