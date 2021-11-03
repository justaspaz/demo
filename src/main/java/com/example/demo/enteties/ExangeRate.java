package com.example.demo.enteties;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(uniqueConstraints=
        @UniqueConstraint(columnNames={"currency_currencyAbbreviation", "date"}))
public class ExangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private float exangeRate;
    @Column
    private Date date;
    @ManyToOne
    @JoinColumn(name="currency_currencyAbbreviation")
    Currency currency;

    public void setExangeRate(float exangeRate){
        this.exangeRate = exangeRate;
}
    public float getExangeRate(){
        return this.exangeRate;
    }
    public void setDate(Date date){
        this.date = date;
    }
    public Date getDate(){
        return this.date;
    }
    public Currency getCurrency(){
        return this.currency;
    }
    public void setCurrency(Currency currency){
        this.currency = currency;
    }
}
