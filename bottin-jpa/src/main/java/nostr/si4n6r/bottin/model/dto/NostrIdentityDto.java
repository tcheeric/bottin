package nostr.si4n6r.bottin.model.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;

/**
 * DTO for {@link nostr.si4n6r.bottin.model.NostrIdentity}
 */
@Data
public class NostrIdentityDto extends BaseDto implements Serializable {
    private String localpart;
    private String domain;
    private String publicKey;
    private List<RelayDto> relays;

    public NostrIdentityDto() {
        super();
    }

    public NostrIdentityDto(String localpart, String domain, String publicKey) {
        this(localpart, domain, publicKey, new ArrayList<>());
    }

    public NostrIdentityDto(String localpart, String domain, String publicKey, List<RelayDto> relays) {
        this();
        this.localpart = localpart;
        this.domain = domain;
        this.publicKey = publicKey;
        this.relays = relays;
    }
}