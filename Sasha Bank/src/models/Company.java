package models;

public class Company {

    private int id;
    private String name;
    private String businessId;
    private String registrationNumber;
    private String registrationDate;
    private double startingCapital;

    public Company(int id, String name, String businessId, String registrationNumber,
                   String registrationDate, double startingCapital) {

        this.id = id;
        this.name = name;
        this.businessId = businessId;
        this.registrationNumber = registrationNumber;
        this.registrationDate = registrationDate;
        this.startingCapital = startingCapital;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getBusinessId() { return businessId; }
    public String getRegistrationNumber() { return registrationNumber; }
    public String getRegistrationDate() { return registrationDate; }
    public double getStartingCapital() { return startingCapital; }
}
