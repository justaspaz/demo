package com.example.demo.dao;

import com.example.demo.enteties.Currency;
import org.springframework.data.repository.CrudRepository;

public interface CurrencyRepo extends CrudRepository<Currency,String> {
}
