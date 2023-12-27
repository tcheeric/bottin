package nostr.si4n6r.bottin.model.mapper;

import nostr.si4n6r.bottin.model.Relay;
import nostr.si4n6r.bottin.model.dto.RelayDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "cdi")
public interface RelayMapper {
    Relay toEntity(RelayDto relayDto);

    RelayDto toDto(Relay relay);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Relay partialUpdate(RelayDto relayDto, @MappingTarget Relay relay);

    List<Relay> toEntity(List<RelayDto> relayDto);

    List<RelayDto> toDto(List<Relay> relay);
}