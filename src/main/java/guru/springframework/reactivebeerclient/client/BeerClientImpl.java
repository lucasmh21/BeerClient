package guru.springframework.reactivebeerclient.client;

import guru.springframework.reactivebeerclient.config.WebClientProperties;
import guru.springframework.reactivebeerclient.model.BeerDto;
import guru.springframework.reactivebeerclient.model.BeerPagedList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

import static guru.springframework.reactivebeerclient.config.WebClientProperties.BEER_UPC_V1_PATH;
import static guru.springframework.reactivebeerclient.config.WebClientProperties.BEER_V1_PATH;

/**
 * Created by jt on 3/13/21.
 */
@RequiredArgsConstructor
public class BeerClientImpl implements BeerClient {

    private static final String PAGE_NUMBER = "pageNumber";
    private static final String PAGE_SIZE = "pageSize";
    private static final String BEER_NAME = "beerName";
    private static final String BEER_STYLE = "beerStyle";
    private static final String SHOW_INVENTORY_ON_HAND = "showInventoryOnHand";
    private final WebClient webClient;

    @Override
    public Mono<BeerDto> getBeerById(UUID id, Boolean showInventoryOnHand) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(String.format("%s/%s", BEER_V1_PATH, id.toString()))
                        .queryParamIfPresent(SHOW_INVENTORY_ON_HAND, Optional.ofNullable(showInventoryOnHand))
                        .build())
                .attribute(SHOW_INVENTORY_ON_HAND, showInventoryOnHand)
                .retrieve()
                .bodyToMono(BeerDto.class);
    }

    @Override
    public Mono<BeerPagedList> listBeers(Integer pageNumber, Integer pageSize, String beerName, String beerStyle, Boolean showInventoryOnhand) {
        return webClient.get().uri(uri -> uri.path(BEER_V1_PATH)
                .queryParamIfPresent(PAGE_NUMBER, Optional.ofNullable(pageNumber))
                .queryParamIfPresent(PAGE_SIZE, Optional.ofNullable(pageSize))
                .queryParamIfPresent(BEER_NAME, Optional.ofNullable(beerName))
                .queryParamIfPresent(BEER_STYLE, Optional.ofNullable(beerStyle))
                .queryParamIfPresent(SHOW_INVENTORY_ON_HAND, Optional.ofNullable(showInventoryOnhand))
                .build())
                .retrieve().bodyToMono(BeerPagedList.class);
    }

    @Override
    public Mono<ResponseEntity> createBeer(BeerDto beerDto) {
        return null;
    }

    @Override
    public Mono<ResponseEntity> updateBeer(BeerDto beerDto) {
        return null;
    }

    @Override
    public Mono<ResponseEntity> deleteBeerById(UUID id) {
        return null;
    }

    @Override
    public Mono<BeerDto> getBeerByUPC(String upc) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(String.format("%s/%s", BEER_UPC_V1_PATH, upc))
                        .build())
                .retrieve()
                .bodyToMono(BeerDto.class);
    }
}
