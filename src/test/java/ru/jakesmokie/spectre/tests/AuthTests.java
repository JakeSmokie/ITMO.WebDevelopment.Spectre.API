package ru.jakesmokie.spectre.tests;

import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.jakesmokie.spectre.beans.AuthenticationService;
import ru.jakesmokie.spectre.restapi.auth.AuthenticationResource;

public class AuthTests {
    private static AuthenticationResource resource;
    private static AuthenticationService authenticationService;

    @BeforeClass
    @SneakyThrows
    public static void setUp() {
        authenticationService = new AuthenticationService();
        resource = new AuthenticationResource();
//        resource.setAuthenticationService(authenticationService);
    }

    @Test
    public void testSuccessfulLogin() {
        final String token = loginAndGetTokenID();
        Assert.assertTrue(authenticationService.isValidToken(token));
    }

    @Test
    public void testValidToken() {
        final String token = loginAndGetTokenID();
        Assert.assertTrue(resource.isAuthorized(token).getIsAuthorized());
    }

    @Test
    public void testInvalidToken() {
        final String token = "xsdjahsudgsauydg";
        Assert.assertFalse(resource.isAuthorized(token).getIsAuthorized());
    }

    @Test
    public void testLogout() {
        final String token = loginAndGetTokenID();
        Assert.assertTrue(authenticationService.isValidToken(token));
        resource.logout(token);
        Assert.assertFalse(authenticationService.isValidToken(token));
    }

    private String loginAndGetTokenID() {
        return resource.login("1", "12345678", "/keykeepers")
                .getToken();
    }
}
