package nostr.bottin.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nostr.bottin.model.NostrIdentity;

import java.beans.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link NostrIdentity}
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