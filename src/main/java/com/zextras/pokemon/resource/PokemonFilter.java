package com.zextras.pokemon.resource;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;

public class PokemonFilter {

  @QueryParam("page")      @DefaultValue("0")  public int page;
  @QueryParam("size")      @DefaultValue("20") public int size;
  @QueryParam("type")      public String type;
  @QueryParam("color")     public String color;
  @QueryParam("legendary") public Boolean legendary;
  @QueryParam("mythical")  public Boolean mythical;
}
