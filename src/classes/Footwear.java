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
public class Footwear extends Product {
    
    private int Size;

    public Footwear() {
        super();
    }

    public Footwear(int Size, int ProductID, String ProductName, double Price, int StockLevel) {
        super(ProductID, ProductName, Price, StockLevel);
        this.Size = Size;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int Size) {
        this.Size = Size;
    }

    @Override
    public String toString() {
        return this.getProductName();
    }
    
    
}
