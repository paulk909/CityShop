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
public class OrderLine {
    
    private int OrderLineID;
    private int Quantity;
    private double LineTotal;
    private Product product;

    public OrderLine() {
    }

    public OrderLine(Order order, Product product, int quantity) {
        this.OrderLineID = order.generateUniqueOrderLineId();
        this.Quantity = quantity;
        this.product = product;
    }
    
    public OrderLine(int orderLineID, int quantity, double lineTotal, Product product) {
        this.OrderLineID = orderLineID;
        this.Quantity = quantity;
        this.LineTotal = lineTotal;
        this.product = product;
    }
    

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    
    public int getOrderLineID() {
        return OrderLineID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public double getLineTotal() {
        return LineTotal;
    }

    public void setOrderLineID(int OrderLineID) {
        this.OrderLineID = OrderLineID;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public void setLineTotal(double LineTotal) {
        this.LineTotal = LineTotal;
    }

    @Override
    public String toString() {
        return "OrderLine{" + "OrderLineID=" + OrderLineID + ", Quantity=" + Quantity + ", LineTotal=" + LineTotal + '}';
    }
    
    
    
}
