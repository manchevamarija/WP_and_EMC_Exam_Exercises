package mk.ukim.finki.traveltoursbackend.dto.domain;

import mk.ukim.finki.traveltoursbackend.model.domain.Destination;

public record CreateOrUpdateDestinationRequestDto(
    String name,
    String description
) {
    public Destination toDestination() {
        return new Destination(name, description);
    }
}
