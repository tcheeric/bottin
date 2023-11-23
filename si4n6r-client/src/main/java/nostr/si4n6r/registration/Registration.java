package nostr.si4n6r.registration;

import nostr.si4n6r.core.impl.BaseActorProxy;

/**
 *
 * @author eric
 * @param <T>
 */
public interface Registration<T extends BaseActorProxy> {
 
    void register(T actor);
}
