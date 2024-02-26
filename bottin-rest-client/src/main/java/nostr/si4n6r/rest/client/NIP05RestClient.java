package nostr.si4n6r.rest.client;

import lombok.extern.java.Log;
import nostr.si4n6r.bottin.model.dto.NostrIdentityDto;

@Log
public class NIP05RestClient extends BottinRestClient{

    public NIP05RestClient() {
        super("nip05", NostrIdentityDto.class);
    }
}
