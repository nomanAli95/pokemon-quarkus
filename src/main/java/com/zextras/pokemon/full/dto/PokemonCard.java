package com.zextras.pokemon.full.dto;

import com.zextras.pokemon.basic.model.PokemonOverview;
import com.zextras.pokemon.basic.model.PokemonSprites;

import java.util.List;

public record PokemonCard(
    PokemonOverview overview,
    PokemonSprites sprites,
    List<AbilityInfo> abilities,
    List<EvolutionStep> evolutionChain,
    String flavorText,
    List<TypeMatchup> typeMatchups,
    List<String> eggGroups,
    List<MoveInfo> moves
) {}
