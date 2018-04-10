/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 30211275
 */
public class Customer extends User {
    
    private String AddressLine1;
    private String AddressLine2;
    private String Town;
    private String Postcode;
    private HashMap<Integer, Order> orders;
    private boolean isRegistered = false;

    public Customer() {
        super();
        orders = new HashMap<>();
        isRegistered = true;
    }

    public Customer(String Username, String Password, String FirstName, String LastName, String AddressLine1, String AddressLine2, String Town, String Postcode  ) {
        super(Username, Password, FirstName, LastName);
        this.AddressLine1 = AddressLine1;
        this.AddressLine2 = AddressLine2;
        this.Town = Town;
        this.Postcode = Postcode;
        orders = new HashMap<>();
        isRegistered = true;
    }

    public String getAddressLine1() {
        return AddressLine1;
    }

    public String getAddressLine2() {
        return AddressLine2;
    }

    public String getTown() {
        return Town;
    }

    public String getPostcode() {
        return Postcode;
    }

    public void setAddressLine1(String AddressLine1) {
        this.AddressLine1 = AddressLine1;
    }

    public void setAddressLine2(String AddressLine2) {
        this.AddressLine2 = AddressLine2;
    }

    public void setTown(String Town) {
        this.Town = Town;
    }

    public void setPostcode(String Postcode) {
        this.Postcode = Postcode;
    }

    public boolean getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(boolean isRegistered) {
        this.isRegistered = isRegistered;
    }

    public HashMap<Integer, Order> getOrders() {
        return orders;
    }

    public boolean isIsRegistered() {
        return isRegistered;
    }

    public void setOrders(HashMap<Integer, Order> orders) {
        this.orders = orders;
    }
    
    
    
    
    public void addOrder(Order newOrder)
    {
        try {
                int id = newOrder.getOrderID();
                orders.put(id, newOrder);
                if(isRegistered)
                {
                    DBHandler db = new DBHandler();
                    int orderid = db.addOrder(this.getUsername(), newOrder);
                    orders.remove(id);
                    orders.put(orderid, newOrder);
                    orders.get(orderid).setOrderID(orderid);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Order findLatestOrder()
    {
        Order lastOrder = new Order();
        if(orders.isEmpty())
        {
            addOrder(lastOrder);
        } else 
        {            
            lastOrder = orders.entrySet().iterator().next().getValue();
            for(Map.Entry<Integer,Order> entry: orders.entrySet())             
            {
                Order ord = entry.getValue();
                if(entry.getValue().getOrderDate().after(lastOrder.getOrderDate()))
                {
                    lastOrder = entry.getValue();
                }
            }
            
            if(lastOrder.getStatus().equals("Complete"))
            {
                lastOrder = new Order();
                addOrder(lastOrder);
            }
        }
        return lastOrder;
    }
    
    public HashMap<Integer, Order> getCompleteOrders()
    {
        // hashmap for storing completed orders
        HashMap<Integer, Order> completeOrders = new HashMap<Integer, Order>();        
        // look through hashmap, check status, and add to complete orders hashmap if the status is complete 
        for(Map.Entry<Integer,Order> entry: orders.entrySet())
            {
                // check status is complete
                if(entry.getValue().getStatus().equals("Complete"))
                {
                    // add order to completed orders
                    completeOrders.put(entry.getKey(), entry.getValue());
                }
            }
        return completeOrders;
    }

    @Override
    public String toString() {
        return "Customer{" + "AddressLine1=" + AddressLine1 + ", AddressLine2=" + AddressLine2 + ", Town=" + Town + ", Postcode=" + Postcode + '}';
    }
    
    public String displayGreeting()
    {
        return "Welcome " + getFirstName() + " " + getLastName() + "\nEnjoy Shopping!";
    }
    
}
