package ru.netology.sender;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;


public class MessageSenderTest {
    @AfterEach
    private void print() {
        System.out.println();
    }

    @ParameterizedTest
    @MethodSource("source")
    public void testLanguageOfMessageSender(String checkIP, Country country, String expected) {
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp(checkIP)).thenReturn(new Location(null, country, null, 0));

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(country)).thenReturn(expected);

        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, checkIP);

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        String result = messageSender.send(headers);

        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> source() {
        return Stream.of(
                Arguments.of("172.0.32.11", Country.RUSSIA, "Добро пожаловать"),
                Arguments.of("96.44.183.149", Country.USA, "Welcome"),
                Arguments.of("172.0.0.1", Country.RUSSIA, "Добро пожаловать"),
                Arguments.of("96.0.0.1", Country.USA, "Welcome"),
                Arguments.of("12.0.0.1", Country.GERMANY, "Welcome"),
                Arguments.of("14.0.0.1", Country.BRAZIL, "Welcome")
        );
    }
}
