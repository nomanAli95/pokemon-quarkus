# pokemon-quarkus

A Quarkus REST backend that exposes a complete Pokédex as a JSON API with image support. It connects to the [pokemon-postgres](https://github.com/nomanAli95/pokemon-postgres) database, which pre-seeds all Pokémon data and sprites into PostgreSQL.

| Bulbasaur | Charmander | Squirtle |
|:---------:|:----------:|:--------:|
| ![Bulbasaur](https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png) | ![Charmander](https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/4.png) | ![Squirtle](https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/7.png) |

---

## API endpoints

Base path: `/api/pokemon`
Swagger UI: `http://localhost:8080/swagger-ui`

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/pokemon` | List Pokémon (default: page 0, 20 per page) |
| `GET` | `/api/pokemon?page={n}&size={n}` | Paginate (size capped at 100) |
| `GET` | `/api/pokemon?type={type}` | Filter by type (e.g. `fire`, `water`) |
| `GET` | `/api/pokemon?color={color}` | Filter by color (e.g. `red`, `blue`) |
| `GET` | `/api/pokemon?legendary=true` | Filter legendary Pokémon |
| `GET` | `/api/pokemon?mythical=true` | Filter mythical Pokémon |
| `GET` | `/api/pokemon/{id}` | Get a single Pokémon by ID (includes `official_artwork_url`) |
| `GET` | `/api/pokemon/{id}/sprite` | Front-facing pixel sprite (`image/png`) |
| `GET` | `/api/pokemon/{id}/sprite/shiny` | Shiny front-facing sprite (`image/png`) |
| `GET` | `/api/pokemon/{id}/sprite/back` | Back-facing sprite (`image/png`) |
| `GET` | `/api/pokemon/{id}/card` | Basic HTML card — name, types, sprite, base stats |
| `GET` | `/api/pokemon/{id}/card/full` | Full HTML card — all data, open in a browser |

The full card includes:
- **Header** — name, genus, type badges, legendary/mythical/baby flags
- **Flavor text** — latest Pokédex entry
- **Artwork & sprites** — official artwork + front/back/shiny pixel sprites
- **Identity** — ID, generation, region, color, shape, habitat
- **Physical** — height, weight, base experience, capture rate, base happiness, hatch counter, gender rate, growth rate
- **Type matchups** — color-coded effectiveness chips (immune/¼×/½×/2×/4×) for the Pokémon's type combination
- **Evolution chain** — full line with evolution conditions (level, item, happiness, trade, etc.)
- **Base stats** — HP, Attack, Defense, Sp. Atk, Sp. Def, Speed with bars and total
- **EV yield** — effort value gains per stat
- **Abilities** — name, hidden flag, short effect description
- **Moves** — full moveset from the latest game version, grouped by learn method (level-up, TM, egg, tutor)

### Quick visual test

Open any of these in a browser after starting the app:

```
http://localhost:8080/api/pokemon/25/card           ← Pikachu basic card
http://localhost:8080/api/pokemon/25/card/full      ← Pikachu full card
http://localhost:8080/api/pokemon/25/sprite         ← pixel sprite (PNG)
http://localhost:8080/api/pokemon/249/card/full     ← Lugia full card (dual-type matchups)
```

---

## Run with Docker Compose

The included `docker-compose.yml` starts both the database ([pokemon-postgres](https://github.com/nomanAli95/pokemon-postgres)) and this app together:

```bash
docker compose up --build
```

The DB image is pre-seeded and starts instantly. The app waits for the DB to be healthy before starting.

---

## Run manually with Docker

```bash
# 1. Start the database
docker run -d \
  --name pokedex-db \
  -e POSTGRES_USER=ash \
  -e POSTGRES_PASSWORD=pikachu \
  -e POSTGRES_DB=pokedex \
  -p 5432:5432 \
  nomanali95/pokemon-postgres:latest

# 2. Build and run the app
docker build -t pokemon-quarkus .
docker run -d \
  --name pokemon-quarkus \
  -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://host.docker.internal:5432/pokedex \
  -e QUARKUS_DATASOURCE_USERNAME=ash \
  -e QUARKUS_DATASOURCE_PASSWORD=pikachu \
  -p 8080:8080 \
  pokemon-quarkus
```

---

## Running in dev mode

Live coding with hot reload:

```bash
./mvnw quarkus:dev
```

> Dev UI available at `http://localhost:8080/q/dev/` in dev mode only.

---

## Packaging

```bash
# Standard JAR
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar

# Über-JAR (self-contained)
./mvnw package -Dquarkus.package.jar.type=uber-jar
java -jar target/*-runner.jar
```

---

## Native executable

```bash
# Requires local GraalVM
./mvnw package -Dnative

# Without local GraalVM (builds inside a container using Mandrel)
./mvnw package -Dnative -Dquarkus.native.container-build=true

./target/pokemon-quarkus-1.0.0-SNAPSHOT-runner
```

### Native with Docker

`Dockerfile.native` uses the official Quarkus Mandrel builder image — no local GraalVM needed. The resulting image is significantly smaller than the JVM one.

**With Docker Compose** (recommended — reuses the same DB setup):
```bash
docker compose -f docker-compose.yml -f docker-compose.native.yml up --build
```

**Manually:**
```bash
docker build -f Dockerfile.native -t pokemon-quarkus-native .
docker run -d \
  --name pokemon-quarkus-native \
  -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://host.docker.internal:5432/pokedex \
  -e QUARKUS_DATASOURCE_USERNAME=ash \
  -e QUARKUS_DATASOURCE_PASSWORD=pikachu \
  -p 8080:8080 \
  pokemon-quarkus-native
```

> **Note:** Native builds take significantly longer than JVM builds (several minutes).

---

## Tech stack

- [Quarkus](https://quarkus.io/) — Supersonic Subatomic Java
- [Hibernate ORM with Panache](https://quarkus.io/guides/hibernate-orm-panache) — simplified persistence
- [SmallRye OpenAPI](https://quarkus.io/guides/openapi-swaggerui) — Swagger UI
- [PostgreSQL JDBC](https://quarkus.io/guides/datasource) — database connectivity
