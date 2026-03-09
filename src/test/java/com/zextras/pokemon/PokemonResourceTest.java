package com.zextras.pokemon;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
class PokemonResourceTest {

  @Test
  void listReturnsDefaultPage() {
    given()
        .when().get("/api/pokemon")
        .then()
        .statusCode(200)
        .body("size()", greaterThan(0));
  }

  @Test
  void getByIdReturnsCorrectPokemon() {
    given()
        .when().get("/api/pokemon/25")
        .then()
        .statusCode(200)
        .body("name", is("pikachu"));
  }

  @Test
  void getByIdNotFoundReturns404() {
    given()
        .when().get("/api/pokemon/99999")
        .then()
        .statusCode(404);
  }

  @Test
  void spriteReturnsImage() {
    given()
        .when().get("/api/pokemon/25/sprite")
        .then()
        .statusCode(200)
        .contentType("image/png");
  }

  @Test
  void basicCardReturnsHtml() {
    given()
        .when().get("/api/pokemon/25/card")
        .then()
        .statusCode(200)
        .contentType("text/html");
  }

  @Test
  void fullCardReturnsHtml() {
    given()
        .when().get("/api/pokemon/25/card/full")
        .then()
        .statusCode(200)
        .contentType("text/html");
  }
}
