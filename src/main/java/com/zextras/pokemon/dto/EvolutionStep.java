package com.zextras.pokemon.dto;

import com.zextras.pokemon.model.PokemonEvolution;

public record EvolutionStep(int id, String name, PokemonEvolution conditions) {}
