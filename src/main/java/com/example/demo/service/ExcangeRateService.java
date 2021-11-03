package com.example.demo.service;

import com.example.demo.dao.CurrencyRepo;
import com.example.demo.dao.ExangeRateRepo;
import com.example.demo.enteties.Currency;
import com.example.demo.enteties.ExangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ExcangeRateService {

    @Autowired
    public ExangeRateRepo exangeRateRepo;

    public List<ExangeRate> getAllExangeRates() {

        List<ExangeRate> exangeRates = new ArrayList<>();

        exangeRateRepo.findAll()
                .forEach(exangeRates::add);
        return exangeRates;
    }
}
