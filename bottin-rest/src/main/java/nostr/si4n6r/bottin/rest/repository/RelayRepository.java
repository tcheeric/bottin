package nostr.si4n6r.bottin.rest.repository;

import nostr.si4n6r.bottin.model.Relay;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.lang.NonNull;

@RepositoryRestResource(collectionResourceRel = "relays", path = "relay")
public interface RelayRepository extends PagingAndSortingRepository<Relay, Long>, CrudRepository<Relay, Long> {

    @Override
    <S extends Relay> S save(S entity);

    Relay findByUrl(@Param("url") @NonNull String url);

    Relay findByUuid(@Param("uuid") @NonNull String uuid);

    Relay findByName(@Param("name") @NonNull String name);
}