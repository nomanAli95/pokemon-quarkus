-- ============================================================
-- SCHEMA
-- ============================================================

CREATE TABLE generations (
    id          INTEGER PRIMARY KEY,
    main_region TEXT,
    identifier  TEXT NOT NULL
);

CREATE TABLE types (
    id            INTEGER PRIMARY KEY,
    identifier    TEXT NOT NULL,
    generation_id INTEGER REFERENCES generations(id),
    damage_class  TEXT
);

CREATE TABLE pokemon_species (
    id                       INTEGER PRIMARY KEY,
    identifier               TEXT NOT NULL,
    generation_id            INTEGER REFERENCES generations(id),
    evolves_from_species_id  INTEGER REFERENCES pokemon_species(id),
    evolution_chain_id       INTEGER,
    color                    TEXT,
    shape                    TEXT,
    habitat                  TEXT,
    gender_rate              INTEGER,
    capture_rate             INTEGER,
    base_happiness           INTEGER,
    is_baby                  BOOLEAN NOT NULL DEFAULT FALSE,
    hatch_counter            INTEGER,
    has_gender_differences   BOOLEAN NOT NULL DEFAULT FALSE,
    growth_rate              TEXT,
    forms_switchable         BOOLEAN NOT NULL DEFAULT FALSE,
    is_legendary             BOOLEAN NOT NULL DEFAULT FALSE,
    is_mythical              BOOLEAN NOT NULL DEFAULT FALSE,
    sort_order               INTEGER,
    conquest_order           INTEGER,
    genus                    TEXT
);

