package com.automation.api;

import com.automation.retry.ConfigurableRetryAnalyzer;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

public class ApiTest {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @Test(retryAnalyzer = ConfigurableRetryAnalyzer.class, groups = "api")
    public void getPostByIdShouldReturnExpectedPost() {
        given()
                .baseUri(BASE_URL)
                .pathParam("id", 1)
        .when()
                .get("/posts/{id}")
        .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("title", equalTo("sunt aut facere repellat provident occaecati excepturi optio reprehenderit"));
    }

    @Test(retryAnalyzer = ConfigurableRetryAnalyzer.class, groups = "api")
    public void getUserByIdShouldReturnUserDetails() {
        given()
                .baseUri(BASE_URL)
                .pathParam("id", 1)
        .when()
                .get("/users/{id}")
        .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("username", equalTo("Bret"))
                .body("email", notNullValue());
    }

    @Test(retryAnalyzer = ConfigurableRetryAnalyzer.class, groups = "api")
    public void getCommentsForPostShouldReturnRelatedComments() {
        given()
                .baseUri(BASE_URL)
                .queryParam("postId", 1)
        .when()
                .get("/comments")
        .then()
                .statusCode(200)
                .body("", hasSize(5))
                .body("postId", everyItem(equalTo(1)));
    }

    @Test(retryAnalyzer = ConfigurableRetryAnalyzer.class, groups = "api")
    public void createPostShouldReturnCreatedPost() {
        given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(Map.of(
                        "title", "Test post",
                        "body", "Test post body",
                        "userId", 1))
        .when()
                .post("/posts")
        .then()
                .statusCode(201)
                .body("id", equalTo(101))
                .body("title", equalTo("Test post"))
                .body("body", equalTo("Test post body"))
                .body("userId", equalTo(1));
    }

    @Test(retryAnalyzer = ConfigurableRetryAnalyzer.class, groups = "api")
    public void updatePostShouldReturnUpdatedPost() {
        given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .pathParam("id", 1)
                .body(Map.of(
                        "id", 1,
                        "title", "Updated test post",
                        "body", "Updated test post body",
                        "userId", 1))
        .when()
                .put("/posts/{id}")
        .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", equalTo("Updated test post"))
                .body("body", equalTo("Updated test post body"))
                .body("userId", equalTo(1));
    }

    @Test(retryAnalyzer = ConfigurableRetryAnalyzer.class, groups = "api")
    public void deletePostShouldReturnEmptyObject() {
        given()
                .baseUri(BASE_URL)
                .pathParam("id", 1)
        .when()
                .delete("/posts/{id}")
        .then()
                .statusCode(200)
                .body("$", anEmptyMap());
    }
}
