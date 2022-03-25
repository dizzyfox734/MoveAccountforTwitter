package com.my.MoveAccountforTwitter.springboot.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.social.connect.Connection;
import org.springframework.social.oauth1.*;
import org.springframework.social.twitter.api.*;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    private static String clientId, clientSecret, callbackUrl;

    @Autowired
    private Environment env;

    @PostConstruct
    private void init() {
        clientId = env.getProperty("twitter.clientId");
        clientSecret = env.getProperty("twitter.clientSecret");
        callbackUrl = env.getProperty("twitter.callbackUrl");
    }

    @GetMapping("/oauth2/authorize/normal/twitter")
    public void oauthLogin(HttpServletResponse response, HttpServletRequest request) throws IOException {
        OAuth1Operations oauth1Operations = new TwitterConnectionFactory(clientId,clientSecret).getOAuthOperations();
        OAuthToken oauthToken = oauth1Operations.fetchRequestToken(callbackUrl, null);
        String authorizeUrl = oauth1Operations.buildAuthorizeUrl(oauthToken.getValue(), OAuth1Parameters.NONE);


        request.getServletContext().setAttribute("token", oauthToken);
        response.sendRedirect(authorizeUrl);
    }

    @GetMapping(value = "/login/twitter")
    public String twitterComplete(HttpServletResponse response, HttpServletRequest request, @RequestParam(name = "oauth_token") String oauthToken, @RequestParam(name = "oauth_verifier") String oauthVerifier) {
        Connection<Twitter> connection = getAccessTokenToConnection(response, request, oauthVerifier);


        Map<String, String> map = getUserInfoMap(connection);

        String id = getUserInfoMap(connection).get("id");
        Cookie cookieID = new Cookie("id", id);
        cookieID.setPath("/");
        response.addCookie(cookieID);

        return "fileLoad";
    }

    private Connection<Twitter> getAccessTokenToConnection(HttpServletResponse response, HttpServletRequest request, @RequestParam(name = "oauth_verifier") String oauthVerifier) {
        TwitterConnectionFactory twitterConnectionFactory = new TwitterConnectionFactory(clientId, clientSecret);
        OAuth1Operations operations = twitterConnectionFactory.getOAuthOperations();
        OAuthToken requestToken = (OAuthToken)request.getServletContext().getAttribute("token");

        request.getServletContext().removeAttribute("token");
        OAuthToken accessToken = operations.exchangeForAccessToken(new AuthorizedRequestToken(requestToken, oauthVerifier), null);



        saveOauthTokeninCookie(response, accessToken);

        return twitterConnectionFactory.createConnection(accessToken);
    }

    private Map<String, String> getUserInfoMap(Connection<Twitter> connection) {
        Map<String, String> map = new HashMap<>();
        String userPrincipal = connection.getKey().getProviderUserId();
        String userName = connection.getDisplayName();


        map.put("name", userName);
        map.put("id", userPrincipal);
        return map;
    }

    private void saveOauthTokeninCookie(HttpServletResponse response, OAuthToken accessToken) {
        Cookie oauth = new Cookie("token", accessToken.getValue());
        Cookie secret = new Cookie("tokensc", accessToken.getSecret());
        oauth.setPath("/");
        secret.setPath("/");
        response.addCookie(oauth);
        response.addCookie(secret);
    }

}
