package org.hemostaza;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class ExchangeController {

    private static final String API_URL = "http://api.nbp.pl/api/exchangerates/rates/a/usd/";

    public Double getRate(String date) throws Exception {

        URL url = new URI(API_URL + date + "?format=json").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode == 404) {
            throw new Exception("Dane nie są dostępne dla podanej daty.");
        } else if (responseCode != 200) {
            throw new Exception("Nie udało się pobrać kursu. Kod błędu: " + responseCode);
        }

        StringBuilder responseString = new StringBuilder();
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            responseString.append(scanner.nextLine());
        }
        scanner.close();

        //System.out.println(responseString);

        JSONParser parser = new JSONParser();
        JSONObject exchangeRate = (JSONObject) parser.parse(String.valueOf(responseString));
        JSONArray rates = (JSONArray) exchangeRate.get("rates");
        JSONObject rate = (JSONObject) rates.getFirst();
        //System.out.println(Double.parseDouble(String.valueOf(rate.get("mid"))));
        return Double.parseDouble(String.valueOf(rate.get("mid")));

    }

}
