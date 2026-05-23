package com.zextras.pokemon.repository;

import com.zextras.pokemon.model.PokemonMove;
import com.zextras.pokemon.model.PokemonMoveId;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PokemonMoveRepository implements PanacheRepositoryBase<PokemonMove, PokemonMoveId> {

  public List<PokemonMove> findLatestVersionMoves(Integer pokemonId) {
    return find("pokemonId = ?1 ORDER BY versionGroupId DESC", pokemonId)
        .firstResultOptional()
        .map(latest -> list("pokemonId = ?1 AND versionGroupId = ?2 ORDER BY learnMethod, level",
            pokemonId, latest.versionGroupId))
        .orElse(List.of());
  }
}
