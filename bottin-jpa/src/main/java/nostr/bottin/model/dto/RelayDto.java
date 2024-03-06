package nostr.bottin.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import nostr.bottin.model.Relay;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link Relay}
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RelayDto extends BaseDto implements Serializable {
    private String name;
    private String url;

    @ToString.Exclude
    @JsonIgnore
    private List<NostrIdentityDto> identities;

    public RelayDto() {
        super();
    }

    public RelayDto(String name, String url) {
        this(name, url, new ArrayList<>());
    }

    public RelayDto(String name, String url, List<NostrIdentityDto> identities) {
        this();
        this.name = name;
        this.url = url;
        this.identities = identities;
    }
}