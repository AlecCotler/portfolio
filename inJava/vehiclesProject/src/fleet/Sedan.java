package fleet;
import util.Date;
/**
 * This class implements a specific type of vehicle (Sedan) to calculate the charge and surcharge.
 *
 * @author Carly Chick
 */
public class Sedan extends Vehicle{

    /**
     * Constructor to create a new Sedan Vehicle with only the plate.
     *
     * @param plate license plate
     */
    public Sedan(String plate) {
        super(plate);
    }

    /**
     * Constructor to create a new Sedan Vehicle with all the instance variables in Vehicle.
     *
     * @param plate license plate
     * @param obtained obtained date
     * @param make Make
     * @param mileage the mileage
     * @param campus campus
     */
    public Sedan(String plate, Date obtained, Make make, int mileage, Campus campus) {
        super(plate, obtained, make, mileage, campus);
    }

    /**
     * Calculates the charge for the Sedan Vehicle.
     *
     * @param mileageUsed used mileage
     * @return the charge
     */
    @Override
    public double charge(int mileageUsed){
        return (mileageUsed * 1.79);
    }

    /**
     * Calculates the surcharge for the Sedan Vehicle.
     *
     * @param mileageUsed used mileage
     * @param surcharge boolean to tell if surcharge or not
     * @return the surcharge; -1 otherwise
     */
    @Override
    public double surcharge(int mileageUsed, boolean surcharge){
        if(surcharge){
            return (.25 * mileageUsed < 32.99 ? (.25 * mileageUsed) : 32.99);
        }
        return -1;
    }
}
