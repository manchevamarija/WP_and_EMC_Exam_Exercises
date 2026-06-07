package mk.ukim.finki.traveltoursbackend.dto.domain;

import java.util.List;
import mk.ukim.finki.traveltoursbackend.model.domain.Destination;

public record DisplayDestinationResponseDto(
    Long id,
    String name,
    String description
) {
    public static DisplayDestinationResponseDto from(Destination destination) {
        return new DisplayDestinationResponseDto(
            destination.getId(),
            destination.getName(),
            destination.getDescription()
        );
    }

    public static List<DisplayDestinationResponseDto> from(List<Destination> destinations) {
        return destinations
            .stream()
            .map(DisplayDestinationResponseDto::from)
            .toList();
    }
}
