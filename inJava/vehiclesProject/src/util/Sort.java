package util;
import fleet.Booking;
import fleet.Vehicle;
/**
 * This class creates a circularly linked list of trips.
 * Additionally, it inserts the trips in order of earliest end date, and can print the list/find the number of trips in the list.
 *
 * @author Alec Cotler
 */
public class Sort {

    /**
     * Just an empty constructor.
     */
    public Sort() {
    }

    /**
     * Sort a list of vehicles by their location, make, and then date.
     *
     * @param list , list of vehicles to be sorted
     */
    public static void sortByLocationMakeAndDate(List<Vehicle> list){
        int n = list.Size();
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (list.get(j).compareTo(list.get(min)) < 0) {
                    min = j;
                }
            }
            if (min != i) {
                Vehicle tmp = list.get(i);
                list.set(i, list.get(min));
                list.set(min, tmp);
            }
        }
    }

    /**
     * Sort a list of bookings by their location, plate, and then end date.
     * @param list, the list of bookings to be sorted.
     */
    public static void sortByLocationThenPlateThenDate(List<Booking> list) {
        for (int i = 1; i < list.Size(); i++) {
            Booking key = list.get(i);
            int index = i - 1;

            while (index >= 0 && (

                    list.get(index).getVehicle().getCampus().getCity().compareTo(key.getVehicle().getCampus().getCity()) > 0 ||

                            (list.get(index).getVehicle().getCampus().getCity().compareTo(key.getVehicle().getCampus().getCity()) == 0 &&
                    // compare plate alphabetically
                        list.get(index).getVehicle().getPlate().compareTo(key.getVehicle().getPlate()) > 0) ||

                            // if plates are equal, compare by beginDate
                            (list.get(index).getVehicle().getCampus().getCity().compareTo(key.getVehicle().getCampus().getCity()) == 0 && list.get(index).getVehicle().getPlate().equals(key.getVehicle().getPlate()) &&
                                    list.get(index).getBegin().compareTo(key.getBegin()) > 0)
            )) {
                list.set(index + 1, list.get(index));
                index--;
            }
            list.set(index + 1, key);
        }
    }

    /**
     * Sort a list of bookings by their department and then the employee.
     * @param list , List of bookings to be sorted.
     */
    public static void sortByDeptThenEmployee(List<Booking> list) {
        for (int i = 1; i < list.Size(); i++) {
            Booking key = list.get(i);
            int index = i - 1;
            while (index >= 0 && (
                    list.get(index).getEmployee().getDept().name()
                            .compareTo(key.getEmployee().getDept().name()) > 0 ||
                            (list.get(index).getEmployee().getDept().name()
                                    .equals(key.getEmployee().getDept().name()) &&
                                    list.get(index).getEmployee().name()
                                            .compareTo(key.getEmployee().name()) > 0)
            )) {
                list.set(index + 1, list.get(index));
                index--;
            }
            list.set(index + 1, key);
        }
    }


}