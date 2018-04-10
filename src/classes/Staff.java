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
public class Staff extends User {
    
    private double Salary;
    private String Position;

    public Staff() {
        super();
    }

    public Staff(double Salary, String Position, String Username, String Password, String FirstName, String LastName) {
        super(Username, Password, FirstName, LastName);
        this.Salary = Salary;
        this.Position = Position;
    }

    public double getSalary() {
        return Salary;
    }

    public String getPosition() {
        return Position;
    }

    public void setSalary(double Salary) {
        this.Salary = Salary;
    }

    public void setPosition(String Position) {
        this.Position = Position;
    }

    @Override
    public String toString() {
        return "Staff{" + "Salary=" + Salary + ", Position=" + Position + '}';
    }
    
    public String displayGreeting()
    {
        return "Welcome " + getFirstName() + " " + getLastName() + "\nYou are logged in as Staff!";
    }
    
}
