package mk.ukim.finki.traveltoursbackend.service.domain.impl;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.traveltoursbackend.model.domain.Destination;
import mk.ukim.finki.traveltoursbackend.repository.DestinationRepository;
import mk.ukim.finki.traveltoursbackend.service.domain.DestinationService;
import org.springframework.stereotype.Service;

@Service
public class DestinationServiceImpl implements DestinationService {

    private final DestinationRepository destinationRepository;

    public DestinationServiceImpl(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    @Override
    public List<Destination> findAll() {
        return destinationRepository.findAll();
    }

    @Override
    public Optional<Destination> findById(Long id) {
        return destinationRepository.findById(id);
    }

    @Override
    public Destination create(Destination destination) {
        return destinationRepository.save(destination);
    }

    @Override
    public Optional<Destination> update(Long id, Destination destination) {
        return findById(id)
            .map((existingDestination) -> {
                existingDestination.setName(destination.getName());
                existingDestination.setDescription(destination.getDescription());
                return destinationRepository.save(existingDestination);
            });
    }

    @Override
    public Optional<Destination> deleteById(Long id) {
        Optional<Destination> destination = findById(id);
        destination.ifPresent(destinationRepository::delete);
        return destination;
    }
}
