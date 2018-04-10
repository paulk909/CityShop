/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

/**
 *
 * @author 30211275
 */
public class Product {
    
    private int ProductID;
    private String ProductName;
    private double Price;
    private int StockLevel;

    public Product() {
    }

    public Product(int ProductID, String ProductName, double Price, int StockLevel) {
        this.ProductID = ProductID;
        this.ProductName = ProductName;
        this.Price = Price;
        this.StockLevel = StockLevel;
    }

    public int getProductID() {
        return ProductID;
    }

    public String getProductName() {
        return ProductName;
    }

    public double getPrice() {
        return Price;
    }

    public int getStockLevel() {
        return StockLevel;
    }

    public void setProductID(int ProductID) {
        this.ProductID = ProductID;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public void setStockLevel(int StockLevel) {
        this.StockLevel = StockLevel;
    }

    @Override
    public String toString() {
        return ProductName;
    }
    
    
     
    
}
