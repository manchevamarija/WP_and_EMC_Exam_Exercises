package mk.ukim.finki.wp.kol2025g2.service.impl;

import mk.ukim.finki.wp.kol2025g2.model.SkiResort;
import mk.ukim.finki.wp.kol2025g2.model.exceptions.InvalidSkiResortIdException;
import mk.ukim.finki.wp.kol2025g2.repository.SkiResortRepository;
import mk.ukim.finki.wp.kol2025g2.service.SkiResortService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkiResortServiceImpl implements SkiResortService {

    private final SkiResortRepository skiResortRepository;

    public SkiResortServiceImpl(SkiResortRepository skiResortRepository) {
        this.skiResortRepository = skiResortRepository;
    }


    @Override
    public SkiResort findById(Long id) {
        return this.skiResortRepository.findById(id).orElseThrow(InvalidSkiResortIdException::new);
    }

    @Override
    public List<SkiResort> listAll() {
        return this.skiResortRepository.findAll();
    }

    @Override
    public SkiResort create(String name, String location) {
        return this.skiResortRepository.save(new SkiResort(name,location));
    }
}
