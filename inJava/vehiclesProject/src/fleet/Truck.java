package fleet;
import util.Date;
/**
 * This class implements a specific type of vehicle (Truck) to calculate the charge and surcharge.
 *
 * @author Carly Chick
 */
public class Truck extends Vehicle{
    /**
     * Constructor to create a new Truck Vehicle with only the plate.
     *
     * @param plate license plate
     */
    public Truck(String plate) {
        super(plate);
    }

    /**
     * Constructor to create a new Truck Vehicle with all the instance variables in Vehicle.
     *
     * @param plate license plate
     * @param obtained obtained date
     * @param make Make
     * @param mileage the mileage
     * @param campus campus
     */
    public Truck(String plate, Date obtained, Make make, int mileage, Campus campus) {
        super(plate, obtained, make, mileage, campus);
    }

    /**
     * Calculates the charge for the Truck Vehicle.
     *
     * @param mileageUsed used mileage
     * @return the charge
     */
    @Override
    public double charge(int mileageUsed){
        return (mileageUsed * 2.99);
    }

    /**
     * Calculates the surcharge for the Truck Vehicle.
     *
     * @param mileageUsed used mileage
     * @param surcharge boolean to tell if surcharge or not
     * @return the surcharge; -1 otherwise
     */
    @Override
    public double surcharge(int mileageUsed, boolean surcharge){
        if(surcharge){
            return 39.99;
        }
        return -1;
    }
}
