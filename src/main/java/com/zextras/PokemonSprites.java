package com.zextras;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "pokemon_sprites")
public class PokemonSprites extends PanacheEntityBase {

  @Id
  @Column(name = "pokemon_id")
  public Integer pokemonId;

  @Column(name = "front_default")
  public byte[] frontDefault;

  @Column(name = "front_shiny")
  public byte[] frontShiny;

  @Column(name = "back_default")
  public byte[] backDefault;

  @Column(name = "official_artwork_url")
  public String officialArtworkUrl;
}