package ru.jakesmokie.spectre.beans;

import com.iplanet.sso.SSOException;
import com.iplanet.sso.SSOToken;
import com.iplanet.sso.SSOTokenManager;
import com.sun.identity.authentication.AuthContext;
import com.sun.identity.authentication.spi.AuthLoginException;
import lombok.SneakyThrows;
import lombok.val;

import javax.ejb.Stateless;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;

@Stateless
public class AuthenticationService {

    @SneakyThrows
    public SSOToken login(String login, String password, String realm) {
        AuthContext lc = getAuthContext(login, password, realm);
        return lc.getSSOToken();
    }

    @SneakyThrows
    public Boolean isValidToken(String token) {
        final val manager = SSOTokenManager.getInstance();

        try {
            return manager.isValidToken(manager.createSSOToken(token));
        } catch (SSOException e) {
            return false;
        }
    }

    private AuthContext getAuthContext(String login, String password, String realm)
            throws AuthLoginException {
        AuthContext lc = new AuthContext(realm);
        lc.login(AuthContext.IndexType.MODULE_INSTANCE, "DataStore");

        Callback[] requirements = lc.getRequirements();

        for (int i = 0; i < requirements.length; i++) {
            Callback callback = requirements[i];
            if (callback instanceof NameCallback) {
                NameCallback name = (NameCallback) callback;
                name.setName(login);
            } else if (callback instanceof PasswordCallback) {
                PasswordCallback pass = (PasswordCallback) callback;
                pass.setPassword(password.toCharArray());
            }
        }

        lc.submitRequirements(requirements);
        return lc;
    }

    @SneakyThrows
    public void logout(String token) {
        final val manager = SSOTokenManager.getInstance();
        final val ssoToken = manager.createSSOToken(token);

        manager.logout(ssoToken);
    }
}
