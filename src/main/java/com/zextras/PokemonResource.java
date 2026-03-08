package com.zextras;

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

  @Inject
  PokemonRepository pokemonRepository;

  @Inject
  SpritesRepository spritesRepository;

  @GET
  @Path("/{id}")
  public Response getById(@PathParam("id") Integer id) {
    return pokemonRepository.findByIdOptional(id)
            .map(p -> Response.ok(p).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }

  @GET
  public List<PokemonOverview> list(@BeanParam PokemonFilter filter) {
    return pokemonRepository.list(filter);
  }

  @GET
  @Path("/{id}/sprite")
  @Produces("image/png")
  public Response getSprite(@PathParam("id") Integer id) {
    return spritesRepository.findByIdOptional(id)
            .filter(s -> s.frontDefault != null)
            .map(s -> Response.ok(s.frontDefault).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }

  @GET
  @Path("/{id}/sprite/shiny")
  @Produces("image/png")
  public Response getSpriteShiny(@PathParam("id") Integer id) {
    return spritesRepository.findByIdOptional(id)
            .filter(s -> s.frontShiny != null)
            .map(s -> Response.ok(s.frontShiny).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }

  @GET
  @Path("/{id}/sprite/back")
  @Produces("image/png")
  public Response getSpriteBack(@PathParam("id") Integer id) {
    return spritesRepository.findByIdOptional(id)
            .filter(s -> s.backDefault != null)
            .map(s -> Response.ok(s.backDefault).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }

  @GET
  @Path("/{id}/card")
  @Produces(MediaType.TEXT_HTML)
  public Response getCard(@PathParam("id") Integer id) {
    return pokemonRepository.findByIdOptional(id)
            .map(p -> {
              PokemonSprites sprites = spritesRepository.findByIdOptional(id).orElse(null);
              return Response.ok(PokemonCardRenderer.render(p, sprites)).build();
            })
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }
}
