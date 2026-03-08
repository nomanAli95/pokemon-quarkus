package com.zextras;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
class PokemonResourceTest {

  @Test
  void testListReturnsDefaultPage() {
    given()
            .when().get("/api/pokemon")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
  }

  @Test
  void testListPagination() {
    given()
            .queryParam("page", 0)
            .queryParam("size", 5)
            .when().get("/api/pokemon")
            .then()
            .statusCode(200)
            .body("size()", is(5));
  }

  @Test
  void testFilterByType() {
    given()
            .queryParam("type", "fire")
            .when().get("/api/pokemon")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
  }

  @Test
  void testFilterByLegendary() {
    given()
            .queryParam("legendary", true)
            .when().get("/api/pokemon")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
  }

  @Test
  void testGetByIdReturnsCorrectPokemon() {
    given()
            .when().get("/api/pokemon/25")
            .then()
            .statusCode(200)
            .body("name", is("pikachu"));
  }

  @Test
  void testGetByIdNotFound() {
    given()
            .when().get("/api/pokemon/99999")
            .then()
            .statusCode(404);
  }

  @Test
  void testSpriteReturnsImage() {
    given()
            .when().get("/api/pokemon/25/sprite")
            .then()
            .statusCode(200)
            .contentType("image/png");
  }

  @Test
  void testSpriteMissingReturns404() {
    given()
            .when().get("/api/pokemon/99999/sprite")
            .then()
            .statusCode(404);
  }

  @Test
  void testCardReturnsHtml() {
    given()
            .when().get("/api/pokemon/25/card")
            .then()
            .statusCode(200)
            .contentType("text/html");
  }
}
