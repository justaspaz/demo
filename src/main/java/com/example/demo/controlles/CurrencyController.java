package com.example.demo.controlles;

import com.example.demo.dao.CurrencyRepo;
import com.example.demo.enteties.Calculation;
import com.example.demo.enteties.Currency;
import com.example.demo.enteties.ExangeRate;
import com.example.demo.service.CurrencyService;
import com.example.demo.service.ExcangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@RestController
public class CurrencyController {
    @Autowired
    CurrencyRepo repo;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private ExcangeRateService excangeRateService;
    @RequestMapping("/addAllCurrencys")
    public String addCurrencys() {
        try {
            Map<String, String> currencyMap = getCurrency("http://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrencyList?");
            for(Map.Entry<String,String> entry : currencyMap.entrySet()){
                Currency currency = new Currency();
                currency.setCurrencyAbbreviation(entry.getKey());
                currency.setCurrencyName(entry.getValue());
                repo.save(currency);
            }
        }catch(Exception ex){
            ex.getMessage();
        }
        return "Added all currencies";
    }
    @RequestMapping("/GetAllCurrencys")
    public List<Currency> getAllCurrency(){
        List<Currency> currency = new ArrayList<>();
        repo.findAll().forEach(currency::add);
        return currency;
    }

    public static Map getCurrency(String urlToRead) throws Exception {
        URL url = new URL(urlToRead);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        Map<String,String> currencyMap = new HashMap<String,String>();
        String currencyName = null;
        String currencyAbbreviation = null;
        while ((inputLine = in.readLine()) != null) {
            if(inputLine.contains(" <CcyNm lang=\"LT\">")){
                String sub = inputLine.replace( "    <CcyNm lang=\"LT\">","");
                sub = sub.replace( "</CcyNm>","");
                currencyName =sub;
            }else if(inputLine.contains("<Ccy>")){
                String sub = inputLine.replace( "    <Ccy>","");
                sub = sub.replace( "</Ccy>","");
                currencyAbbreviation = sub;
            }
            if(currencyName!=null && currencyAbbreviation != null){
                currencyMap.put(currencyAbbreviation,currencyName);
                currencyName = null;
                currencyAbbreviation = null;
            }
        }
        in.close();
        return currencyMap;
    }
    @RequestMapping(value = "/calculationCalculate", method = RequestMethod.POST)
    public String submit(@ModelAttribute("calculation") Calculation calculation) {
        List<ExangeRate> exangeRates = excangeRateService.getAllExangeRates();
        List<ExangeRate> exRates =  excangeRateService.getAllExangeRates();
        List<ExangeRate> exRates2 =  excangeRateService.getAllExangeRates();;
        for(ExangeRate exangeRate:exangeRates){
            if(!exangeRate.getCurrency().getCurrencyAbbreviation().equals(calculation.getOneCurrencyAbbreviation())){
                exRates.remove(exangeRate);
            }if(!exangeRate.getCurrency().getCurrencyAbbreviation().equals(calculation.getTwoCurrencyAbbreviation())){
                exRates2.remove(exangeRate);
            }
        }
        ExangeRate exFrom = exRates.get(0);
        ExangeRate exTo = exRates2.get(0);
        for(ExangeRate exangeRate:exRates){
            if(exangeRate.getDate().after(exFrom.getDate())){
                exFrom = exangeRate;
            }
        }
        for(ExangeRate exangeRate:exRates2){
            if(exangeRate.getDate().after(exTo.getDate())){
                exTo = exangeRate;
            }
        }
        Float ats = Float.valueOf(calculation.getFloatNumber()).floatValue()/exFrom.getExangeRate()*exTo.getExangeRate();
        return ats.toString();
    }

}
