import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.netology.entity.Country;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;

public class testLocalizationService {
    LocalizationService localizationService;

    @BeforeEach
    public void initTest() {
        localizationService = new LocalizationServiceImpl();
    }

    @ParameterizedTest
    @EnumSource(Country.class)
    void testLocaleCountry(Country country) {
        String expected;
        if (country == Country.RUSSIA)
            expected = "Добро пожаловать";
        else
            expected = "Welcome";

        String result = localizationService.locale(country);

        Assertions.assertEquals(expected, result);
    }


}
