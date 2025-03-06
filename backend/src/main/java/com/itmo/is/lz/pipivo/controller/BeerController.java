package com.itmo.is.lz.pipivo.controller;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.itmo.is.lz.pipivo.dto.BeerDTO;
import com.itmo.is.lz.pipivo.model.BeerDocument;
import com.itmo.is.lz.pipivo.service.BeerService;
import com.itmo.is.lz.pipivo.service.TasteProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private TasteProfileService tasteProfileService;

    public BeerController(BeerService beerService) {
        this.beerService = beerService;
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


    @GetMapping("/beers/search")
    public ResponseEntity<List<BeerDocument>> searchBeers(
            @RequestParam Map<String, String> params,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
            throws IOException {

        Map<String, Object> filters = new HashMap<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!entry.getKey().equals("page") && !entry.getKey().equals("size")) {
                filters.put(entry.getKey(), entry.getValue());
            }
        }
        SearchResponse<BeerDocument> response = beerService.searchBeers(filters, page, size);
        System.out.println(response);
        List<BeerDocument> beers = response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
        tasteProfileService.updateTasteProfileBySearch(filters);
        return ResponseEntity.ok(beers);
    }
}