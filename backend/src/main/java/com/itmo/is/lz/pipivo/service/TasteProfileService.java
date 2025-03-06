package com.itmo.is.lz.pipivo.service;

import com.itmo.is.lz.pipivo.dto.BeerDTO;
import com.itmo.is.lz.pipivo.model.FermentationType;
import com.itmo.is.lz.pipivo.model.TasteProfile;
import com.itmo.is.lz.pipivo.model.User;
import com.itmo.is.lz.pipivo.repository.TasteProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TasteProfileService {

    private final BeerService beerService;
    private final TasteProfileRepository tasteProfileRepository;
    private final UserService userService;

    private final double favouriteWeight = 0.7;
    private final double searchWeight = 0.1;

    public void updateTasteProfileByFavourite(Long userId, Long beerId) {
        TasteProfile tasteProfile = tasteProfileRepository.findByUserId(userId);
        BeerDTO beer = beerService.getBeerById(beerId);
        tasteProfile.setIbuPreference((long) (tasteProfile.getIbuPreference()*(1-favouriteWeight) + beer.getIbu()*favouriteWeight));
        tasteProfile.setAbvPreference((long) (tasteProfile.getAbvPreference()*(1-favouriteWeight) + beer.getAbv()*favouriteWeight));
        tasteProfile.setSrmPreference((long) (tasteProfile.getSrmPreference()*(1-favouriteWeight) + beer.getSrm()*favouriteWeight));
        tasteProfile.setOgPreference((long) (tasteProfile.getOgPreference()*(1-favouriteWeight) + beer.getOg()*favouriteWeight));
        tasteProfile.setPricePreference((long) (tasteProfile.getPricePreference()*(1-favouriteWeight) + beer.getPrice()*favouriteWeight));

        FermentationType fermentationType = findMostPopularFermentationTypeByUserId(userId);
        if (fermentationType == null) {
            tasteProfile.setFermentationType(beer.getFermentationType());
        }
        else {
            tasteProfile.setFermentationType(fermentationType);
        }
        tasteProfileRepository.save(tasteProfile);

    }

    public void updateTasteProfileBySearch(Map<String, Object> filters) {
        String username = userService.getCurrentUsername();
        User user = userService.getByUsername(username);

        TasteProfile tasteProfile = tasteProfileRepository.findByUserId(user.getId());
        if (tasteProfile == null) {
            return;
        }
  
        if (filters.containsKey("ibu")) {
            double ibu = Double.parseDouble(filters.get("ibu").toString());
            tasteProfile.setIbuPreference((long) (tasteProfile.getIbuPreference() * (1 - searchWeight) + ibu * searchWeight));
        }

        if (filters.containsKey("abv")) {
            double abv = Double.parseDouble(filters.get("abv").toString());
            tasteProfile.setAbvPreference((long) (tasteProfile.getAbvPreference() * (1 - searchWeight) + abv * searchWeight));
        }

        if (filters.containsKey("srm")) {
            double srm = Double.parseDouble(filters.get("srm").toString());
            tasteProfile.setSrmPreference((long) (tasteProfile.getSrmPreference() * (1 - searchWeight) + srm * searchWeight));
        }

        if (filters.containsKey("og")) {
            double og = Double.parseDouble(filters.get("og").toString());
            tasteProfile.setOgPreference((long) (tasteProfile.getOgPreference() * (1 - searchWeight) + og * searchWeight));
        }

        if (filters.containsKey("price")) {
            double price = Double.parseDouble(filters.get("price").toString());
            tasteProfile.setPricePreference((long) (tasteProfile.getPricePreference() * (1 - searchWeight) + price * searchWeight));
        }

        tasteProfileRepository.save(tasteProfile);
    }

    private FermentationType findMostPopularFermentationTypeByUserId(Long id) {
        List<BeerDTO> beers = beerService.getFavouriteByUserId(id);

        return beers.stream()
                .collect(Collectors.groupingBy(BeerDTO::getFermentationType, Collectors.counting())) // Группируем и считаем
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }


}
