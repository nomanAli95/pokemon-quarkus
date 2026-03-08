package com.zextras;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "pokemon_overview")
@NamedQuery(
    name = "PokemonOverview.byType",
    query = "FROM PokemonOverview WHERE LOWER(type1) = LOWER(:type) OR LOWER(type2) = LOWER(:type)"
)
public class PokemonOverview extends PanacheEntityBase {

  @Id
  public Integer id;

  public String name;
  public String color;

  @Column(name = "is_legendary")
  public boolean legendary;

  @Column(name = "is_mythical")
  public boolean mythical;

  public String type1;
  public String type2;

  public Integer hp;
  public Integer attack;
  public Integer defense;

  @Column(name = "sp_attack")
  public Integer spAttack;

  @Column(name = "sp_defense")
  public Integer spDefense;

  public Integer speed;

  @Column(name = "base_stat_total")
  public Integer baseStatTotal;

  @Column(name = "official_artwork_url")
  public String officialArtworkUrl;
}
