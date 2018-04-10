/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 30211275
 */
public class DBHandler {
    
    private Connection dbCon;
    //private String conStr= "jdbc:ucanaccess://H:/ShopDB.accdb";
    private String conStr= "jdbc:ucanaccess://C:\\Users\\Paul\\Pictures\\college\\OO Programming\\CityShop\\src\\db\\ShopDB.accdb";

    public DBHandler() throws SQLException, ClassNotFoundException{
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        //this.dbCon= DriverManager.getConnection("jdbc:ucanaccess://H:/ShopDB.accdb");
        this.dbCon= DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\Paul\\Pictures\\college\\OO Programming\\CityShop\\src\\db\\ShopDB.accdb");

    }
    
    public void deleteOrderLine(int orderid, int productid)
    {        
        try {
            Statement stmt = dbCon.createStatement();
            String sql = "DELETE from ORDERLINES where ProductID = " + productid + " AND OrderId = " + orderid;
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteProduct(Product product)
    {
        try
        {
            Statement stmt = dbCon.createStatement();
            stmt.executeUpdate("DELETE FROM Products WHERE ProductId = '" + product.getProductID() + "'");
        }
        catch(Exception ex)
        {
            String message = ex.getMessage();
        }
    }
    
    public void deleteCustomer(Customer c)
    {
        try
        {
            Statement stmt = dbCon.createStatement();
            stmt.executeUpdate("DELETE FROM Customers WHERE EmailAddress = '" + c.getUsername() + "'");
        }
        catch(Exception ex)
        {
            String message = ex.getMessage();
        }
    }
    
    public void updateOrderLine(int orderLineId, int newOrderLineQuantity, double newOrderLineTotal)
    {        
        try {
            Statement stmt = dbCon.createStatement();
            String sql = "UPDATE ORDERLINES SET LineTotal = " + newOrderLineTotal + ",  Quantity = " + newOrderLineQuantity + "WHERE OrderId = " + orderLineId;
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public int addOrder(String username, Order order)
    {
            // DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            DateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            int orderId = 0;
        try {
            Statement stmt = dbCon.createStatement();
            stmt.executeUpdate("INSERT INTO Orders (OrderDate, Username, OrderTotal, Status) " +
                    "VALUES ('" + dateFormat.format(date) + "','" + 
                    username + "','" + order.getOrderTotal() + "','" +
                    order.getStatus() + "')");
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next())
            {
                orderId = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
            return orderId;
    }
    
    public int addOrderLine(int orderId, OrderLine orderLine)
    {
        try {
            int orderLineId = 0;
            Statement stmt = dbCon.createStatement();
            String sql = "INSERT INTO OrderLines (OrderLineId,ProductId , Quantity ,LineTotal, OrderId) VALUES("
                    +orderLine.getOrderLineID()+","
                    +orderLine.getProduct().getProductID()+","
                    +orderLine.getQuantity()+","
                    +orderLine.getLineTotal()+","
                    +orderId+")";
            
            stmt.executeUpdate(sql);
            updateOrderTotal(orderId, orderLine.getLineTotal());
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next())
            {
                orderLineId = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
            return orderId;
    }
    
    public void addProduct(Product newProduct)
    {
        
        String measurement = "";
        int size =0;
        
        if(newProduct.getClass().getName().equals("shoplog.Clothing"))
        {
            Clothing newClothing = (Clothing)newProduct;
            measurement = newClothing.getMeasurement();
        }
        else
        {
            Footwear newFootWear = (Footwear)newProduct;
            size = newFootWear.getSize();
        }
        
        try
        {
            
            Statement stmt = dbCon.createStatement();
            stmt.executeUpdate("INSERT INTO Products (ProductId, ProductName, Price, StockLevel, Size, Measurement) " +
                    "VALUES ('" + newProduct.getProductID() + "','" + newProduct.getProductName() + "','" + newProduct.getPrice() + "','" + 
                    newProduct.getStockLevel() + "','" + size + "','" + measurement +"')");
            
        }
        catch(Exception ex)
        {
            String message = ex.getMessage();
        }
    }
    
    public void updateOrderTotal(int orderid, double orderLinetotal)
    {        
        try {
            Statement stmt = dbCon.createStatement();
            String sql = "UPDATE ORDERS SET OrderTotal = " + orderLinetotal + "," + "WHERE OrderId = " + orderid;
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void updateProduct(Product p) throws SQLException
    {        
        String sqlstr;
        Statement st = dbCon.createStatement();
        ResultSet rs = null;
        
        if(p.getClass().getName().equals("classes.Footwear"))
        {
            
            sqlstr = "UPDATE Products set productname = ?, price = ?, stocklevel = ?, size = ? WHERE productID = ?";
            PreparedStatement ps = dbCon.prepareStatement(sqlstr);
            
            Footwear fw = (Footwear) p;
            
                ps.setString(1, fw.getProductName());
                ps.setDouble(2, fw.getPrice());
                ps.setInt(3, fw.getStockLevel());
                ps.setInt(4, fw.getSize());
                ps.setInt(5, fw.getProductID());              
            
            
            int i = ps.executeUpdate();
        }
        else if(p.getClass().getName().equals("classes.Clothing"))
        {
            
            sqlstr = "UPDATE Products set productname = ?, price = ?, stocklevel = ?, measurement = ? WHERE productID = ?";
            PreparedStatement ps = dbCon.prepareStatement(sqlstr);
            
            Clothing cl = (Clothing) p;
            
                ps.setString(1, cl.getProductName());
                ps.setDouble(2, cl.getPrice());
                ps.setInt(3, cl.getStockLevel());
                ps.setString(4, cl.getMeasurement());
                ps.setInt(5, cl.getProductID());            
            
        int i = ps.executeUpdate();
        }       
        
    }
    
    
    public void updateCustomer(Customer c) throws SQLException 
    {
        String sqlstr = "Update customers Set username = ?, password = ?, firstname = ?, lastname = ?, addressline1 = ?, addressline2 = ?, town = ?, postcode = ? Where username = ?";
        Statement st = dbCon.createStatement();
        ResultSet rs = null;
        PreparedStatement ps = dbCon.prepareStatement(sqlstr);
    
        ps.setString(1, c.getUsername());
        ps.setString(2, c.getPassword());
        ps.setString(3, c.getFirstName());
        ps.setString(4, c.getLastName());
        
        ps.setString(5, c.getAddressLine1());
        ps.setString(6, c.getAddressLine2());
        ps.setString(7, c.getTown());
        ps.setString(8, c.getPostcode());
        
        int i = ps.executeUpdate();
        
        if(i > 0) {
            System.out.println("Customer details updated.");
        }
        else
        {
            System.out.println("Could not find details.");
        }
        
    }
    
    
    public void completeOrder(int orderid)
    {        
        try {
            Statement stmt = dbCon.createStatement();
            String sql = "UPDATE ORDERS SET STATUS = 'Complete' WHERE OrderId = " + orderid;
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   
    public HashMap<String, Customer> loadCustomers()
    {
        HashMap<String, Customer> customers = new HashMap();
        
        try {
            Statement stmt = dbCon.createStatement();
            String sql = "SELECT * from CUSTOMERS";            
            
            ResultSet rs = stmt.executeQuery(sql);
            
            Customer newCustomer = new Customer();             
            
            while(rs.next())
            {
                newCustomer.setUsername(rs.getString("Username"));
                newCustomer.setFirstName(rs.getString("FirstName"));
                newCustomer.setLastName(rs.getString("LastName"));
                newCustomer.setPassword(rs.getString("Password"));
                newCustomer.setAddressLine1(rs.getString("AddressLine1"));
                newCustomer.setAddressLine2(rs.getString("AddressLine2"));
                newCustomer.setTown(rs.getString("Town"));
                newCustomer.setPostcode(rs.getString("Postcode"));
                
                customers.put(rs.getString("Username"), newCustomer);            
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customers;
    }
    
    public HashMap<String, Customer> loadCustomerOrders()
    {
        try {
            Statement stmt = dbCon.createStatement();
            String sql = "SELECT * from ORDERS";            
            
            ResultSet rs = stmt.executeQuery(sql);
                        
            while(rs.next())
            {
                int orderid = rs.getInt("OrderID");               
                Date orderdate = rs.getDate("OrderDate");
                String username = rs.getString("Username");
                int ordertotal = rs.getInt("OrderTotal");
                String status = rs.getString("Status");    
                
                HashMap<String,Customer> theCustomers = loadCustomers();
                
                Customer cust = theCustomers.get(username);
                // NEED TO ADD ORDER TO CUSTOMER
                // put order in customer
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    

    public void Close()
    {
        try {
            dbCon.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Customer CustomerLogin(String username, String password)
    {
        try {
            String sql = ("select * from Customers where username ='"+username+"' and password ='"+password+"'");
            Statement stmt = dbCon.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            if(rs.next())
            {
                Customer c = new Customer();
                c.setFirstName(rs.getString("FirstName"));
                c.setLastName(rs.getString("LastName"));
                c.setUsername(rs.getString("Username"));
                c.setPassword(rs.getString("Password"));
                c.setAddressLine1(rs.getString("AddressLine1"));
                c.setAddressLine2(rs.getString("AddressLine2"));
                c.setTown(rs.getString("Town"));
                c.setPostcode(rs.getString("Postcode"));
                return c;
            } else {
                return null;
            }
                    
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return null;        
    }
    
    public Staff StaffLogin(String username, String password)
    {
        try {
            String sql = ("select * from Staff where username ='"+username+"' and password ='"+password+"'");
            Statement stmt = dbCon.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            if(rs.next())
            {
                Staff s = new Staff();
                s.setFirstName(rs.getString("FirstName"));
                s.setLastName(rs.getString("LastName"));
                s.setUsername(rs.getString("Username"));
                s.setPassword(rs.getString("Password"));
                s.setSalary(rs.getDouble("Salary"));
                s.setPosition(rs.getString("Position"));
                return s;
            } else {
                return null;
            }
                    
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return null;        
    }
    
    public HashMap LoadProducts()
    {
        HashMap<Integer, Product> products = new HashMap();
        
        String sql = "Select * from products";
      
        try {
          
           Statement stmt = dbCon.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            
            while ( rs.next()){
              //check if measurement empty or null
                
                if (!rs.getString("Measurement").isEmpty()&& rs.getString("Measurement") != null ){                    
                    Clothing cl = new Clothing(rs.getString("Measurement"), rs.getInt("ProductID"), rs.getString("ProductName"), rs.getDouble("Price"), rs.getInt("StockLevel"));
                    products.put(cl.getProductID(), cl);
                }else {
                    Footwear fw = new Footwear(rs.getInt("Size"), rs.getInt("ProductID"), rs.getString("ProductName"), rs.getDouble("Price"), rs.getInt("StockLevel"));
                    products.put(fw.getProductID(), fw);
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return products;
    }
    
    
    public HashMap<Integer, Order> loadOrders(String user) throws SQLException 
    {
        HashMap<Integer, Order> orders = new HashMap<>();
        String sqlstr = "";
        int count = 0;
        
        sqlstr = "select * from orders where username = '" + user + "'";
        
        if(user.equals(""))
        {
            sqlstr = "select * from orders";
        }
        
        Statement st = dbCon.createStatement();
        ResultSet rs = null;
        
        rs = st.executeQuery(sqlstr);
        
        while(rs.next()) 
        {
            Order order = new Order();
            order.setOrderDate(rs.getDate("OrderDate"));
            order.setOrderID(rs.getInt("OrderID"));
            order.setOrderTotal(rs.getDouble("OrderTotal"));
            order.setUsername(rs.getString("Username"));
            order.setStatus(rs.getString("Status"));
            
            //orders.put(count, order
            orders.put(rs.getInt("OrderID"), order);
            count++;
        }
        
        return orders;
    }
    
    public HashMap<String, Customer> loadOrderLines (HashMap<String, Customer> customers)
    {
        HashMap<Integer, Product> products = LoadProducts();
        try
        {
          
            Statement stmt = dbCon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM OrderLines");
            while(rs.next())
            {
                int orderLineId = rs.getInt("OrderLineId");
                int productId = rs.getInt("ProductId");
                int quantity = rs.getInt("Quantity");
                double lineTotal = rs.getDouble("LineTotal");
                int orderId = rs.getInt("OrderId");
                
                Product loadedProduct = products.get(productId);

                for(Map.Entry<String, Customer> customerEntry : customers.entrySet())
                {
                    Customer customer = customerEntry.getValue();
                    if(customer.getOrders().containsKey(orderId))
                    {
                        OrderLine loadedOrderLine = 
                                new OrderLine(orderLineId,quantity, lineTotal, loadedProduct);
                        
                        customer.getOrders().get(orderId).getOrderLines().put(orderLineId, loadedOrderLine);
                    }                                    
                }
            }
            
        }
        catch(Exception ex)
        {
            String message = ex.getMessage();
        }
        finally
        {
            return customers;
        }
    }
    
    
    public Boolean registerCustomer ( Customer c )
    {
        try {
            Statement stmt = dbCon.createStatement();
            String sql = "Select * from customers where username = '" + c.getUsername() + "'";
            ResultSet rs = stmt.executeQuery(sql);
            
            if(rs.next())
            {
                return false;
            } else
            {
                sql = "insert into customers (username, password, firstname, lastname, addressline1, addressline2, town, postcode) values (?,?,?,?,?,?,?,?)";
                PreparedStatement ps = dbCon.prepareStatement(sql);
                ps.setString(1, c.getUsername());
                ps.setString(2, c.getPassword());
                ps.setString(3, c.getFirstName());
                ps.setString(4, c.getLastName());
                ps.setString(5, c.getAddressLine1());
                ps.setString(6, c.getAddressLine2());
                ps.setString(7, c.getTown());
                ps.setString(8, c.getPostcode());
               
                //String[] args = {c.getUsername(),c.getPassword(), c.getFirstName(), c.getLastName(),c.getAddressLine1(),c.getAddressLine2(),c.getTown(),c.getPostcode()};
                int n = ps.executeUpdate();
                ps.close();
                if (n > 0)
                {
                    return true;
                }                
                
            }
        
            
            
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
            
    }
    
    
}
