package com.zextras.pokemon.full.service;

import com.zextras.pokemon.basic.model.PokemonOverview;
import com.zextras.pokemon.basic.model.PokemonSprites;
import com.zextras.pokemon.basic.repository.PokemonRepository;
import com.zextras.pokemon.basic.repository.PokemonSpritesRepository;
import com.zextras.pokemon.full.dto.AbilityInfo;
import com.zextras.pokemon.full.dto.EvolutionStep;
import com.zextras.pokemon.full.dto.MoveInfo;
import com.zextras.pokemon.full.dto.PokemonCard;
import com.zextras.pokemon.full.dto.TypeMatchup;
import com.zextras.pokemon.full.model.TypeEfficacy;
import com.zextras.pokemon.full.repository.AbilityProseRepository;
import com.zextras.pokemon.full.repository.PokemonAbilityRepository;
import com.zextras.pokemon.full.repository.PokemonEggGroupRepository;
import com.zextras.pokemon.full.repository.PokemonEvolutionRepository;
import com.zextras.pokemon.full.repository.PokemonFlavorTextRepository;
import com.zextras.pokemon.full.repository.PokemonMoveRepository;
import com.zextras.pokemon.full.repository.PokemonSpeciesRepository;
import com.zextras.pokemon.full.repository.TypeEfficacyRepository;
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

  public Optional<PokemonCard> getCard(Integer id) {
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
        .replaceAll("\\[\\]\\{[^:}]+:([^}]+)\\}", "$1")  // []{type:water} → water
        .replaceAll("\\[([^\\]]+)\\]\\{[^}]+\\}", "$1"); // [HP]{mechanic:hp} → HP
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
