package com.itmo.is.lz.pipivo.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.itmo.is.lz.pipivo.dto.BeerDTO;
import com.itmo.is.lz.pipivo.model.Beer;
import com.itmo.is.lz.pipivo.model.BeerDocument;
import com.itmo.is.lz.pipivo.repository.BeerRepository;
import com.itmo.is.lz.pipivo.repository.FavouriteBeerRepository;
import com.itmo.is.lz.pipivo.specification.BeerSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BeerService {
    private final BeerRepository beerRepository;
    private final FavouriteBeerRepository favouriteBeerRepository;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private ElasticsearchClient elasticsearchClient;


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

    public List<BeerDTO> getFavouriteByUserId(Long userId) {
        List<Long> beerIds = favouriteBeerRepository.getFavouriteBeerIdsByUserId(userId);
        if (beerIds.isEmpty()) {
            return List.of();
        }

        List<Beer> beers = beerRepository.findAllById(beerIds);
        return beers.stream().map(this::convertToDTO).toList();
    }

    public SearchResponse<BeerDocument> searchBeers(Map<String, Object> filters, int page, int size) throws IOException {
        Query query = new Query.Builder()
                .bool(b -> {
                    filters.forEach((field, value) -> {

                        if (value instanceof String) {
                            b.must(m -> m.matchPhrasePrefix(
                                    match -> match.field(field).query((String) value)
                            ));
                        } else if (value instanceof Number) {
                            b.must(t -> t.term(term -> term.field(field).value(FieldValue.of(value))));

                        }
                    });
                    return b;
                })
                .build();

        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("beers")
                .query(query)
                .from(page * size)
                .size(size)
                .build();

        System.out.println("Taste profile updated for user");

        return elasticsearchClient.search(searchRequest, BeerDocument.class);

    }
}