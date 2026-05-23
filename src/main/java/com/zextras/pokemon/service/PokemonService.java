package com.zextras.pokemon.service;

import com.zextras.pokemon.dto.AbilityInfo;
import com.zextras.pokemon.dto.EvolutionStep;
import com.zextras.pokemon.dto.MoveInfo;
import com.zextras.pokemon.dto.PokemonBasicCard;
import com.zextras.pokemon.dto.PokemonCard;
import com.zextras.pokemon.dto.TypeMatchup;
import com.zextras.pokemon.model.PokemonOverview;
import com.zextras.pokemon.model.PokemonSprites;
import com.zextras.pokemon.model.TypeEfficacy;
import com.zextras.pokemon.repository.AbilityProseRepository;
import com.zextras.pokemon.repository.PokemonAbilityRepository;
import com.zextras.pokemon.repository.PokemonEggGroupRepository;
import com.zextras.pokemon.repository.PokemonEvolutionRepository;
import com.zextras.pokemon.repository.PokemonFlavorTextRepository;
import com.zextras.pokemon.repository.PokemonMoveRepository;
import com.zextras.pokemon.repository.PokemonRepository;
import com.zextras.pokemon.repository.PokemonSpeciesRepository;
import com.zextras.pokemon.repository.PokemonSpritesRepository;
import com.zextras.pokemon.repository.TypeEfficacyRepository;
import com.zextras.pokemon.resource.PokemonFilter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.*;

@ApplicationScoped
public class PokemonService {

  @Inject PokemonRepository pokemonRepository;
  @Inject PokemonSpritesRepository spritesRepository;
  @Inject PokemonAbilityRepository abilityRepository;
  @Inject AbilityProseRepository abilityProseRepository;
  @Inject PokemonSpeciesRepository speciesRepository;
  @Inject PokemonEvolutionRepository evolutionRepository;
  @Inject PokemonFlavorTextRepository flavorTextRepository;
  @Inject TypeEfficacyRepository efficacyRepository;
  @Inject PokemonEggGroupRepository eggGroupRepository;
  @Inject PokemonMoveRepository moveRepository;

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

  public Optional<PokemonCard> getFullCard(Integer id) {
    return pokemonRepository.findByIdOptional(id).map(overview -> {
      PokemonSprites sprites = spritesRepository.findByIdOptional(id).orElse(null);

      List<AbilityInfo> abilities = abilityRepository.findByPokemonId(id).stream()
          .map(pa -> new AbilityInfo(
              pa.ability.identifier,
              pa.hidden,
              abilityProseRepository.findByIdOptional(pa.ability.id).map(p -> cleanEffect(p.shortEffect)).orElse(null)
          ))
          .toList();

      List<EvolutionStep> chain = overview.evolutionChainId != null
          ? speciesRepository.findEvolutionChain(overview.evolutionChainId).stream()
              .map(s -> new EvolutionStep(s.id, s.identifier, evolutionRepository.findByEvolvedSpeciesId(s.id).orElse(null)))
              .toList()
          : List.of();

      String flavorText = flavorTextRepository.findLatestBySpeciesId(id).orElse(null);

      List<TypeMatchup> typeMatchups = buildTypeMatchups(overview.type1, overview.type2);

      List<String> eggGroups = eggGroupRepository.findBySpeciesId(id).stream()
          .map(peg -> peg.eggGroup.identifier)
          .toList();

      List<MoveInfo> moves = moveRepository.findLatestVersionMoves(id).stream()
          .map(pm -> new MoveInfo(
              pm.move.name != null ? pm.move.name : pm.move.identifier,
              pm.move.type != null ? pm.move.type.identifier : null,
              pm.move.damageClass,
              pm.move.power,
              pm.move.pp,
              pm.move.accuracy,
              pm.learnMethod,
              pm.level
          ))
          .toList();

      return new PokemonCard(overview, sprites, abilities, chain, flavorText, typeMatchups, eggGroups, moves);
    });
  }

  private static String cleanEffect(String text) {
    if (text == null) return null;
    return text
        .replaceAll("\\[\\]\\{[^:}]+:([^}]+)\\}", "$1")
        .replaceAll("\\[([^\\]]+)\\]\\{[^}]+\\}", "$1");
  }

  private List<TypeMatchup> buildTypeMatchups(String type1, String type2) {
    if (type1 == null) return List.of();

    Map<String, Integer> combined = new HashMap<>();
    for (TypeEfficacy te : efficacyRepository.findByTargetTypeIdentifier(type1)) {
      combined.put(te.damageType.identifier, te.damageFactor);
    }
    if (type2 != null) {
      for (TypeEfficacy te : efficacyRepository.findByTargetTypeIdentifier(type2)) {
        int existing = combined.getOrDefault(te.damageType.identifier, 100);
        combined.put(te.damageType.identifier, existing * te.damageFactor / 100);
      }
    }
    return combined.entrySet().stream()
        .filter(e -> e.getValue() != 100)
        .map(e -> new TypeMatchup(e.getKey(), e.getValue()))
        .sorted(Comparator.comparingInt(TypeMatchup::factor).reversed())
        .toList();
  }
}
