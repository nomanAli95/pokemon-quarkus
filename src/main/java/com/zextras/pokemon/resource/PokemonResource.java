package com.zextras.pokemon.resource;

import com.zextras.pokemon.basic.model.PokemonOverview;
import com.zextras.pokemon.basic.renderer.PokemonBasicCardRenderer;
import com.zextras.pokemon.basic.service.PokemonBaseService;
import com.zextras.pokemon.full.renderer.PokemonCardRenderer;
import com.zextras.pokemon.full.service.PokemonService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/pokemon")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Pokémon")
public class PokemonResource {

  @Inject PokemonBaseService pokemonBaseService;
  @Inject PokemonService pokemonService;

  @GET
  public List<PokemonOverview> list(@BeanParam PokemonFilter filter) {
    return pokemonBaseService.list(filter);
  }

  @GET
  @Path("/{id}")
  public Response getById(@PathParam("id") Integer id) {
    return pokemonBaseService.getById(id)
        .map(p -> Response.ok(p).build())
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }

  @GET
  @Path("/{id}/sprite")
  @Produces("image/png")
  public Response getSprite(@PathParam("id") Integer id) {
    return pokemonBaseService.getSprite(id)
        .map(data -> Response.ok(data).build())
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }

  @GET
  @Path("/{id}/sprite/shiny")
  @Produces("image/png")
  public Response getSpriteShiny(@PathParam("id") Integer id) {
    return pokemonBaseService.getSpriteShiny(id)
        .map(data -> Response.ok(data).build())
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }

  @GET
  @Path("/{id}/sprite/back")
  @Produces("image/png")
  public Response getSpriteBack(@PathParam("id") Integer id) {
    return pokemonBaseService.getSpriteBack(id)
        .map(data -> Response.ok(data).build())
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }

  @GET
  @Path("/{id}/card")
  @Produces(MediaType.TEXT_HTML)
  public Response getCard(@PathParam("id") Integer id) {
    return pokemonBaseService.getCard(id)
        .map(card -> Response.ok(PokemonBasicCardRenderer.render(card)).build())
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }

  @GET
  @Path("/{id}/card/full")
  @Produces(MediaType.TEXT_HTML)
  public Response getFullCard(@PathParam("id") Integer id) {
    return pokemonService.getCard(id)
        .map(card -> Response.ok(PokemonCardRenderer.render(card)).build())
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }
}
