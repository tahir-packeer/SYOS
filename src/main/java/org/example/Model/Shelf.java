package org.example.Model;

public class Shelf {
    private int id;
    private Item item;
    private int quantity;
    private String type;

    public Shelf(Item item, int quantity, String type) {
        this.item = item;
        this.quantity = quantity;
        this.type = type;
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

}
