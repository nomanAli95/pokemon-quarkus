package com.zextras.pokemon.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "types")
public class Type extends PanacheEntityBase {

  @Id
  public Integer id;

  public String identifier;
}
