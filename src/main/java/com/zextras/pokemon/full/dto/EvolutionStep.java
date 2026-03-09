package com.zextras.pokemon.full.dto;

import com.zextras.pokemon.full.model.PokemonEvolution;

public record EvolutionStep(int id, String name, PokemonEvolution conditions) {}
