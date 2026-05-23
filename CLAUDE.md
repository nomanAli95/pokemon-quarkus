# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & run

Maven wrapper (`./mvnw`) is the entry point — Java 21 is required (`mise.toml` pins `openjdk-21`).

```bash
./mvnw quarkus:dev                              # live-reload dev mode, Dev UI at /q/dev/
./mvnw package                                  # standard build → target/quarkus-app/quarkus-run.jar
./mvnw package -Dquarkus.package.jar.type=uber-jar   # self-contained jar
./mvnw package -Dnative                         # native image (needs local GraalVM)
./mvnw package -Dnative -Dquarkus.native.container-build=true  # native via Mandrel container
```

Dev mode connects to a real PostgreSQL on `localhost:5432/pokedex` (see `application.properties`). The DB is **not** managed by this repo — it comes from the [`nomanali95/pokemon-postgres`](https://github.com/nomanAli95/pokemon-postgres) image. Use `docker compose up --build` to start DB + app together; `docker-compose.native.yml` is an overlay that swaps in `Dockerfile.native`.

Swagger UI: `http://localhost:8080/swagger-ui`.

## Tests

Tests run via Surefire (unit/`*Test`) and Failsafe (integration/`*IT`, only enabled with `-Dnative`).

```bash
./mvnw test                                                     # all unit tests
./mvnw test -Dtest=PokemonResourceTest                          # single test class
./mvnw test -Dtest=PokemonResourceTest#listReturnsDefaultPage   # single test method
./mvnw verify -Dnative                                          # native ITs (slow)
```

`@QuarkusTest` boots the full app per test class. Tests rely on **Quarkus Dev Services**: a Postgres container is started automatically and seeded from `src/test/resources/test-init.sql` (`quarkus.datasource.devservices.init-script-path`). `src/test/resources/application.properties` sets `quarkus.hibernate-orm.schema-management.strategy=none` so Hibernate never tries to manage the externally-defined schema — when adding entities, add their tables to `test-init.sql` rather than relying on JPA DDL.

REST endpoint assertions use RestAssured (`given().when().get(...).then()...`).

## Architecture

Single REST resource (`PokemonResource`, `/api/pokemon`) delegates to a single `PokemonService` that owns all read paths. Packages are flat under `com.zextras.pokemon`:

```
resource/    PokemonResource (only JAX-RS class), PokemonFilter
service/     PokemonService — list, getById, sprite getters, getCard (basic), getFullCard
repository/  10 Panache repositories (Pokémon, sprites, species, evolutions, abilities, ability prose, type efficacy, egg groups, moves, flavor texts)
model/       JPA entities (PokemonOverview, PokemonSprites, Move, Ability, Type, TypeEfficacy, …)
dto/         response shapes: PokemonBasicCard, PokemonCard, AbilityInfo, EvolutionStep, MoveInfo, TypeMatchup
renderer/    PokemonBasicCardRenderer, PokemonCardRenderer — static HTML-by-concatenation renderers
```

`PokemonService` exposes two card builders:
- `getCard(id)` — lightweight `PokemonBasicCard` (overview + sprites) for `/card`.
- `getFullCard(id)` — rich `PokemonCard` for `/card/full`: fans out across the species, evolution, ability, ability-prose, type-efficacy, egg-group, move, and flavor-text repositories. Type matchups are computed in-app by multiplying `damage_factor`s from `type_efficacy` for `type1` × `type2`.

`PokemonResource` is the **only** JAX-RS class. The two HTML endpoints (`/card`, `/card/full`) render via static `PokemonBasicCardRenderer.render(...)` / `PokemonCardRenderer.render(...)` — these build HTML by string concatenation, not templates. Errors are mapped to 404s by `Optional.map(...).orElse(...)` at the resource layer; the service returns `Optional`, never throws for not-found.

### Persistence

- All entities use **Hibernate ORM with Panache** (`PanacheRepositoryBase<Entity, IdType>`), field-based access, public fields.
- `PokemonOverview` is annotated `@Immutable` — it maps the `pokemon_overview` DB view, not a real table. Don't attempt to write to it.
- Composite-key entities use a separate `*Id` class with `@Embeddable` + `@IdClass` (see `PokemonAbility` / `PokemonAbilityId`).
- The schema lives in the external `pokemon-postgres` image. There is no Flyway/Liquibase here. If you change entity mappings, mirror the change in `src/test/resources/test-init.sql` and ensure it matches the upstream DB image.
- Dynamic filtering (see `PokemonRepository.list`) builds a parameterized Panache `find` string — keep this pattern; do **not** concatenate untrusted values into HQL/SQL.

### Filtering & paging

`PokemonFilter` (a `@BeanParam` POJO with `@QueryParam`/`@DefaultValue` fields) carries `page`, `size` (capped at 100 in the repository), `type`, `color`, `legendary`, `mythical`. Bind new query params here rather than adding method params to `PokemonResource`.

## Conventions

- Constructor injection isn't used; the codebase consistently uses field `@Inject` — match the existing style.
- Two-space indentation, public fields on entities/DTOs/filters (Panache idiom), no Lombok.
- Package layout: `resource/` (JAX-RS), `service/`, `repository/`, `model/` (JPA entities), `dto/` (response shapes), `renderer/` (HTML).
- Group ID is `com.zextras`, artifact `pokemon-quarkus`.