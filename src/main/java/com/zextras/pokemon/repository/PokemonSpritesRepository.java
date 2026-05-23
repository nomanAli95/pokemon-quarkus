package com.zextras.pokemon.repository;

import com.zextras.pokemon.model.PokemonSprites;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PokemonSpritesRepository implements PanacheRepositoryBase<PokemonSprites, Integer> {
}
