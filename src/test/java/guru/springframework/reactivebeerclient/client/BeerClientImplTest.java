package guru.springframework.reactivebeerclient.client;

import guru.springframework.reactivebeerclient.config.WebClientConfig;
import guru.springframework.reactivebeerclient.model.BeerDto;
import guru.springframework.reactivebeerclient.model.BeerPagedList;
import guru.springframework.reactivebeerclient.model.v2.BeerStyleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Null;
import java.math.BigDecimal;
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
        BeerDto beer = BeerDto
                .builder()
                .beerName("Skol")
                .beerStyle(BeerStyleEnum.PILSNER.name())
                .upc(String.valueOf(Math.round(Math.random()) * 10000))
                .quantityOnHand(Long.valueOf(Math.round(Math.random() * 1000)).intValue())
                .price(BigDecimal.valueOf(Math.round(Math.random() * 1000)))
                .build();
        Mono<ResponseEntity<Void>> result = beerClient.createBeer(beer);
        assertEquals(HttpStatus.CREATED.value(), result.block().getStatusCodeValue());
    }

    @Test
    void updateBeer() {
        Mono<BeerPagedList> listMono = beerClient.listBeers(null,
                null,
                null,
                null,
                null);
        BeerDto beerUpdated = listMono.block().stream().findAny().get();
        beerUpdated.setPrice(BigDecimal.TEN);
        @Null UUID id = beerUpdated.getId();
        beerUpdated.setId(null);
        Mono<ResponseEntity<Void>> result = beerClient.updateBeer(id, beerUpdated);
        assertEquals(HttpStatus.NO_CONTENT.value(), result.block().getStatusCodeValue());
    }

    @Test
    void deleteBeerById() {
        Mono<BeerPagedList> listMono = beerClient.listBeers(null,
                null,
                null,
                null,
                null);
        BeerDto beerDeleted = listMono.block().stream().findAny().get();
        @Null UUID id = beerDeleted.getId();
        Mono<ResponseEntity<Void>> result = beerClient.deleteBeerById(id);
        assertEquals(HttpStatus.NO_CONTENT.value(), result.block().getStatusCodeValue());
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