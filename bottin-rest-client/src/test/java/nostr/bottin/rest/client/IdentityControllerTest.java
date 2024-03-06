package nostr.bottin.rest.client;

import nostr.bottin.model.dto.NostrIdentityDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IdentityControllerTest {

    private IdentityRestClient restClient;

    @BeforeEach
    public void setUp() {
        restClient = new IdentityRestClient();
    }

    @Test
    public void getByPublicKey() {
        var index = System.currentTimeMillis();
        var identity = new NostrIdentityDto("localpart_" + index, "domain_" + index, "publicKey_" + index);
        var identityDto = restClient.create(identity);

        var id = restClient.getByPublicKey("publicKey_" + index);

        assertEquals(identityDto.getNip05(), id.getNip05());
    }
}
