package nostr.si4n6r.bottin.servlet.handler;

import lombok.AllArgsConstructor;
import nostr.si4n6r.core.impl.ApplicationProxy;
import nostr.si4n6r.storage.fs.NostrApplicationFSVault;

@AllArgsConstructor
public class AppRegistrationHandler implements Handler<Handler.Result>{

    private final String publicKey;
    private final ApplicationProxy.ApplicationTemplate template;

    /**
     * @return
     */
    @Override
    public Result handle() {
        var vault = new NostrApplicationFSVault();
        var actor = new ApplicationProxy(publicKey, template);
        vault.store(actor);

        var result = new Result();
        result.setResult(Result.RESULT_SUCCESS);
        result.setNpub(publicKey);
        return result;
    }
}
