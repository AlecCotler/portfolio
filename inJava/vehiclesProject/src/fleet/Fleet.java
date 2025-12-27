package fleet;
import java.util.Scanner;
import util.Date;
import util.List;
import util.Sort;
/**
 * This class creates a dynamic array of Vehicle objects that allows to add, remove,
 * find, and a print sorted by make (then obtained date).
 *
 * @author Carly Chick
 */
public class Fleet extends List<Vehicle>{

    /**
     * Creates an empty fleet with an initial capacity of 4.
     */
    public Fleet() {
        super();
    }

    /**
     * Prints all vehicles in the fleet in order by location, make, then obtained date.
     * Sorting is done using selection sort.
     */
    public void printByMake() {
        Sort.sortByLocationMakeAndDate(this);
        for (Vehicle v : this) {
            System.out.println(v);
        }
    }

    /**
     * Returns the vehicle in the fleet equal to the given one.
     *
     * @param vehicle the vehicle to search for
     * @return the stored vehicle if found; null if not found
     */
    public Vehicle getVehicle(Vehicle vehicle) {
        int index = indexOf(vehicle);
        return (index == util.List.NOT_FOUND) ? null : get(index);
    }


    /**
     * Method to get the fleet
     *
     * @return the fleet array
     */
    public Vehicle[] getFleet() {
        Vehicle[] arr = new Vehicle[Size()];
        for (int i = 0; i < Size(); i++) {
            arr[i] = get(i);
        }
        return arr;
    }

    /**
     * Loads the vehicles into the fleet from vehicles.txt.
     *
     * @param scanner the scanner object
     * @return the amount of vehicles
     */
    public int load(Scanner scanner){
        int count = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                String[] parts = line.split("\\s+");
                Date date = new Date(parts[1]);
                Make make = Make.parseMake(parts[2]);
                int odo = Integer.parseInt(parts[3]);
                Campus campus = Campus.parseCampus(parts[4]);
                Vehicle vehicle;
                switch(parts[0].charAt(parts[0].length() - 1)){
                    case 'D':
                        vehicle = new Utility(parts[0],date,make,odo,campus);
                        break;
                    case 'X':
                        vehicle = new Truck(parts[0],date,make,odo,campus);
                        break;
                    case 'S':
                        vehicle = new Sedan(parts[0],date,make,odo,campus);
                        break;
                    default:
                        continue;
                }
                if(!contains(vehicle)){
                    count++;
                    add(vehicle);
                }
            }
        }
        return count;
    }
}
