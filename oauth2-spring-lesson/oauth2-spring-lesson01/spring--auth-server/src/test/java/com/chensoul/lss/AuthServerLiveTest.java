package com.chensoul.lss;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * Needs the following to be running:
 * - Authorization Server
 */
public class AuthServerLiveTest {

  private static final String USERNAME = "user";
  private static final String PASSWORD = "pass";

  private static final String CLIENT_ID = "client";
  private static final String CLIENT_SECRET = "secret";

  private static final String AUTH_SERVER_BASE_URL = "http://localhost:8083";
  private static final String CLIENT_BASE_URL = "http://localhost:8082/lsso-client";

  private static final String REDIRECT_URL = CLIENT_BASE_URL + "/login/oauth2/code/spring";
  private static final String AUTHORIZE_URL =
          AUTH_SERVER_BASE_URL + "/oauth2/authorize?response_type=code&client_id=client&scope=openid%20read%20write&redirect_uri=" + REDIRECT_URL;
  private static final String TOKEN_URL = AUTH_SERVER_BASE_URL + "/oauth2/token";

  @Test
  public void givenAuthorizationCodeGrant_whenObtainAccessToken_thenSuccess() {
    String accessToken = obtainAccessToken();

    assertThat(accessToken).isNotBlank();
  }

  @Test
  public void whenServiceStarts_thenOidcDiscoveryEndpointIsAvailable() {
    final String oidcDiscoveryUrl = AUTH_SERVER_BASE_URL + "/.well-known/openid-configuration";

    Response response = RestAssured.given()
            .redirects()
            .follow(false)
            .get(oidcDiscoveryUrl);

    assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatusCode());
    System.out.println(response.asString());
    assertThat(response.jsonPath()
            .getMap("$.")).containsKeys("issuer", "authorization_endpoint", "token_endpoint", "userinfo_endpoint");
  }

  private String obtainAccessToken() {
    // obtain authentication url with custom codes
    RequestSpecification mySpec = new RequestSpecBuilder().setUrlEncodingEnabled(false)
            .build();
    Response response = RestAssured.given()
            .spec(mySpec)
            .redirects()
            .follow(false)
            .urlEncodingEnabled(false)
            .header(new Header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"))
            .get(AUTHORIZE_URL);

    String authSessionId = response.getCookie("JSESSIONID");

    String loginLocation = response.getHeader("Location");

    // open login form
    response = RestAssured.given()
            .redirects()
            .follow(false)
            .cookie("JSESSIONID", authSessionId)
            .get(loginLocation);

    String loginResponse = response.asString();
    Pattern pattern = Pattern.compile("<input.*name=\"_csrf\".*\\svalue=\"(.*?)\"\\s+/>");
    Matcher matcher = pattern.matcher(loginResponse);
    String csrf = null;
    if (matcher.find()) {
      csrf = matcher.group(1);
    }

    // obtain authentication code and state
    response = RestAssured.given()
            .redirects()
            .follow(false)
            .cookie("JSESSIONID", authSessionId)
            .formParams("username", USERNAME, "password", PASSWORD, "_csrf", csrf)
            .post(loginLocation);

    assertThat(HttpStatus.FOUND.value()).isEqualTo(response.getStatusCode());

    String location = URLDecoder.decode(response.getHeader(HttpHeaders.LOCATION), StandardCharsets.UTF_8);
    authSessionId = response.getCookie("JSESSIONID");

    // redirect to client url
    response = RestAssured.given()
            .redirects()
            .follow(false)
            .cookie("JSESSIONID", authSessionId)
            .get(location);

    // extract authorization code
    location = response.getHeader(HttpHeaders.LOCATION);
    String code = location.split("code=")[1].split("&")[0];

    // get access token
    Charset charset = StandardCharsets.ISO_8859_1;
    String basicAuth = new String(Base64.getEncoder()
            .encode((CLIENT_ID + ":" + CLIENT_SECRET).getBytes(charset)), charset);

    Map<String, String> params = new HashMap<>();
    params.put("grant_type", AuthorizationGrantType.AUTHORIZATION_CODE.getValue());
    params.put("code", code);
    params.put("redirect_uri", REDIRECT_URL);
    response = RestAssured.given()
            .header("Authorization", "Basic " + basicAuth)
            .queryParams(params)
            .post(TOKEN_URL);

    System.out.println(response.asString());

    return response.jsonPath()
            .getString("access_token");
  }

}
