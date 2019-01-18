//getters setters class for productQuantity which contains the details to be displayed in the details page
package com.niyati.customcollections;

public class ProductQuantityClass {
    private String title;
    private String inventory_quantity;
    private String collection_title;
    private String collection_image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInventory_quantity() {
        return inventory_quantity;
    }

    public void setInventory_quantity(String inventory_quantity) {
        this.inventory_quantity = inventory_quantity;
    }

    public String getCollection_title() {
        return collection_title;
    }

    public void setCollection_title(String collection_title) {
        this.collection_title = collection_title;
    }

    public String getCollection_image() {
        return collection_image;
    }

    public void setCollection_image(String collection_image) {
        this.collection_image = collection_image;
    }

    public ProductQuantityClass(String title, String inventory_quantity,String collection_title,String collection_image) {
        this.title = title;
        this.inventory_quantity = inventory_quantity;
        this.collection_title = collection_title;
        this.collection_image = collection_image;

    }
}
