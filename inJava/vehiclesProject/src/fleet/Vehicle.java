package fleet;
import util.Date;
/**
 * The class represents a vehicle with a license plate, obtained date, make, mileage, and pickup campus.
 * Also has methods for comparison, equality checks, and string representation.
 *
 * @author Carly Chick
 */
public abstract class Vehicle implements Comparable<Vehicle> {
    private String plate;
    private Date obtained;
    private Make make;
    private int mileage;

    protected Campus campus;

    /**
     * Creates a Vehicle with the given license plate.
     *
     * @param plate the license plate identifier for the vehicle
     */
    public Vehicle(String plate) {
        this.plate = plate;
    }

    /**
     * Creates a Vehicle with the given license plate, obtained date, make, and mileage.
     *
     * @param plate the license plate identifier for the vehicle
     * @param obtained the date the vehicle was acquired
     * @param make the manufacturer or brand of the vehicle
     * @param mileage the mileage (in miles) recorded for the vehicle
     */
    public Vehicle(String plate, Date obtained, Make make, int mileage, Campus campus) {
        this.plate = plate;
        this.obtained = obtained;
        this.make = make;
        this.mileage = mileage;
        this.campus = campus;
    }

    /**
     * Finds the type of vehicle based off license plate.
     *
     * @param plate the license plate
     * @return the corresponding type of vehicle string; null otherwise
     */
    private String findType(String plate){
        switch(plate.charAt(plate.length() - 1)){
            case 'D':
                return "Utility";
            case 'X':
                return "Truck";
            case 'S':
                return "Sedan";
            default:
                System.out.println("Unknown vehicle type: " + plate);
                return null;
        }
    }

    /**
     * Checks if two vehicles are equal by their license plates.
     *
     * @param obj the reference object with which to compare.
     * @return true if the plates are not null and their license plates are the same; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        Vehicle other = (Vehicle) obj;
        return this.plate != null && this.plate.equals(other.plate);
    }

    /**
     * Method that creates a String that shows the information of the Vehicle.
     *
     * @return a string explaining all the vehicle information
     */
    @Override
    public String toString() {
        String type = this.findType(plate);
        return this.plate + "[" + this.make + ":" + type.toLowerCase() + "] " + this.obtained + " [mileage:" + this.mileage + "] " +
                "[" + this.campus +
                ":" + this.campus.getCity() + "]";
    }

    /**
     * Compares "this" vehicle to another vehicle (passed in parameter) based on location, Make, and then obtained date.
     *
     * @param other the Vehicle to compare to
     * @return 0 if the location, Make, and obtained date are the same;
     * -1 if this Vehicle's location comes before the other Vehicle's location alphabetically,
     * or if this Vehicle's Make comes before the other Vehicle's Make alphabetically,
     * or if the Makes are equal but this Vehicle's obtained date is earlier;
     * 1 if this Vehicle's location comes after the other Vehicle's location alphabetically,
     * or if this Vehicle's Make comes after the other Vehicle's Make alphabetically,
     * or if the Makes are equal but this Vehicle's obtained date is later;
     * Null pointer exception if the given Vehicle is null
     */
    @Override
    public int compareTo(Vehicle other) {
        if (other == null) {
            throw new NullPointerException("Cannot compare to null Vehicle");
        }
        int byLocation = Campus.compareStringsIgnoreCase(this.campus.getCity(), other.campus.getCity());
        if (byLocation != 0) {
            return byLocation < 0 ? -1 : 1;
        }
        int byMake = Make.compareStringsIgnoreCase(this.getMake().name(), other.getMake().name());
        if (byMake != 0) {
            return byMake < 0 ? -1 : 1;
        }
        int byDate = this.getObtained().compareTo(other.getObtained());
        if (byDate != 0) {
            return byDate < 0 ? -1 : 1;
        }
        return 0;
    }

    /**
     * Abstract method to be implemented in the vehicle type classes to calculate the charge.
     *
     * @param mileageUsed used mileage
     * @return the charge
     */
    public abstract double charge(int mileageUsed);

    /**
     * Abstract method to be implemented in the vehicle type classes to calculate the surcharge if there is any.
     *
     * @param mileageUsed used mileage
     * @param surcharge boolean to tell if surcharge or not
     * @return the surcharge
     */
    public abstract double surcharge(int mileageUsed, boolean surcharge);

    /**
     * Method to get the license plate.
     *
     * @return the license plate
     */
    public String getPlate() {
        return plate;
    }

    /**
     * Method to get the obtained date.
     *
     * @return the obtained date
     */
    public Date getObtained() {
        return obtained;
    }

    /**
     * Method to get the Make.
     *
     * @return the Make
     */
    public Make getMake() {
        return make;
    }

    /**
     * Method to get the mileage.
     *
     * @return the mileage
     */
    public int getMileage() {
        return mileage;
    }

    /**
     * Method to set a new mileage.
     *
     * @param mileage the new mileage to be set
     */
    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    /**
     * Method to get the pickup campus
     *
     * @return the campus
     */
    public Campus getCampus() {
        return campus;
    }

    /**
     * Sets to a new campus.
     * @param campus the new campus
     */
    public void setCampus(Campus campus) {
        this.campus = campus;
    }
}
