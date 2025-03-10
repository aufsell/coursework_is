package com.itmo.is.lz.pipivo.controller;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.itmo.is.lz.pipivo.dto.BeerDTO;
import com.itmo.is.lz.pipivo.dto.BeerReviewCountDTO;
import com.itmo.is.lz.pipivo.model.Beer;
import com.itmo.is.lz.pipivo.model.BeerDocument;
import com.itmo.is.lz.pipivo.model.FermentationType;
import com.itmo.is.lz.pipivo.repository.FermentationTypeRepository;
import com.itmo.is.lz.pipivo.service.BeerService;
import com.itmo.is.lz.pipivo.service.TasteProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {
    private final  BeerService beerService;

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ElasticsearchClient elasticsearchClient;
    private final FermentationTypeRepository fermentationTypeRepository;

    private final TasteProfileService tasteProfileService;

    public BeerController(BeerService beerService, ElasticsearchTemplate elasticsearchTemplate, ElasticsearchClient elasticsearchClient, FermentationTypeRepository fermentationTypeRepository, TasteProfileService tasteProfileService) {

        this.beerService = beerService;
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.elasticsearchClient = elasticsearchClient;
        this.fermentationTypeRepository = fermentationTypeRepository;
        this.tasteProfileService = tasteProfileService;
    }


    @GetMapping ("/{beerId}")
    public ResponseEntity<BeerDTO> grantBeerId(@PathVariable Long beerId) {
        BeerDTO beerDTO = beerService.getBeerById(beerId);
        return ResponseEntity.status(HttpStatus.OK).body(beerDTO);
    }

    @GetMapping
    public ResponseEntity<Page<BeerDTO>> getBeers(Pageable pageable, @RequestParam Map<String, String> filters) {
        Page<BeerDTO> beers = beerService.getBeers(pageable, filters);
        return ResponseEntity.ok(beers);
    }

    @GetMapping("/search")
    public Page<BeerDTO> searchBeers(
            @RequestParam(required = false) Double priceMin,
            @RequestParam(required = false) Double priceMax,
            @RequestParam(required = false) Double ratingMin,
            @RequestParam(required = false) Double ratingMax,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long srmMin,
            @RequestParam(required = false) Long srmMax,
            @RequestParam(required = false) Long ibuMin,
            @RequestParam(required = false) Long ibuMax,
            @RequestParam(required = false) Long abvMin,
            @RequestParam(required = false) Long abvMax,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String fermentationType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return beerService.searchBeersWithFilters(
                priceMin, priceMax,
                ratingMin, ratingMax,
                name, srmMin, srmMax,
                ibuMin, ibuMax,
                abvMin, abvMax,
                country, fermentationType, pageable);
    }


    @GetMapping("/beers/search")
    public ResponseEntity<List<BeerDocument>> searchBeers(
            @RequestParam Map<String, String> params)
            throws IOException {

        Map<String, Object> filters = new HashMap<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!entry.getKey().equals("page") && !entry.getKey().equals("size")) {
                filters.put(entry.getKey(), entry.getValue());
            }
        }
        SearchResponse<BeerDocument> response = beerService.searchBeers(filters);
        System.out.println(response);
        List<BeerDocument> beers = response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());

        tasteProfileService.updateTasteProfileBySearch(filters);
        return ResponseEntity.ok(beers);
    }

    @GetMapping("/fermentationType/{id}")
    public ResponseEntity<FermentationType> getFermentationType(@PathVariable Long id) {
        return ResponseEntity.ok(fermentationTypeRepository.findById(id).orElseThrow());
    }


    @GetMapping("/top")
    public ResponseEntity<List<BeerReviewCountDTO>> getTopBeers() {
        return ResponseEntity.ok(beerService.getTopBeers());
    }
}