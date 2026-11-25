package models;

public class SavingsAccount extends Account implements InterestBearing {

    private static final double INTEREST_RATE = 0.05; 

    public SavingsAccount(int id, int userId, double balance) {
        super(id, userId, "Savings", balance);
    }

    @Override
    public double calculateInterest() {
        return balance * INTEREST_RATE;
    }
}
