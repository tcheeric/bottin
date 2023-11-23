package nostr.si4n6r.registration;

import nostr.si4n6r.core.impl.ApplicationProxy;
import nostr.si4n6r.core.impl.BaseActorProxy;
import nostr.si4n6r.storage.fs.NostrApplicationFSVault;

/**
 *
 * @author eric
 */
public class ApplicationRegistration extends AbstractBaseRegistration<ApplicationProxy> {

    public ApplicationRegistration() {
        super(BaseActorProxy.VAULT_ACTOR_ACCOUNT);
    }

    @Override
    public void register(ApplicationProxy actor) {
        var vault = new NostrApplicationFSVault();
        vault.store(actor);
    }

}
