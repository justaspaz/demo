package com.example.demo.enteties;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
public class Currency {
        @Id
        @Column
        private String currencyAbbreviation;
        @Column
        private String currencyName;
        @OneToMany(mappedBy = "currency")
        Set<ExangeRate> exangeRate;
        public void setCurrencyAbbreviation(String currencyAbbreviation){
            this.currencyAbbreviation = currencyAbbreviation;
        }
        public String getCurrencyAbbreviation(){
            return this.currencyAbbreviation;
        }
        public void setCurrencyName(String currencyName){
            this.currencyName = currencyName;
        }
        public String getCurrencyName(){
            return this.currencyName;
        }
}
