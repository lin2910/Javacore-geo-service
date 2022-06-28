import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class testGeoService {
    GeoService geoService;

    @BeforeEach
    public void start() {
        geoService = new GeoServiceImpl();
    }

    @ParameterizedTest
    @MethodSource("testLocationByIP")
    public void testByIp(String checkIp, Location expected) {
        Location result = geoService.byIp(checkIp);

        if (expected != null) {
            assertEquals(expected.getCountry(), result.getCountry());
            assertEquals(expected.getCity(), result.getCity());
            assertEquals(expected.getStreet(), result.getStreet());
            assertEquals(expected.getBuiling(), result.getBuiling());
        } else {
            assertNull(result);
        }
    }

    private static Stream<Arguments> testLocationByIP() {
        return Stream.of(
                Arguments.of("", null),
                Arguments.of("199.0.0.1", null),
                Arguments.of("127.0.0.1", new Location(null, null, null, 0)),
                Arguments.of("172.0.32.11", new Location("Moscow", Country.RUSSIA, "Lenina", 15)),
                Arguments.of("96.44.183.149", new Location("New York", Country.USA, " 10th Avenue", 32)),
                Arguments.of("172.0.0.1", new Location("Moscow", Country.RUSSIA, null, 0)),
                Arguments.of("96.0.0.1", new Location("New York", Country.USA, null, 0))
        );
    }

    @Test
    public void testByCoordinatesRuntimeException() {
        double a = 1.0, b = 1.0;
        String expected = "Not implemented";

        Exception exception = assertThrows(RuntimeException.class, () -> geoService.byCoordinates(a, b));
        String result = exception.getMessage();

        assertTrue(result.contains(expected));
    }
}
