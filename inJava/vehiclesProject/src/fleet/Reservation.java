package fleet;
import util.List;
import util.Sort;
/**
 * This class creates an array of bookings, where if the array is full adds 4 more spaces to the array
 * Then, additional lines of description are added to elaborate on the
 * details if necessary.
 *
 * @author Alec Cotler
 */
public class Reservation extends List<Booking>{


    /**
     * Constructor for a new reservation.
     */
    public Reservation() {
        super();
    }

    /**
     * Prints the reservation list in the order of the location then plates of the vehicle, then beginning date of the reservation.
     */
    public void printByVehicle() {
        Sort.sortByLocationThenPlateThenDate(this);
        for (int i = 0; i < Size(); i++) {
            System.out.println(get(i));
        }

    }

    /**
     * Prints the reservation list ordered by department then by employee (alphabetically).
     */
    public void printByDept() {
        Sort.sortByDeptThenEmployee(this);
        Department dept = get(0).getEmployee().getDept();
        System.out.println("--" + dept.convertToWords() + "--");
        for (int i = 0; i < Size(); i++) {
            if(!dept.name().equals(get(i).getEmployee().getDept().name())){
                dept = get(i).getEmployee().getDept();
                System.out.println("--" + dept.convertToWords() + "--");
            }
            System.out.println("\t" + get(i));
        }
    }

    /**
     * Sees if the booking is in the reservation list, and returns the booking if it is or null if not.
     *
     * @param booking to check if it is in the reservation list
     * @return booking if it is in the reservation list
     */
    public Booking bookingLookup(Booking booking) {
        if(booking == null){
            return null;
        }
        int index = indexOf(booking);
        if(index == -1){
            return null;
        }
        return get(index);
    }

    /**
     * Finds if a vehicle and ending date combo is in any of the bookings in the reservation list.
     *
     * @param booking to see if its vehicle and end date combo exists in the reservation list
     * @return true if the vehicle and end date combo exists in the reservation list, false if not
     */
    public boolean findInstanceOfVehicle(Booking booking){
        if (booking == null) {
            return false;
        }
        for (int i = 0; i < Size(); i++) {
            if (get(i).equalsNoBeginDate(booking)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds if a vehicle is in any of the bookings in the reservation list.
     *
     * @param booking the booking to see if its vehicle exists in the reservation list
     * @return true if a vehicle is in the reservation list, false if not
     */
    public boolean findInstanceOfVehicleOnlyPlate(Booking booking){
        if (booking == null) {
            return false;
        }
        for (int i = 0; i < Size(); i++) {
            if (get(i).equalsNoDate(booking)) {
                return true;
            }
        }
        return false;
    }

    /**
     * returns an array from our implementation of a list.
     *
     * @return out ,the booking array created.
     */
    public Booking[] toArray() {
        Booking[] out = new Booking[Size()];
        for (int i = 0; i < Size(); i++) out[i] = get(i);
        return out;
    }
}

