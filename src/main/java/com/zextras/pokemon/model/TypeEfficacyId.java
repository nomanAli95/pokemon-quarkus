package com.zextras.pokemon.model;

import java.io.Serializable;
import java.util.Objects;

public class TypeEfficacyId implements Serializable {

  public Integer damageTypeId;
  public Integer targetTypeId;

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof TypeEfficacyId other)) return false;
    return Objects.equals(damageTypeId, other.damageTypeId) && Objects.equals(targetTypeId, other.targetTypeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(damageTypeId, targetTypeId);
  }
}
