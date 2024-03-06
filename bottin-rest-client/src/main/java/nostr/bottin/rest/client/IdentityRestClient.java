package nostr.bottin.rest.client;

import lombok.NonNull;
import lombok.extern.java.Log;
import nostr.bottin.model.dto.NostrIdentityDto;

import java.util.logging.Level;

@Log
public class IdentityRestClient extends BottinRestClient<NostrIdentityDto> {
    public IdentityRestClient() {
        super("identity", NostrIdentityDto.class);
    }

    public NostrIdentityDto getByPublicKey(@NonNull String publicKey) {
        String url = getBaseUrl() + "/publicKey/" + publicKey;
        log.log(Level.INFO, "Sending request: {0}", url);
        var response = getRestTemplate().getForEntity(url, NostrIdentityDto.class);
        log.log(Level.INFO, "Received response: {0}", response.getBody());
        return response.getBody();
    }

}
