package nostr.bottin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@lombok.NoArgsConstructor
@lombok.Setter
@Entity(name = "identity")
@Table(name = "t_identity")
@ToString
public class NostrIdentity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uuid", unique = true, nullable = false, length = Integer.MAX_VALUE)
    private String uuid;

    @Column(name = "localpart", nullable = false, length = Integer.MAX_VALUE)
    private String localpart;

    @Column(name = "domain", nullable = false, length = Integer.MAX_VALUE)
    private String domain;

    @Column(name = "public_key", nullable = false, unique = true, length = Integer.MAX_VALUE)
    private String publicKey;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "t_identity_relay",
            joinColumns = @JoinColumn(name = "identity_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "relay_id", referencedColumnName = "id"))
    @JsonIgnore
    private List<Relay> relays = new ArrayList<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        NostrIdentity identity = (NostrIdentity) o;
        return getId() != null && Objects.equals(getId(), identity.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}