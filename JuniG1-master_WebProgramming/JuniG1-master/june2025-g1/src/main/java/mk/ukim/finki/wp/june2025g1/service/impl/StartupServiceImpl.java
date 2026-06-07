package mk.ukim.finki.wp.june2025g1.service.impl;

import mk.ukim.finki.wp.june2025g1.model.Founder;
import mk.ukim.finki.wp.june2025g1.model.Industry;
import mk.ukim.finki.wp.june2025g1.model.Startup;
import mk.ukim.finki.wp.june2025g1.model.exceptions.InvalidStartupIdException;
import mk.ukim.finki.wp.june2025g1.repository.StartupRepository;
import mk.ukim.finki.wp.june2025g1.service.FounderService;
import mk.ukim.finki.wp.june2025g1.service.StartupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static mk.ukim.finki.wp.june2025g1.service.specifications.FieldFilterSpecification.*;

@Service
public class StartupServiceImpl implements StartupService {

    private final StartupRepository startupRepository;
    private final FounderService founderService;

    public StartupServiceImpl(StartupRepository startupRepository, FounderService founderService) {
        this.startupRepository = startupRepository;
        this.founderService = founderService;
    }


    @Override
    public List<Startup> listAll() {
        return  this.startupRepository.findAll();
    }

    @Override
    public Startup findById(Long id) {
        return  this.startupRepository.findById(id).orElseThrow(InvalidStartupIdException::new);
    }

    @Override
    public Startup create(String name, Double valuation, Integer yearFounded, Industry industry, Long founderId) {
        Founder founder = founderService.findById(founderId);
        Startup startup = new Startup(name,valuation,yearFounded,industry,founder);
        return startupRepository.save(startup);
    }

    @Override
    public Startup update(Long id, String name, Double valuation, Integer yearFounded, Industry industry, Long founderId) {
        Founder founder = founderService.findById(founderId);
        Startup startup = this.findById(id);

        startup.setName(name);
        startup.setValuation(valuation);
        startup.setYearFounded(yearFounded);
        startup.setIndustry(industry);
        startup.setFounder(founder);

        return startupRepository.save(startup);
    }

    @Override
    public Startup delete(Long id) {
        Startup startup = this.findById(id);
        this.startupRepository.delete(startup);
        return startup;
    }

    @Override
    public Startup deactivate(Long id) {
        Startup startup = this.findById(id);
        startup.setActive(!startup.isActive());
        return startupRepository.save(startup);
    }

    @Override
    public Page<Startup> findPage(String name, Double valuation, Integer yearFounded, Industry industry, Long founderId, int pageNum, int pageSize) {
        Specification<Startup> specification = Specification.allOf(
                filterContainsText(Startup.class, "name", name),
                greaterThan(Startup.class, "valuation", valuation),
                greaterThan(Startup.class, "yearFounded", yearFounded),
                filterEqualsV(Startup.class, "industry", industry),
                filterEquals(Startup.class, "founder.id", founderId)
        );

        return this.startupRepository.findAll(
                specification,
                PageRequest.of(pageNum, pageSize));
    }
}
