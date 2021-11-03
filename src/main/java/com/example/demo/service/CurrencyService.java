package com.example.demo.service;

import com.example.demo.dao.CurrencyRepo;
import com.example.demo.enteties.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyService {
    @Autowired
    public CurrencyRepo currencyRepo;
    public List<Currency> getAllCurrencies() {

        List<Currency> currencies = new ArrayList<>();

        currencyRepo.findAll()
                .forEach(currencies::add);

        return currencies;
    }
}
