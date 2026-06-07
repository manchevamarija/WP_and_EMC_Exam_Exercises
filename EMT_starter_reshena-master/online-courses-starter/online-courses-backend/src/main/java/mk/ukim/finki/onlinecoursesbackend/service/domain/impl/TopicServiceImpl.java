package mk.ukim.finki.onlinecoursesbackend.service.domain.impl;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Topic;
import mk.ukim.finki.onlinecoursesbackend.repository.TopicRepository;
import mk.ukim.finki.onlinecoursesbackend.service.domain.TopicService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;

    public TopicServiceImpl(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    public List<Topic> findAll() {
        log.info("Finding all topics");
        return topicRepository.findAll();
    }

    @Override
    public Optional<Topic> findById(Long id) {
        log.info("Finding topic by id={}", id);
        return topicRepository.findById(id);
    }

    @Override
    public Topic create(Topic topic) {
        Topic saved = topicRepository.save(topic);
        log.info("Created topic id={} name='{}'", saved.getId(), saved.getName());
        return saved;
    }

    @Override
    public Optional<Topic> update(Long id, Topic topic) {
        return findById(id)
            .map((existingTopic) -> {
                existingTopic.setName(topic.getName());
                existingTopic.setDescription(topic.getDescription());
                Topic saved = topicRepository.save(existingTopic);
                log.info("Updated topic id={} name='{}'", saved.getId(), saved.getName());
                return saved;
            });
    }

    @Override
    public Optional<Topic> deleteById(Long id) {
        Optional<Topic> topic = findById(id);
        topic.ifPresent(t -> {
            topicRepository.delete(t);
            log.info("Deleted topic id={} name='{}'", t.getId(), t.getName());
        });
        return topic;
    }
}
