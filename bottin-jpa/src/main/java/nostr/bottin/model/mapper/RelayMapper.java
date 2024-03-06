package nostr.bottin.model.mapper;

import nostr.bottin.model.Relay;
import nostr.bottin.model.dto.RelayDto;
import org.mapstruct.*;

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