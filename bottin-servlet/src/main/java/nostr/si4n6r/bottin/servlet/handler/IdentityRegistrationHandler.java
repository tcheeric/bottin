package nostr.si4n6r.bottin.servlet.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import nostr.si4n6r.core.impl.AccountProxy;
import nostr.si4n6r.core.impl.ApplicationProxy;
import nostr.si4n6r.core.impl.BaseActorProxy;
import nostr.si4n6r.storage.Vault;
import nostr.si4n6r.storage.fs.NostrAccountFSVault;
import nostr.si4n6r.util.EncryptionUtil;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ServiceLoader;

@Data
@AllArgsConstructor
public class IdentityRegistrationHandler implements Handler<Handler.Result>{

    private final String npub;
    private final String nsec;
    private final String password;
    private final String appName;
    private final String appPubKey;

    /**
     * @return
     * @throws Exception
     */
    @Override
    public Result handle() throws Exception {
        return handleRegistration();
    }

    private Result handleRegistration() {
        var result = new Result();

        var app = new ApplicationProxy(appPubKey);
        var decodedAppName = URLDecoder.decode(appName, StandardCharsets.UTF_8);
        app.setName(decodedAppName);

        var account = new AccountProxy();
        account.setPrivateKey(nsec);
        account.setPublicKey(npub);
        account.setId(System.currentTimeMillis());
        account.setApplication(app);

        Vault<AccountProxy> vault = getVault();
        ((NostrAccountFSVault)vault).setPassword(password);
        vault.store(account);

        result.setNpub(npub);
        result.setNsec(EncryptionUtil.hashSHA256(nsec));
        result.setPassword(EncryptionUtil.hashSHA256(password));
        result.setResult(Result.RESULT_SUCCESS);

        return result;
    }

    private static Vault getVault(@NonNull String entity) {
        return ServiceLoader
                .load(Vault.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .filter(v -> entity.equals(v.getEntityName()))
                .findFirst()
                .get();
    }

    private Vault<AccountProxy> getVault() {
        return getVault(BaseActorProxy.VAULT_ACTOR_ACCOUNT);
    }
}