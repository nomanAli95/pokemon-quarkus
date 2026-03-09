package com.zextras.pokemon.basic.repository;

import com.zextras.pokemon.basic.model.PokemonSprites;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PokemonSpritesRepository implements PanacheRepositoryBase<PokemonSprites, Integer> {
}
