package com.example.demo.controlles;

import com.example.demo.dao.CurrencyRepo;
import com.example.demo.dao.ExangeRateRepo;
import com.example.demo.enteties.Currency;
import com.example.demo.enteties.ExangeRate;
import com.example.demo.service.ExcangeRateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.parser.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;


@RestController
public class ExangeRateController {
    @Autowired
    ExangeRateRepo repo;
    @Autowired
    private ExcangeRateService excangeRateService;


    @RequestMapping("/addExangeRates")
    public String addExangeRates() throws Exception {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now().minusDays(1);
            LocalDateTime startDate = now.minusYears(1);
            final String uri = "http://localhost:8080/GetAllCurrencys";
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(uri,String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            List<Currency> currencys = objectMapper.readValue(response,objectMapper.getTypeFactory().constructCollectionType(List.class, Currency.class));
            for(Currency currency:currencys){
                Map<Date, Float> exangeRate = getExangeRateFor(currency, formatter.format(startDate), formatter.format(now));
                SortedSet<Date> keys2 = new TreeSet<>(exangeRate.keySet());
                List<Date> keys = new ArrayList<Date>();
                keys.addAll(keys2);
                Collections.reverse(keys);
                for(Date key : keys){
                    ExangeRate exange = new ExangeRate();
                    exange.setDate(key);
                    exange.setExangeRate(exangeRate.get(key));
                    exange.setCurrency(currency);
                    repo.save(exange);
                }
            }
            if(currencys.isEmpty()){
                return "First add all currencies";
            }else{
                return "Added all exange rates";
            }
    }
    @RequestMapping("/addTodayExangeRates")
    public String addExangeRatesToday() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        final String uri = "http://localhost:8080/GetAllCurrencys";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(uri,String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Currency> currencys = objectMapper.readValue(response,objectMapper.getTypeFactory().constructCollectionType(List.class, Currency.class));
        for(Currency currency:currencys){
            Map<Date, Float> exangeRate = getExangeRateFor(currency, formatter.format(now), formatter.format(now));
            SortedSet<Date> keys2 = new TreeSet<>(exangeRate.keySet());
            List<Date> keys = new ArrayList<Date>();
            keys.addAll(keys2);
            Collections.reverse(keys);
            for(Date key : keys){
                ExangeRate exange = new ExangeRate();
                exange.setDate(key);
                exange.setExangeRate(exangeRate.get(key));
                exange.setCurrency(currency);
                repo.save(exange);
            }
        }
        return "Added today's exange rates";
    }
    public static Map getExangeRateFor(Currency currency,String startDate, String endDate) throws Exception {
        URL url = new URL("http://www.lb.lt/webservices/FxRates/FxRates.asmx/getFxRatesForCurrency?tp=EU&ccy="+currency.getCurrencyAbbreviation()+"&dtFrom="+startDate+"&dtTo="+endDate);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        TimeUnit.MILLISECONDS.sleep(100);
        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        Map<Date, Float> exangeRate = new HashMap<Date,Float>();
        String exangeDate = null;
        String exangeRateString = null;
        int temp = 0;
        while ((inputLine = in.readLine()) != null) {
            if (temp==1){
                String sub = inputLine.replace( "    <Amt>","");
                sub = sub.replace( "</Amt>","");
                exangeRateString =sub;
                temp = 0;
            }
            if(inputLine.contains("<Dt>")){
                String sub = inputLine.replace( "    <Dt>","");
                sub = sub.replace( "</Dt>","");
                exangeDate =sub;
            }else if(inputLine.contains("<Ccy>"+currency.getCurrencyAbbreviation())){
                temp = 1;
            }
            if(exangeDate!=null && exangeRateString != null){
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
                exangeRate.put(ft.parse(exangeDate),Float.valueOf(exangeRateString).floatValue());
                exangeDate = null;
                exangeRateString = null;
            }
        }
        in.close();
        return exangeRate;
    }
}
