package mk.ukim.finki.onlinecoursesbackend.dto.domain;

import mk.ukim.finki.onlinecoursesbackend.model.domain.Topic;

public record CreateOrUpdateTopicRequestDto(
    String name,
    String description
) {
    public Topic toTopic() {
        return new Topic(name, description);
    }
}
