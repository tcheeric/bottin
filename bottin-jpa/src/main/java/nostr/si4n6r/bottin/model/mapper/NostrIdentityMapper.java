package nostr.si4n6r.bottin.model.mapper;

import nostr.si4n6r.bottin.model.NostrIdentity;
import nostr.si4n6r.bottin.model.dto.NostrIdentityDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "cdi")
public interface NostrIdentityMapper {

    NostrIdentity toEntity(NostrIdentityDto nostrIdentityDto);

    NostrIdentityDto toDto(NostrIdentity nostrIdentity);

    List<NostrIdentity> toEntity(List<NostrIdentityDto> nostrIdentityDto);

    List<NostrIdentityDto> toDto(List<NostrIdentity> nostrIdentity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    NostrIdentity partialUpdate(NostrIdentityDto nostrIdentityDto, @MappingTarget NostrIdentity nostrIdentity);
}