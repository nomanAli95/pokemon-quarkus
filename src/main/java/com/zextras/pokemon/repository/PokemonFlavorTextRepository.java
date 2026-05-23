package com.zextras.pokemon.repository;

import com.zextras.pokemon.model.PokemonFlavorText;
import com.zextras.pokemon.model.PokemonFlavorTextId;
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
