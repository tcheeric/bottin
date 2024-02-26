package nostr.si4n6r.bottin.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.beans.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link nostr.si4n6r.bottin.model.NostrIdentity}
 */
@EqualsAndHashCode(callSuper = true)
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

    @Transient
    public String getNip05() {
        return localpart + "@" + domain;
    }
}