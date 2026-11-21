package models;

public class Transaction {

    private int id;
    private int accountId;
    private String type;
    private double amount;
    private String date;           // matches DB column
    private double updatedBalance; // matches DB column

    public Transaction(int id, int accountId, String type, double amount, String date, double updatedBalance) {
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.updatedBalance = updatedBalance;
    }

    public int getId() { return id; }
    public int getAccountId() { return accountId; }
    public String getType() { return type; }
    public double getAmount() { return amount; }

    public String getDate() { return date; }  // FIXED

    public double getUpdatedBalance() { return updatedBalance; } // FIXED
}
