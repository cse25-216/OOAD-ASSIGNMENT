package models;

public class ChequingAccount extends Account implements Withdrawable {

    public ChequingAccount(int id, int userId, double balance) {
        super(id, userId, "Chequing", balance);
    }

    @Override
    public boolean withdraw(double amount) {
        if (!Double.isFinite(amount) || amount <= 0 || amount > getBalance()) {
            return false;
        }

        return super.withdraw(amount);
    }
}
