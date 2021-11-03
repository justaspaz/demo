package com.example.demo.controlles;

import com.example.demo.enteties.Calculation;
import com.example.demo.enteties.ExangeRate;
import com.example.demo.service.CurrencyService;
import com.example.demo.service.ExcangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @RequestMapping(value="/home")
    public static String welcome() {
        return "index";
    }
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private ExcangeRateService excangeRateService;


    @RequestMapping(value = "/currencies")
    public String getAllCurrencies(Model model)
    {
        model.addAttribute("currencies", currencyService.getAllCurrencies());
        return  "currencies";
    }

    @RequestMapping(value = "/exangeRate")
    public String getExangeRateByCurrencyAbbreviation(@RequestParam String currencyAbbreviation, Model model)
    {
        List<ExangeRate> exangeRates = excangeRateService.getAllExangeRates();
        List<ExangeRate> exRates =  excangeRateService.getAllExangeRates();;
        for(ExangeRate exangeRate:exangeRates){
            if(!exangeRate.getCurrency().getCurrencyAbbreviation().equals(currencyAbbreviation)){
                exRates.remove(exangeRate);
            }
        }
        model.addAttribute("exangeRates", exRates);
        return  "exangeRate";
    }
    @RequestMapping(value = "/calculator")
    public String getCalculator( Model model)
    {
        model.addAttribute("calculation", new Calculation());
        model.addAttribute("currencies", currencyService.getAllCurrencies());
        return  "calculator";
    }
}