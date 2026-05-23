package com.zextras.pokemon.resource;

import com.zextras.pokemon.model.PokemonOverview;
import com.zextras.pokemon.renderer.PokemonBasicCardRenderer;
import com.zextras.pokemon.renderer.PokemonCardRenderer;
import com.zextras.pokemon.service.PokemonService;
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

  @Inject PokemonService pokemonService;

  @GET
  public List<PokemonOverview> list(@BeanParam PokemonFilter filter) {
    return pokemonService.list(filter);
  }

  @GET
  @Path("/{id}")
  public Response getById(@PathParam("id") Integer id) {
    return pokemonService.getById(id)
        .map(p -> Response.ok(p).build())
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }

  @GET
  @Path("/{id}/sprite")
  @Produces("image/png")
  public Response getSprite(@PathParam("id") Integer id) {
    return pokemonService.getSprite(id)
        .map(data -> Response.ok(data).build())
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }

  @GET
  @Path("/{id}/sprite/shiny")
  @Produces("image/png")
  public Response getSpriteShiny(@PathParam("id") Integer id) {
    return pokemonService.getSpriteShiny(id)
        .map(data -> Response.ok(data).build())
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }

  @GET
  @Path("/{id}/sprite/back")
  @Produces("image/png")
  public Response getSpriteBack(@PathParam("id") Integer id) {
    return pokemonService.getSpriteBack(id)
        .map(data -> Response.ok(data).build())
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }

  @GET
  @Path("/{id}/card")
  @Produces(MediaType.TEXT_HTML)
  public Response getCard(@PathParam("id") Integer id) {
    return pokemonService.getCard(id)
        .map(card -> Response.ok(PokemonBasicCardRenderer.render(card)).build())
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }

  @GET
  @Path("/{id}/card/full")
  @Produces(MediaType.TEXT_HTML)
  public Response getFullCard(@PathParam("id") Integer id) {
    return pokemonService.getFullCard(id)
        .map(card -> Response.ok(PokemonCardRenderer.render(card)).build())
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }
}
