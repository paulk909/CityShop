/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 30211275
 */

package classes;

public class Clothing extends Product {
    
    private String Measurement;

    public Clothing() {
        super();
    }

    public Clothing(String Measurement, int ProductId, String ProductName, double Price, int StockLevel) {
        super(ProductId, ProductName, Price, StockLevel);
        this.Measurement = Measurement;
    }

    public String getMeasurement() {
        return Measurement;
    }

    public void setMeasurement(String Measurement) {
        this.Measurement = Measurement;
    }

    @Override
    public String toString() {
        return this.getProductName();
    }
    
    
}
