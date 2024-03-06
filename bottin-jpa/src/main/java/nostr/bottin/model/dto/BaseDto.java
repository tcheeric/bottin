package nostr.bottin.model.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.Map;
import java.util.UUID;


@Data
public class BaseDto {
    @ToString.Exclude
    private Long id;
    @ToString.Include
    private String uuid;
    @ToString.Include
    private Date createdAt;
    @ToString.Exclude
    private Map<String, Object> _links;

    public BaseDto() {
        this(null, UUID.randomUUID().toString(), new Date());
    }

    public BaseDto(Long id, String uuid, Date createdAt) {
        this.id = id;
        this.uuid = uuid;
        this.createdAt = createdAt;
    }

    public Long getIdFromLinks() {
        if (_links != null && _links.containsKey("self")) {
            var selfLink = _links.get("self");
            var hrefLink = (Map<String, Object>) selfLink;
            if (hrefLink.containsKey("href")) {
                var href = hrefLink.get("href").toString();
                String[] parts = href.split("/");
                return Long.parseLong(parts[parts.length - 1]);
            }
        }
        return null;
    }

}
