package com.itmo.is.lz.pipivo.service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.itmo.is.lz.pipivo.model.BeerDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BeerSearchFacadeService {

    private final BeerService beerService;
    private final TasteProfileService tasteProfileService;

    public List<BeerDocument> searchIndexAndUpdateTasteProfile(Map<String, Object> filters) throws IOException {
        SearchResponse<BeerDocument> response = beerService.searchBeers(filters);

        List<BeerDocument> beers = response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());

        tasteProfileService.updateTasteProfileBySearch(filters);

        return beers;
    }
}

