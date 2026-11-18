package models;

public class Account {

    protected int id;
    protected int userId;
    protected String type;
    protected double balance;

    public Account(int id, int userId, String type, double balance) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.balance = balance;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getType() { return type; }
    public double getBalance() { return balance; }

    public void deposit(double amount) { this.balance += amount; }
    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
}
