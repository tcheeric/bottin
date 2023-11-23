module si4n6r.client {
    requires lombok;
    requires java.logging;

    requires si4n6r.core;
    requires si4n6r.storage;
    requires si4n6r.storage.fs;

    requires nostr.id;
    requires nostr.base;

    exports nostr.si4n6r.registration;
}