package mk.ukim.finki.onlinecoursesbackend.dto.domain;

import java.util.List;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Topic;

public record DisplayTopicResponseDto(
    Long id,
    String name,
    String description
) {
    public static DisplayTopicResponseDto from(Topic topic) {
        return new DisplayTopicResponseDto(
            topic.getId(),
            topic.getName(),
            topic.getDescription()
        );
    }

    public static List<DisplayTopicResponseDto> from(List<Topic> topics) {
        return topics
            .stream()
            .map(DisplayTopicResponseDto::from)
            .toList();
    }
}
