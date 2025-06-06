package org.example.Model;

public class Shelf_Stock {
    private int id;
    private Shelf shelf;
    private Stock stock;

    public Shelf_Stock(Shelf shelf, Stock stock) {
        this.shelf = shelf;
        this.stock = stock;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Shelf getShelf() {
        return shelf;
    }
    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }
    public Stock getStock() {
        return stock;
    }
    public void setStock(Stock stock) {
        this.stock = stock;
    }
}