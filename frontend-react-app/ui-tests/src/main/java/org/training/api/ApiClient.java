package org.training.api;

import io.restassured.response.Response;
import org.training.exception.RestApiException;

import java.util.Map;

public interface ApiClient {

  //Response get(String url);

  Response get(String url) throws RestApiException;

  Response postJson(String url, Object body) throws RestApiException;

  void delete(String url, Map<String, String> pathParams) throws RestApiException;
}
