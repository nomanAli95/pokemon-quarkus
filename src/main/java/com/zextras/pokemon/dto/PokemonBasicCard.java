package com.zextras.pokemon.dto;

import com.zextras.pokemon.model.PokemonOverview;
import com.zextras.pokemon.model.PokemonSprites;

public record PokemonBasicCard(PokemonOverview overview, PokemonSprites sprites) {}
