package com.itmo.is.lz.pipivo.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.itmo.is.lz.pipivo.dto.BeerDTO;
import com.itmo.is.lz.pipivo.dto.BeerReviewCountDTO;
import com.itmo.is.lz.pipivo.model.Beer;
import com.itmo.is.lz.pipivo.model.BeerDocument;
import com.itmo.is.lz.pipivo.repository.BeerRepository;
import com.itmo.is.lz.pipivo.repository.FavouriteBeerRepository;
import com.itmo.is.lz.pipivo.repository.ReviewsRepository;
import com.itmo.is.lz.pipivo.specification.BeerSpecification;
import com.itmo.is.lz.pipivo.specification.BeerSpecificationFilters;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BeerService {
    private final BeerRepository beerRepository;
    private final FavouriteBeerRepository favouriteBeerRepository;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private ElasticsearchClient elasticsearchClient;
    @Autowired
    private ReviewsRepository reviewsRepository;

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
        beerDTO.setAverageRating(beer.getAverageRating());
        beerDTO.setFermentationType(beer.getFermentationType());
        beerDTO.setSrm(beer.getSrm());
        beerDTO.setIbu(beer.getIbu());
        beerDTO.setAbv(beer.getAbv());
        beerDTO.setOg(beer.getOg());
        beerDTO.setCountry(beer.getCountry());
        beerDTO.setImagePath(beer.getImagePath());
        beerDTO.setAverageRating(beer.getAverageRating());
        return beerDTO;
    }

    public Page<BeerDTO> searchBeersWithFilters(
            Double priceMin, Double priceMax,
            Double ratingMin, Double ratingMax,
            String name, Long srmMin, Long srmMax,
            Long ibuMin, Long ibuMax,
            Long abvMin, Long abvMax,
            String country, String fermentationType, Pageable pageable) {

        Specification<Beer> specification = BeerSpecificationFilters.filterBeers(
                priceMin, priceMax,
                ratingMin, ratingMax,
                name, srmMin, srmMax,
                ibuMin, ibuMax,
                abvMin, abvMax,
                country, fermentationType);
        Page<Beer> beers = beerRepository.findAll(specification, pageable);
        return beers.map(this::convertToDTO);
    }

    public Page<BeerDTO> getBeers(Pageable pageable, Map<String, String> filters) {
        Specification<Beer> spec = BeerSpecification.withFilters(filters);
        return beerRepository.findAll(spec, pageable).map(beer ->
                new BeerDTO(
                        beer.getId(), beer.getName(), beer.getPrice(), beer.getVolume(),
                        beer.getFermentationType(), beer.getAverageRating(), beer.getSrm(), beer.getIbu(),
                        beer.getAbv(), beer.getOg(), beer.getCountry(), beer.getImagePath())

        );
    }

    public List<BeerDTO> getFavouriteByUserId(Long userId) {
        List<Long> beerIds = favouriteBeerRepository.getFavouriteBeerIdsByUserId(userId);
        if (beerIds.isEmpty()) {
            return List.of();
        }

        List<Beer> beers = beerRepository.findAllById(beerIds);
        return beers.stream().map(this::convertToDTO).toList();
    }

    public SearchResponse<BeerDocument> searchBeers(Map<String, Object> filters) throws IOException {
        Query query = new Query.Builder()
                .bool(b -> {
                    filters.forEach((field, value) -> {
                        if (value instanceof String) {
                            try {
                                if (field.equals("id") || field.equals("abv") || field.equals("ibu") || field.equals("price")) {
                                    double numericValue = Double.parseDouble((String) value);
                                    b.must(t -> t.term(
                                            term -> term
                                                    .field(field)
                                                    .value(numericValue)
                                    ));
                                } else {
                                    b.must(m -> m.matchPhrasePrefix(
                                            match -> match
                                                    .field(field)
                                                    .query((String) value)
                                                    .analyzer(field.equals("name") ? "ngram_analyzer" : null)
                                    ));
                                }
                            } catch (NumberFormatException e) {
                                b.must(m -> m.matchPhrasePrefix(
                                        match -> match
                                                .field(field)
                                                .query((String) value)
                                                .analyzer(field.equals("name") ? "ngram_analyzer" : null)
                                ));
                            }
                        } else if (value instanceof Number numberValue) {
                            b.must(t -> t.term(
                                    term -> term
                                            .field(field)
                                            .value(numberValue.doubleValue())
                            ));
                        }
                    });
                    return b;
                })
                .build();

        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("beers")
                .query(query)
                .build();



        return elasticsearchClient.search(searchRequest, BeerDocument.class);
    }


    public List<BeerReviewCountDTO> getTopBeers() {
        List<Object[]> topBeers = reviewsRepository.findTopBeers();

        List<BeerReviewCountDTO> result = new ArrayList<>();
        for (Object[] row : topBeers) {
            Integer beerId = (Integer) row[0];
            String beerName = (String) row[1];
            String imagePath = (String) row[2];
            Double price = row[3] != null ? ((BigDecimal) row[3]).doubleValue() : null;
            Double averageRating = row[4] != null ? ((BigDecimal) row[4]).doubleValue() : null;
            String country = (String) row[5];
            Integer reviewCount = (Integer) row[6];

            result.add(new BeerReviewCountDTO(beerId, beerName, imagePath, price, averageRating, country, reviewCount));
        }
        return result;
    }
}