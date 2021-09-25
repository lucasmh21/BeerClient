package guru.springframework.reactivebeerclient.client;

import guru.springframework.reactivebeerclient.config.WebClientProperties;
import guru.springframework.reactivebeerclient.model.BeerDto;
import guru.springframework.reactivebeerclient.model.BeerPagedList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

import static guru.springframework.reactivebeerclient.config.WebClientProperties.*;

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
                        .path(BEER_V1_PATH_ID)
                        .queryParamIfPresent(SHOW_INVENTORY_ON_HAND, Optional.ofNullable(showInventoryOnHand))
                        .build(id.toString()))
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
    public Mono<ResponseEntity<Void>> createBeer(BeerDto beerDto) {
        return webClient.post().uri(uriBuilder -> uriBuilder
                .path(BEER_V1_PATH).build())
                .body(BodyInserters.fromValue(beerDto)).retrieve().toBodilessEntity();
    }

    @Override
    public Mono<ResponseEntity<Void>> updateBeer(UUID id, BeerDto beerDto) {
        return webClient.put().uri(uriBuilder -> uriBuilder
                .path(BEER_V1_PATH_ID)
                .build(id))
                .body(BodyInserters.fromValue(beerDto))
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteBeerById(UUID id) {
        return webClient
                .delete()
                .uri(uriBuilder -> uriBuilder.path(BEER_V1_PATH_ID)
                        .build(id))
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public Mono<BeerDto> getBeerByUPC(String upc) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(BEER_UPC_V1_PATH)
                        .build(upc))
                .retrieve()
                .bodyToMono(BeerDto.class);
    }
}
