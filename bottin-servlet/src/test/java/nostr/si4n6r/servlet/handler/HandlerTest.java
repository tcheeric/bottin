package nostr.si4n6r.servlet.handler;

import nostr.base.PublicKey;
import nostr.id.Identity;
import nostr.si4n6r.bottin.servlet.handler.AppRegistrationHandler;
import nostr.si4n6r.bottin.servlet.handler.IdentityRegistrationHandler;
import nostr.si4n6r.bottin.servlet.handler.Handler;
import nostr.si4n6r.core.impl.AccountProxy;
import nostr.si4n6r.core.impl.ApplicationProxy;
import nostr.si4n6r.core.impl.Principal;
import nostr.si4n6r.core.impl.SecurityManager;
import nostr.si4n6r.util.EncryptionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @author eric
 */
public class HandlerTest {

    @Test
    @DisplayName("Register a new account")
    public void registerAccount() throws Exception {

        var applicationIdentity = Identity.generateRandomIdentity();
        final String appPublicKey = applicationIdentity.getPublicKey().toString();

        ApplicationProxy.ApplicationTemplate template = new ApplicationProxy.ApplicationTemplate();
        template.setName("Test_Application_" + System.currentTimeMillis());
        template.setUrl("https://github.com/tcheeric/demo-nip46-app");
        template.setDescription("This is a test application");
        template.setIcons(Arrays.stream(new String[]{"https://raw.githubusercontent.com/mbarulli/nostr-logo/main/PNG/nostr-icon-purple-256x256.png"}).toList());

        AppRegistrationHandler handler = new AppRegistrationHandler(appPublicKey, template);
        var result = handler.handle();

        assertEquals(Handler.Result.RESULT_SUCCESS, result.getResult());

        var applicationProxy = new ApplicationProxy(appPublicKey, template);
        applicationProxy.setName(template.getName());

        var accountIdentity = Identity.generateRandomIdentity();
        final String accountPrivateKey = accountIdentity.getPrivateKey().toString();
        final String accountPublicKey = accountIdentity.getPublicKey().toString();

        var accountProxy = new AccountProxy();
        accountProxy.setPrivateKey(accountPrivateKey);
        accountProxy.setPublicKey(accountPublicKey);
        accountProxy.setApplication(applicationProxy);

        var securityManager = SecurityManager.getInstance();

        Assertions.assertThrows(RuntimeException.class, () -> securityManager.getPrincipal(new PublicKey(applicationProxy.getPublicKey())), "Principal not found!");

        var userRegistrationHandler = new IdentityRegistrationHandler(accountPublicKey, accountPrivateKey, "password", applicationProxy.getName(), applicationProxy.getPublicKey());
        result = userRegistrationHandler.handle();

        assertEquals(Handler.Result.RESULT_SUCCESS, result.getResult());
        assertEquals(accountPublicKey, result.getNpub());
        assertEquals(EncryptionUtil.hashSHA256(accountPrivateKey), result.getNsec());
        assertEquals(EncryptionUtil.hashSHA256("password"), result.getPassword());
    }

    @Test
    @DisplayName("Add a principal to the security manager")
    public void getPrincipal() {
        var applicationIdentity = Identity.generateRandomIdentity();
        final String appPublicKey = applicationIdentity.getPublicKey().toString();

        ApplicationProxy.ApplicationTemplate template = new ApplicationProxy.ApplicationTemplate();
        template.setName("Test Application");
        template.setUrl("https://github.com/tcheeric/demo-nip46-app");
        template.setDescription("This is a test application");
        template.setIcons(Arrays.stream(new String[]{"https://raw.githubusercontent.com/mbarulli/nostr-logo/main/PNG/nostr-icon-purple-256x256.png"}).toList());

        var applicationProxy = new ApplicationProxy(appPublicKey, template);
        applicationProxy.setName("Test Application_" + System.currentTimeMillis());

        var securityManager = SecurityManager.getInstance();

        Assertions.assertThrows(RuntimeException.class, () -> securityManager.getPrincipal(new PublicKey(applicationProxy.getPublicKey())), "Principal not found!");

        securityManager.addPrincipal(Principal.getInstance(new PublicKey(applicationProxy.getPublicKey()), "password"));
        var principal = securityManager.getPrincipal(new PublicKey(applicationProxy.getPublicKey()));

        Assertions.assertNotNull(principal);

    }

}
