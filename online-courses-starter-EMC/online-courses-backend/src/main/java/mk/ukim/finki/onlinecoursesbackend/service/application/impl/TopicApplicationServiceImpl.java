package mk.ukim.finki.onlinecoursesbackend.service.application.impl;

import java.util.List;
import java.util.Optional;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.CreateOrUpdateTopicRequestDto;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.DisplayTopicResponseDto;
import mk.ukim.finki.onlinecoursesbackend.service.application.TopicApplicationService;
import mk.ukim.finki.onlinecoursesbackend.service.domain.TopicService;
import org.springframework.stereotype.Service;

@Service
public class TopicApplicationServiceImpl implements TopicApplicationService {
    private final TopicService topicService;

    public TopicApplicationServiceImpl(TopicService topicService) {
        this.topicService = topicService;
    }

    @Override
    public List<DisplayTopicResponseDto> findAll() {
        return DisplayTopicResponseDto.from(topicService.findAll());
    }

    @Override
    public Optional<DisplayTopicResponseDto> findById(Long id) {
        return topicService
            .findById(id)
            .map(DisplayTopicResponseDto::from);
    }

    @Override
    public DisplayTopicResponseDto create(CreateOrUpdateTopicRequestDto createTopicRequestDto) {
        return DisplayTopicResponseDto.from(topicService.create(createTopicRequestDto.toTopic()));
    }

    @Override
    public Optional<DisplayTopicResponseDto> update(Long id, CreateOrUpdateTopicRequestDto updateTopicRequestDto) {
        return topicService
            .update(id, updateTopicRequestDto.toTopic())
            .map(DisplayTopicResponseDto::from);
    }

    @Override
    public Optional<DisplayTopicResponseDto> deleteById(Long id) {
        return topicService
            .deleteById(id)
            .map(DisplayTopicResponseDto::from);
    }
}