CREATE TABLE pokemon (
    id              INTEGER PRIMARY KEY,
    identifier      TEXT NOT NULL,
    species_id      INTEGER REFERENCES pokemon_species(id),
    height          INTEGER,
    weight          INTEGER,
    base_experience INTEGER,
    sort_order      INTEGER,
    is_default      BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE pokemon_stats (
    pokemon_id  INTEGER REFERENCES pokemon(id),
    stat_id     INTEGER CHECK (stat_id BETWEEN 1 AND 6),
    base_stat   INTEGER NOT NULL,
    effort      INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (pokemon_id, stat_id)
);

CREATE TABLE pokemon_types (
    pokemon_id  INTEGER REFERENCES pokemon(id),
    slot        INTEGER CHECK (slot IN (1, 2)),
    type_id     INTEGER REFERENCES types(id),
    PRIMARY KEY (pokemon_id, slot)
);

CREATE TABLE abilities (
    id             INTEGER PRIMARY KEY,
    identifier     TEXT NOT NULL,
    generation_id  INTEGER REFERENCES generations(id),
    is_main_series BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE pokemon_abilities (
    pokemon_id  INTEGER REFERENCES pokemon(id),
    slot        INTEGER NOT NULL,
    ability_id  INTEGER REFERENCES abilities(id),
    is_hidden   BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (pokemon_id, slot)
);

CREATE TABLE moves (
    id                      INTEGER PRIMARY KEY,
    identifier              TEXT NOT NULL,
    name                    TEXT,
    generation_id           INTEGER REFERENCES generations(id),
    type_id                 INTEGER REFERENCES types(id),
    power                   INTEGER,
    pp                      INTEGER,
    accuracy                INTEGER,
    priority                INTEGER NOT NULL DEFAULT 0,
    target                  TEXT,
    damage_class            TEXT,
    effect_id               INTEGER,
    effect_chance           INTEGER,
    contest_type_id         INTEGER,
    contest_effect_id       INTEGER,
    super_contest_effect_id INTEGER
);

CREATE TABLE pokemon_sprites (
    pokemon_id           INTEGER PRIMARY KEY REFERENCES pokemon(id),
    front_default        BYTEA,
    front_shiny          BYTEA,
    back_default         BYTEA,
    official_artwork_url TEXT
);

CREATE TABLE egg_groups (
    id         INTEGER PRIMARY KEY,
    identifier TEXT NOT NULL
);

CREATE TABLE pokemon_egg_groups (
    species_id   INTEGER REFERENCES pokemon_species(id),
    egg_group_id INTEGER REFERENCES egg_groups(id),
    PRIMARY KEY (species_id, egg_group_id)
);

CREATE TABLE type_efficacy (
    damage_type_id INTEGER REFERENCES types(id),
    target_type_id INTEGER REFERENCES types(id),
    damage_factor  INTEGER NOT NULL,
    PRIMARY KEY (damage_type_id, target_type_id)
);

CREATE TABLE ability_prose (
    ability_id   INTEGER PRIMARY KEY REFERENCES abilities(id),
    short_effect TEXT
);

CREATE TABLE pokemon_flavor_text (
    species_id  INTEGER REFERENCES pokemon_species(id),
    version_id  INTEGER NOT NULL,
    flavor_text TEXT NOT NULL,
    PRIMARY KEY (species_id, version_id)
);

CREATE TABLE pokemon_evolution (
    id                      INTEGER PRIMARY KEY,
    evolved_species_id      INTEGER REFERENCES pokemon_species(id),
    evolution_trigger       TEXT,
    minimum_level           INTEGER,
    trigger_item            TEXT,
    held_item               TEXT,
    time_of_day             TEXT,
    minimum_happiness       INTEGER,
    minimum_beauty          INTEGER,
    minimum_affection       INTEGER,
    known_move_id           INTEGER REFERENCES moves(id),
    trade_species_id        INTEGER REFERENCES pokemon_species(id),
    relative_physical_stats INTEGER,
    needs_overworld_rain    BOOLEAN NOT NULL DEFAULT FALSE,
    turn_upside_down        BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE pokemon_moves (
    pokemon_id       INTEGER REFERENCES pokemon(id),
    version_group_id INTEGER NOT NULL,
    move_id          INTEGER REFERENCES moves(id),
    learn_method     TEXT NOT NULL,
    level            INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (pokemon_id, version_group_id, move_id, learn_method)
);

CREATE VIEW pokemon_overview AS
SELECT
    p.id,
    p.identifier                                            AS name,
    p.height,
    p.weight,
    p.base_experience,
    ps.color,
    ps.shape,
    ps.habitat,
    ps.genus,
    ps.gender_rate,
    ps.capture_rate,
    ps.base_happiness,
    ps.is_baby,
    ps.hatch_counter,
    ps.growth_rate,
    ps.is_legendary,
    ps.is_mythical,
    ps.evolves_from_species_id,
    ps.evolution_chain_id,
    g.identifier                                            AS generation,
    g.main_region                                           AS region,
    t1.identifier                                           AS type1,
    t2.identifier                                           AS type2,
    MAX(CASE WHEN pst.stat_id = 1 THEN pst.base_stat END)  AS hp,
    MAX(CASE WHEN pst.stat_id = 2 THEN pst.base_stat END)  AS attack,
    MAX(CASE WHEN pst.stat_id = 3 THEN pst.base_stat END)  AS defense,
    MAX(CASE WHEN pst.stat_id = 4 THEN pst.base_stat END)  AS sp_attack,
    MAX(CASE WHEN pst.stat_id = 5 THEN pst.base_stat END)  AS sp_defense,
    MAX(CASE WHEN pst.stat_id = 6 THEN pst.base_stat END)  AS speed,
    SUM(pst.base_stat)                                      AS base_stat_total,
    MAX(CASE WHEN pst.stat_id = 1 THEN pst.effort END)     AS ev_hp,
    MAX(CASE WHEN pst.stat_id = 2 THEN pst.effort END)     AS ev_attack,
    MAX(CASE WHEN pst.stat_id = 3 THEN pst.effort END)     AS ev_defense,
    MAX(CASE WHEN pst.stat_id = 4 THEN pst.effort END)     AS ev_sp_attack,
    MAX(CASE WHEN pst.stat_id = 5 THEN pst.effort END)     AS ev_sp_defense,
    MAX(CASE WHEN pst.stat_id = 6 THEN pst.effort END)     AS ev_speed,
    spr.front_default,
    spr.official_artwork_url
FROM pokemon p
JOIN pokemon_species ps         ON ps.id = p.species_id
LEFT JOIN generations g         ON g.id = ps.generation_id
LEFT JOIN pokemon_types pt1     ON pt1.pokemon_id = p.id AND pt1.slot = 1
LEFT JOIN types t1              ON t1.id = pt1.type_id
LEFT JOIN pokemon_types pt2     ON pt2.pokemon_id = p.id AND pt2.slot = 2
LEFT JOIN types t2              ON t2.id = pt2.type_id
LEFT JOIN pokemon_stats pst     ON pst.pokemon_id = p.id
LEFT JOIN pokemon_sprites spr   ON spr.pokemon_id = p.id
GROUP BY
    p.id, p.identifier, p.height, p.weight, p.base_experience,
    ps.color, ps.shape, ps.habitat, ps.genus, ps.gender_rate, ps.capture_rate,
    ps.base_happiness, ps.is_baby, ps.hatch_counter, ps.growth_rate,
    ps.is_legendary, ps.is_mythical, ps.evolves_from_species_id, ps.evolution_chain_id,
    g.identifier, g.main_region,
    t1.identifier, t2.identifier,
    spr.front_default, spr.official_artwork_url;

-- ============================================================
-- TEST DATA — Pikachu (id=25)
-- ============================================================

INSERT INTO generations (id, main_region, identifier)
VALUES (1, 'kanto', 'generation-i');

INSERT INTO types (id, identifier, generation_id, damage_class)
VALUES (13, 'electric', 1, 'special');

INSERT INTO pokemon_species (id, identifier, generation_id, evolves_from_species_id, evolution_chain_id,
    color, shape, habitat, gender_rate, capture_rate, base_happiness, is_baby, hatch_counter,
    has_gender_differences, growth_rate, forms_switchable, is_legendary, is_mythical, sort_order, genus)
VALUES (25, 'pikachu', 1, NULL, NULL,
    'yellow', 'quadruped', 'forest', 4, 190, 70, false, 10,
    true, 'medium', false, false, false, 25, 'Mouse Pokémon');

INSERT INTO pokemon (id, identifier, species_id, height, weight, base_experience, sort_order, is_default)
VALUES (25, 'pikachu', 25, 4, 60, 112, 35, true);

INSERT INTO pokemon_stats (pokemon_id, stat_id, base_stat, effort) VALUES
    (25, 1, 35, 0),
    (25, 2, 55, 0),
    (25, 3, 40, 0),
    (25, 4, 50, 0),
    (25, 5, 50, 0),
    (25, 6, 90, 2);

INSERT INTO pokemon_types (pokemon_id, slot, type_id)
VALUES (25, 1, 13);

INSERT INTO abilities (id, identifier, generation_id, is_main_series)
VALUES (9, 'static', 1, true);

INSERT INTO pokemon_abilities (pokemon_id, slot, ability_id, is_hidden)
VALUES (25, 1, 9, false);

INSERT INTO ability_prose (ability_id, short_effect)
VALUES (9, 'May cause paralysis on contact.');

INSERT INTO egg_groups (id, identifier)
VALUES (2, 'field');

INSERT INTO pokemon_egg_groups (species_id, egg_group_id)
VALUES (25, 2);

INSERT INTO pokemon_flavor_text (species_id, version_id, flavor_text)
VALUES (25, 1, 'When several of these POKéMON gather, their electricity can cause lightning storms.');

-- Minimal 1x1 white PNG for sprite test
INSERT INTO pokemon_sprites (pokemon_id, front_default, official_artwork_url)
VALUES (25,
    decode('iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAAAAAA6fptVAAAACklEQVQI12NgAAAAAgAB4iG8MwAAAABJRU5ErkJggg==', 'base64'),
    'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png');