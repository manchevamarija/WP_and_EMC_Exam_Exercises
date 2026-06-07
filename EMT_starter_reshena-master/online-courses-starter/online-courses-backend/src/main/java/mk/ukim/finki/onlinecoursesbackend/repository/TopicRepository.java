package mk.ukim.finki.onlinecoursesbackend.repository;

import mk.ukim.finki.onlinecoursesbackend.model.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
}
