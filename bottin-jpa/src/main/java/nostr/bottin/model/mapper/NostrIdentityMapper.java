package nostr.bottin.model.mapper;

import nostr.bottin.model.NostrIdentity;
import nostr.bottin.model.dto.NostrIdentityDto;
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