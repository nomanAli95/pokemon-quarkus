package com.zextras.pokemon.dto;

import com.zextras.pokemon.model.PokemonOverview;
import com.zextras.pokemon.model.PokemonSprites;

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
