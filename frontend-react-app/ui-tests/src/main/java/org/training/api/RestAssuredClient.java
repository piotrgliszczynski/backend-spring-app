package org.training.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.training.exception.RestApiException;

import java.util.Map;

import static io.restassured.RestAssured.given;

@RequiredArgsConstructor
public class RestAssuredClient implements ApiClient {

  private final String baseUrl;

  @Override
  public Response get(String url) throws RestApiException {
    Response response = given()
        .baseUri(baseUrl)
        .log().all()
        .when()
        .get(url)
        .then()
        .log().all()
        .extract().response();

    if (response.getStatusCode() != 200 && response.getStatusCode() != 201) {
      throw new RestApiException("Failed to fetch data, HTTP Status code: " + response.getStatusCode());
    }

    return response;
  }

  @Override
  public Response postJson(String url, Object body) throws RestApiException {

    Response response = given()
        .baseUri(baseUrl)
        .contentType(ContentType.JSON)
        .body(body)
        .log().all()
        .when()
        .post(url)
        .then()
        .log().all()
        .extract().response();

    if (response.getStatusCode() != 200 && response.getStatusCode() != 201) {
      throw new RestApiException("Failed to fetch data, HTTP Status code: " + response.getStatusCode());
    }

    return response;
  }

  @Override
  public void delete(String url, Map<String, String> pathParams) throws RestApiException {

    Response response = given()
        .baseUri(baseUrl)
        .pathParams(pathParams)
        .log().all()
        .when()
        .delete(url)
        .then()
        .log().all()
        .extract().response();

    if (response.getStatusCode() != 200 && response.getStatusCode() != 201) {
      throw new RestApiException("Failed to fetch data, HTTP Status code: " + response.getStatusCode());
    }
  }


}
