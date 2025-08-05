package com.trademo.app.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockApiService {

    @Value("${alphavantage.api.key}")
    private String apiKey;

    @Value("${alphavantage.api.url}")
     private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public double getCurrentPrice(String stockSymbol) {
        try {
            String url = baseUrl + "?function=GLOBAL_QUOTE&symbol=" + stockSymbol + "&apikey=" + apiKey;

            String response = restTemplate.getForObject(url, String.class);
            JSONObject json = new JSONObject(response);

            JSONObject quote = json.getJSONObject("Global Quote");
            String priceStr = quote.getString("05. price");
            return Double.parseDouble(priceStr);

        } catch (org.json.JSONException | org.springframework.web.client.RestClientException | NumberFormatException e) {
            throw new RuntimeException("Failed to fetch stock price for " + stockSymbol, e);
        }
    }
}
