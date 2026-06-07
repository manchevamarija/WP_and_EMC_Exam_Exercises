package mk.ukim.finki.onlinecoursesbackend.service.domain;

import java.util.List;
import java.util.Optional;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Topic;

public interface TopicService {
    List<Topic> findAll();

    Optional<Topic> findById(Long id);

    Topic create(Topic topic);

    Optional<Topic> update(Long id, Topic topic);

    Optional<Topic> deleteById(Long id);
}
