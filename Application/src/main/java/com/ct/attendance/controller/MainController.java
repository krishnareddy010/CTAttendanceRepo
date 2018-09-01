package com.ct.attendance.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ct.app.attendance.auth.AuthHelper;
import com.ct.app.attendance.auth.IdToken;
import com.ct.app.attendance.auth.TokenResponse;

@RestController
public class MainController {

    UUID states = UUID.randomUUID();
    UUID nonce = UUID.randomUUID();

    @RequestMapping ( "/index" )
    public String index() {
        String loginUrl = AuthHelper.getLoginUrl( states, nonce );
        System.out.println( "Login URL: " + loginUrl );
        return loginUrl;
    }

    @RequestMapping ( value = "/authorize", method = RequestMethod.POST )
    public ResponseEntity<String> authorize( @RequestParam ( "code" ) String code, @RequestParam ( "id_token" ) String idToken, @RequestParam ( "state" ) UUID state, HttpServletRequest request ) {
        // Get the expected state value from the session
        System.out.println( "This is called!!!" );
        System.out.println( "Token is: " + idToken );
        System.out.println( "Code is: " + code );
        System.out.println( "State is: " + state.toString() );
        // Make sure that the state query parameter returned matches
        // the expected state

        IdToken idTokenObj = IdToken.parseEncodedToken( idToken, nonce.toString() );
        if ( idTokenObj != null ) {
            TokenResponse tokenResponse = AuthHelper.getTokenFromAuthCode( code, idTokenObj.getTenantId() );
            System.out.println( "Token from AuthCode: " + tokenResponse.getAccessToken() );
            // Able to access token now.. Need to store access token and evaluate expiry of token for subsequent
            // requests for an hour
            // as Microsoft tokens are hourly lived and if expired get refresh token and update in SQL lite in android.
            return new ResponseEntity<String>( new JSONObject().put( "authenticated", true ).putOnce( "token", tokenResponse.getAccessToken() ).toString(), HttpStatus.OK );
        }

        return new ResponseEntity<String>( new JSONObject().put( "authenticated", false ).toString(), HttpStatus.FORBIDDEN );
    }

}
