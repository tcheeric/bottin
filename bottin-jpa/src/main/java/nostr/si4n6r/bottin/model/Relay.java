package nostr.si4n6r.bottin.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@lombok.NoArgsConstructor
@lombok.Getter
@lombok.Setter
@Entity(name = "relay")
@Table(name = "t_relay")
@ToString
public class Relay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, length = Integer.MAX_VALUE)
    private String uuid;

    @Column(name = "name", unique = true, length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "url", nullable = false, unique = true, length = Integer.MAX_VALUE)
    private String url;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @ManyToMany(mappedBy = "relays", cascade = {CascadeType.ALL})
    @RestResource(exported = false)
    @ToString.Exclude
    private List<NostrIdentity> identities = new ArrayList<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Relay relay = (Relay) o;
        return getId() != null && Objects.equals(getId(), relay.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}