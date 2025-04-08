package uk.gov.service.payments.commons.testing.pact.consumers;

import au.com.dius.pact.consumer.junit.PactVerification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Request;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class PayPactProviderRuleTest {

    @Rule
    public PayPactProviderRule stockQuoteService = new PayPactProviderRule("stock-quote-service", this);

    @Rule
    public PayPactProviderRule weatherService = new PayPactProviderRule("weather-service", this);

    @Test
    @PactVerification({"stock-quote-service"})
    @Pacts(pacts = {"consumer-stock-quote-service-axp"})
    public void getAmexPrice() throws Exception {
        HashMap map = jsonToMap(Request.Get(stockQuoteService.getUrl() + "/stocks/AXP").execute().returnContent().asString());
        assertThat(map.get("name")).isEqualTo("American Express");
        assertThat(map.get("price")).isEqualTo("200.00");
        assertThat(map.get("currency")).isEqualTo("USD");
    }

    @Test
    @PactVerification({"stock-quote-service"})
    @Pacts(pacts = {"consumer-stock-quote-service-v"})
    public void getVisaPrice() throws Exception {
        HashMap map = jsonToMap(Request.Get(stockQuoteService.getUrl() + "/stocks/V").execute().returnContent().asString());
        assertThat(map.get("name")).isEqualTo("Visa");
        assertThat(map.get("price")).isEqualTo("100.00");
        assertThat(map.get("currency")).isEqualTo("USD");
    }

    @Test
    @PactVerification({"weather-service"})
    @Pacts(pacts = {"consumer-weather-service"})
    public void getLondonWeather() throws Exception {
        HashMap map = jsonToMap(Request.Get(weatherService.getUrl() + "/weather/London").execute().returnContent().asString());
        assertThat(map.get("name")).isEqualTo("London");
        assertThat(map.get("temperature")).isEqualTo("25");
        assertThat(map.get("unit")).isEqualTo("degrees");
    }

    @Test
    @PactVerification({"weather-service", "stock-quote-service"})
    @Pacts(pacts = {"consumer-weather-service", "consumer-stock-quote-service-axp"})
    public void callTwoProviders() throws Exception {
        HashMap map = jsonToMap(Request.Get(stockQuoteService.getUrl() + "/stocks/AXP").execute().returnContent().asString());
        assertThat(map.get("name")).isEqualTo("American Express");
        assertThat(map.get("price")).isEqualTo("200.00");
        assertThat(map.get("currency")).isEqualTo("USD");

        map = jsonToMap(Request.Get(weatherService.getUrl() + "/weather/London").execute().returnContent().asString());
        assertThat(map.get("name")).isEqualTo("London");
        assertThat(map.get("temperature")).isEqualTo("25");
        assertThat(map.get("unit")).isEqualTo("degrees");
    }

    private HashMap jsonToMap(String respBody) throws IOException {
        if (respBody.isEmpty()) {
            return new HashMap();
        }
        return new ObjectMapper().readValue(respBody, HashMap.class);
    }
}
