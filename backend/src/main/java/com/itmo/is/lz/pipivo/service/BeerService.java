package com.itmo.is.lz.pipivo.service;

import com.itmo.is.lz.pipivo.dto.BeerDTO;
import com.itmo.is.lz.pipivo.model.Beer;
import com.itmo.is.lz.pipivo.repository.BeerRepository;
import com.itmo.is.lz.pipivo.specification.BeerSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BeerService {
    private BeerRepository beerRepository;
    public BeerService(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    public BeerDTO getBeerById(Long beerId) {
        BeerDTO beerDTO = beerRepository.findById(beerId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Beer with id " + beerId + " was not found."));
        return beerDTO;
    }

    public BeerDTO convertToDTO(Beer beer) {
        if (beer == null) {
            return null;
        }
        BeerDTO beerDTO = new BeerDTO();
        beerDTO.setBeerId(beer.getId());
        beerDTO.setName(beer.getName());
        beerDTO.setPrice(beer.getPrice());
        beerDTO.setVolume(beer.getVolume());
        beerDTO.setFermentationType(beer.getFermentationType());
        beerDTO.setSrm(beer.getSrm());
        beerDTO.setIbu(beer.getIbu());
        beerDTO.setAbv(beer.getAbv());
        beerDTO.setOg(beer.getOg());
        beerDTO.setCountry(beer.getCountry());
        beerDTO.setImagePath(beer.getImagePath());
        return beerDTO;
    }

    public Page<BeerDTO> getBeers(Pageable pageable, Map<String, String> filters) {
        Specification<Beer> spec = BeerSpecification.withFilters(filters);
        return beerRepository.findAll(spec, pageable).map(beer ->
                new BeerDTO(
                        beer.getId(), beer.getName(), beer.getPrice(), beer.getVolume(),
                        beer.getFermentationType(), beer.getSrm(), beer.getIbu(),
                        beer.getAbv(), beer.getOg(), beer.getCountry(), beer.getImagePath())
        );
    }
}
