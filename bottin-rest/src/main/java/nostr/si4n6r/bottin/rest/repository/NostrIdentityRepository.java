package nostr.si4n6r.bottin.rest.repository;

import nostr.si4n6r.bottin.model.NostrIdentity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.lang.NonNull;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "identities", path = "identity")
public interface NostrIdentityRepository extends PagingAndSortingRepository<NostrIdentity, Long>, CrudRepository<NostrIdentity, Long> {

    @Override
    <S extends NostrIdentity> S save(S entity);

    NostrIdentity findByLocalpartAndDomain(@Param("localpart") @NonNull String localpart, @Param("domain")@NonNull String domain);

    NostrIdentity findByPublicKey(@Param("publicKey") @NonNull String publicKey);

    NostrIdentity findByUuid(@Param("uuid") @NonNull String uuid);

    List<NostrIdentity> findAll();
}