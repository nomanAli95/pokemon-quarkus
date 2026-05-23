package com.zextras.pokemon.dto;

public record MoveInfo(
    String name,
    String type,
    String damageClass,
    Integer power,
    Integer pp,
    Integer accuracy,
    String learnMethod,
    int level
) {}
