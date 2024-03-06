package nostr.bottin.servlet.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import nostr.base.PublicKey;
import nostr.si4n6r.util.EncryptionUtil;

@Data
@AllArgsConstructor
public class AuthenticationHandler implements Handler<Handler.Result> {

    private final String name;
    private final String npub;
    private final String password;

    @Override
    public Result handle() throws Exception {
        return handleAuthentication();
    }

    private Result handleAuthentication() throws Exception {
        var authResult = new Result();
        authResult.setNpub(npub);
        authResult.setHashedPassword(EncryptionUtil.hashSHA256(password));

        EncryptionUtil.decryptNsec(name, new PublicKey(npub), password);

        authResult.setResult(Result.RESULT_SUCCESS);
        return authResult;
    }
}