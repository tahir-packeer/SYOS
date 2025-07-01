package org.example.Model;

import java.sql.Date;

public class Stock {
    private int id;
    private Item item;
    private int quantity;
    private Date dateOfPurchase;
    private Date dateOfExpiry;
    private boolean availability;


    public Stock(Item item, int quantity, Date dateOfPurchase, Date dateOfExpiry, boolean availability) {
        this.item = item;
        this.quantity = quantity;
        this.dateOfPurchase = dateOfPurchase;
        this.dateOfExpiry = dateOfExpiry;
        this.availability = availability;
    }

    public Stock(Item savedItem, int quantity, java.util.Date date) {
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(Date dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public Date getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(Date dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }


}