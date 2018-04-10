/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 30211275
 */
public class Order {
    
    private int OrderID;
    private Date OrderDate;
    private double OrderTotal;
    private String Status;
    private String Username;
    private HashMap<Integer, OrderLine> orderLines;

    public Order() {
        OrderDate = new Date();
        OrderTotal = 0;
        Status = "Incomplete";
        Username = "";
        orderLines = new HashMap<>();
    }

    public Order(int OrderID, Date OrderDate, double OrderTotal, String Status) {
        this.OrderID = OrderID;
        this.OrderDate = OrderDate;
        this.OrderTotal = OrderTotal;
        this.Status = Status;
        orderLines = new HashMap<>();
    }   
   
    public void addOrderLine(OrderLine orderline)
    {
        boolean newProduct = false;
        int orderLineID = 0;
        
        for(Map.Entry<Integer, OrderLine> entry : orderLines.entrySet())
        {
            if(entry.getValue().getProduct().getProductID() == orderline.getProduct().getProductID())
            {
                orderLineID = entry.getValue().getOrderLineID();
            } else
            {
                newProduct = true;
                orderLines.put(orderLineID, orderline);
                //if(is registered){}
            }
        }
    }
    

    public HashMap<Integer, OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(HashMap<Integer, OrderLine> orderLines) {
        this.orderLines = orderLines;
    }
    
    public int generateUniqueOrderLineId()
    {
        //declare local variable for integer and set to 0
        int uniqueId = 0;
                
        // loop through order lines
        for(Map.Entry<Integer, OrderLine> entry : orderLines.entrySet())
        {
            // check if current order line key value = local variable and add one
            if(entry.getValue().getOrderLineID() == uniqueId)
            {
                uniqueId = uniqueId++;
            }
        }
        return uniqueId;        
    }
    
    public int getQuantityOfProducts(int productId)
    {
        // loop through order lines 
        for(Map.Entry<Integer, OrderLine> entry : orderLines.entrySet())
        {
            // check for matching product id
            if(entry.getValue().getProduct().getProductID() == productId)
            {
                // return quantity
                return entry.getValue().getQuantity();
            }
        }
        // return 0 if product not found
        return 0;
    }
    
    public void removeOrderLine(int productId, boolean isRegistered)            
    {
        Iterator<Map.Entry<Integer, OrderLine>> iter = orderLines.entrySet().iterator();
        while(iter.hasNext())
        {
            Map.Entry<Integer, OrderLine> entry = iter.next();
            if(entry.getValue().getProduct().getProductID() == productId)
            {
                iter.remove();
                OrderTotal = OrderTotal - entry.getValue().getLineTotal();
                if(isRegistered)
                {
                    try {
                        DBHandler db = new DBHandler();
                        db.deleteOrderLine(OrderID, productId);
                        db.updateOrderTotal(OrderID, -entry.getValue().getLineTotal());
                    } catch (SQLException ex) {
                        Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    
    public void addOrderLine(OrderLine ol, boolean isRegistered)
    {
        boolean isNewProduct = true;
        int orderLineId = 0;
        
        for(Map.Entry<Integer, OrderLine> entry: orderLines.entrySet())
        {
            if(entry.getValue().getProduct().getProductID() == ol.getProduct().getProductID())
            {
                isNewProduct = false;
                orderLineId = entry.getValue().getOrderLineID();
            }
        }
        
        if(isNewProduct)
        {
            orderLines.put(ol.getOrderLineID(), ol);
            int oldOrderLineId = ol.getOrderLineID();
            
            if(isRegistered)
            {
                try {
                    DBHandler db = new DBHandler();
                    orderLineId = db.addOrderLine(orderLineId, ol);
                    orderLines.remove(oldOrderLineId);
                    orderLines.put(orderLineId, ol);
                    orderLines.get(orderLineId).setOrderLineID(orderLineId);
                } catch (SQLException ex) {
                    Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else
            {
                int newOrderLineQuantity = orderLines.get(orderLineId).getQuantity() + ol.getQuantity();
                double newOrderLineTotal = orderLines.get(orderLineId).getLineTotal() + ol.getLineTotal();
                orderLines.get(orderLineId).setQuantity(newOrderLineQuantity);
                orderLines.get(orderLineId).setLineTotal(newOrderLineTotal);
                if(isRegistered)
                {
                    try {
                        DBHandler db = new DBHandler();
                        db.updateOrderLine(orderLineId, newOrderLineQuantity, newOrderLineTotal);
                        db.updateOrderTotal(OrderID, ol.getLineTotal());
                    } catch (SQLException ex) {
                        Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                OrderTotal = OrderTotal + ol.getLineTotal();
            }
        }
    }
    
    public int getOrderID() {
        return OrderID;
    }

    public Date getOrderDate() {
        return OrderDate;
    }

    public double getOrderTotal() {
        return OrderTotal;
    }

    public String getStatus() {
        return Status;
    }

    public void setOrderID(int OrderID) {
        this.OrderID = OrderID;
    }

    public void setOrderDate(Date OrderDate) {
        this.OrderDate = OrderDate;
    }

    public void setOrderTotal(double OrderTotal) {
        this.OrderTotal = OrderTotal;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }
    
    public void setUsername(String username) {
        this.Username = username;
    }
    

    @Override
    public String toString() {
        return "Order{" + "OrderID=" + OrderID + ", OrderDate=" + OrderDate + ", OrderTotal=" + OrderTotal + ", Status=" + Status + '}';
    }
    
    
    
}
