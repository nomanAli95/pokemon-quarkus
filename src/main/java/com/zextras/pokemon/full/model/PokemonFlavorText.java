package com.zextras.pokemon.full.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "pokemon_flavor_text")
@IdClass(PokemonFlavorTextId.class)
public class PokemonFlavorText extends PanacheEntityBase {

  @Id
  @Column(name = "species_id")
  public Integer speciesId;

  @Id
  @Column(name = "version_id")
  public Integer versionId;

  @Column(name = "flavor_text")
  public String flavorText;
}
