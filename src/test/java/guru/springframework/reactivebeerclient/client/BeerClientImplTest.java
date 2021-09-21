package guru.springframework.reactivebeerclient.client;

import guru.springframework.reactivebeerclient.config.WebClientConfig;
import guru.springframework.reactivebeerclient.model.BeerDto;
import guru.springframework.reactivebeerclient.model.BeerPagedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BeerClientImplTest {
    BeerClientImpl beerClient;

    @BeforeEach
    void setUp() {
        beerClient = new BeerClientImpl(new WebClientConfig().webClientBean());
    }

    @Test
    void getBeerById() {
        Mono<BeerPagedList> result = beerClient.listBeers(null,
                null,
                null,
                null,
                null);
        BeerPagedList beerList = result.block();
        Optional<BeerDto> firstBeer = beerList.stream().findFirst();
        UUID id = firstBeer.isEmpty() ? null: firstBeer.get().getId();
        Mono<BeerDto> resultFindById = beerClient.getBeerById(id, true);
        BeerDto beerDto = resultFindById.block();
        assertNotNull(beerDto);
        assertEquals(id.toString(), beerDto.getId().toString());
    }

    @Test
    void listBeers() {
        Mono<BeerPagedList> result = beerClient.listBeers(null,
                null,
                null,
                null,
                null);
        BeerPagedList beersList = result.block();
        assertNotNull(beersList);
        assertTrue(beersList.getSize() > 0);
    }

    @Test
    void createBeer() {
    }

    @Test
    void updateBeer() {
    }

    @Test
    void deleteBeerById() {
    }

    @Test
    void getBeerByUPC() {
        Mono<BeerPagedList> result = beerClient.listBeers(null,
                null,
                null,
                null,
                null);
        BeerPagedList beerList = result.block();
        Optional<BeerDto> firstBeer = beerList.stream().findFirst();
        String upc = firstBeer.isEmpty() ? null : firstBeer.get().getUpc();
        Mono<BeerDto> beerMono = beerClient.getBeerByUPC(upc);
        BeerDto beer = beerMono.block();
        assertNotNull(beer);
        assertEquals(upc, beer.getUpc());
    }
}