package com.zextras.pokemon.basic.service;

import com.zextras.pokemon.basic.dto.PokemonBasicCard;
import com.zextras.pokemon.resource.PokemonFilter;
import com.zextras.pokemon.basic.model.PokemonOverview;
import com.zextras.pokemon.basic.model.PokemonSprites;
import com.zextras.pokemon.basic.repository.PokemonRepository;
import com.zextras.pokemon.basic.repository.PokemonSpritesRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PokemonBaseService {

  @Inject PokemonRepository pokemonRepository;
  @Inject PokemonSpritesRepository spritesRepository;

  public List<PokemonOverview> list(PokemonFilter filter) {
    return pokemonRepository.list(filter);
  }

  public Optional<PokemonOverview> getById(Integer id) {
    return pokemonRepository.findByIdOptional(id);
  }

  public Optional<byte[]> getSprite(Integer id) {
    return spritesRepository.findByIdOptional(id)
        .filter(s -> s.frontDefault != null)
        .map(s -> s.frontDefault);
  }

  public Optional<byte[]> getSpriteShiny(Integer id) {
    return spritesRepository.findByIdOptional(id)
        .filter(s -> s.frontShiny != null)
        .map(s -> s.frontShiny);
  }

  public Optional<byte[]> getSpriteBack(Integer id) {
    return spritesRepository.findByIdOptional(id)
        .filter(s -> s.backDefault != null)
        .map(s -> s.backDefault);
  }

  public Optional<PokemonBasicCard> getCard(Integer id) {
    return pokemonRepository.findByIdOptional(id).map(p -> {
      PokemonSprites sprites = spritesRepository.findByIdOptional(id).orElse(null);
      return new PokemonBasicCard(p, sprites);
    });
  }
}
