package nostr.si4n6r.bottin.model.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DTO for {@link nostr.si4n6r.bottin.model.Relay}
 */
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