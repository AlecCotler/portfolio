package fleet;
import util.Date;
/**
 * This class implements a specific type of vehicle (Utility) to calculate the charge and surcharge.
 *
 * @author Carly Chick
 */
public class Utility extends Vehicle{
    /**
     * Constructor to create a new Utility Vehicle with only the plate.
     *
     * @param plate license plate
     */
    public Utility(String plate) {
        super(plate);
    }

    /**
     * Constructor to create a new Utility Vehicle with all the instance variables in Vehicle.
     *
     * @param plate license plate
     * @param obtained obtained date
     * @param make Make
     * @param mileage the mileage
     * @param campus campus
     */
    public Utility(String plate, Date obtained, Make make, int mileage, Campus campus) {
        super(plate, obtained, make, mileage, campus);
    }

    /**
     * Calculates the charge for the Utility Vehicle.
     *
     * @param mileageUsed used mileage
     * @return the charge
     */
    @Override
    public double charge(int mileageUsed){
        return (mileageUsed * 1.99);
    }

    /**
     * Calculates the surcharge for the Utility Vehicle.
     *
     * @param mileageUsed used mileage
     * @param surcharge boolean to tell if surcharge or not
     * @return the surcharge; -1 otherwise
     */
    @Override
    public double surcharge(int mileageUsed, boolean surcharge){
        if(surcharge){
            return (.3 * mileageUsed < 35.99 ? (.3 * mileageUsed) : 35.99);
        }
        return -1;
    }
}
