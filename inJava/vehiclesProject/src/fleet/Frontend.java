package fleet;
import util.Date;
import java.util.Scanner;
import java.util.Calendar;
import java.io.File;
import java.io.FileNotFoundException;
/**
 * Console based frontend for the Vehicle Management System.
 *
 * @author Carly Chick
 */
public class Frontend {
    private boolean running = true;
    private final Fleet fleet = new Fleet();
    private final Reservation reservation = new Reservation();
    private final TripList tripList = new TripList();

    /**
     * Constructs a new Frontend object to manage the vehicle system.
     */
    public Frontend() {

    }

    /**
     * Stops the program loop and prints a termination message.
     */
    private void terminate(){
        System.out.println("Vehicle Management System is terminated.");
        running = false;
    }

    /**
     * Routes a command to the appropriate handler.
     *
     * @param cmd the command token
     * @param parts the tokenized input line from the console
     */
    private void processCommand(String cmd, String[] parts){
        switch(cmd){
            case "L":
                loadVehicles();
                break;
            case "A":
                addToFleet(parts);
                break;
            case "D":
                removeFromFleet(parts);
                break;
            case "B":
                bookVehicle(parts);
                break;
            case "C":
                cancelBooking(parts);
                break;
            case "R":
                returnVehicle(parts);
                break;
            case "PF":
                displayVehicles();
                break;
            case "PR":
                displayBookings1();
                break;
            case "PD":
                displayBookings2();
                break;
            case "PT":
                displayCompletedTrips();
                break;
            case "PC":
                displayCostReport();
                break;
            default:
                System.out.println(cmd + " - invalid command!");
        }
    }

    /**
     * Finds today's date using the Calendar library and returns it as a Date.
     *
     * @return a Date representing the current day (mm/dd/yyyy)
     */
    private Date getToday(){
        Calendar calendar = Calendar.getInstance();
        String todayString = (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH)
                + "/" + calendar.get(Calendar.YEAR);
        return new Date(todayString);
    }

    /**
     * Validates an odometer reading.
     *
     * @param odo the odometer value to check
     * @return true if odo is a positive int; false otherwise
     */
    private boolean isValidOdometer(int odo) {
        try {
            return odo > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Converts a Date to a Calendar set to midnight.
     *
     * @param date the date to convert
     * @return a Calendar set to obj's year/month/day at 00:00:00
     */
    private Calendar convertDateToCalendar(Date date){
        int month = date.getMonth() - 1;
        int day  = date.getDay();
        int year = date.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * Finds whole day difference between two dates, ignoring time-of-day.
     *
     * @param startingDate the start date
     * @param endingDate the end date
     * @return number of days between end and start; may be negative if endingDate less than or equal to startingDate;
     * returns 0 if either argument is null
     */
    private int daysBetween(Date startingDate, Date endingDate){
        if (startingDate == null || endingDate == null) return 0;
        Calendar start = convertDateToCalendar(startingDate);
        Calendar end   = convertDateToCalendar(endingDate);

        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);

        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);

        long diffMillis = end.getTimeInMillis() - start.getTimeInMillis();
        return (int) (diffMillis / (24L * 60 * 60 * 1000));
    }

    /**
     * Returns whether two closed date ranges overlap.
     * The ranges are inclusive.
     *
     * @param aStart start of the first range
     * @param aEnd end of the first range
     * @param bStart start of the second range
     * @param bEnd end of the second range
     * @return true if the two ranges overlap at any point; false otherwise
     */
    private boolean rangesOverlapInclusive(Date aStart, Date aEnd, Date bStart, Date bEnd) {
        return aStart.compareTo(bEnd) <= 0 && aEnd.compareTo(bStart) >= 0;
    }

    /**
     * Checks if a vehicle is available for a date range by checking all bookings
     * for that vehicle and seeing if any overlap with the range.
     *
     * @param vehicle the vehicle to check
     * @param reqStart requested start date
     * @param reqEnd requested end date
     * @return true if no existing booking for the vehicle overlaps the range; false otherwise
     */
    private boolean isVehicleAvailable(Vehicle vehicle, Date reqStart, Date reqEnd) {
        Booking[] all = reservation.toArray();
        if (all == null) return true;
        for (int i = 0; i < all.length; i++) {
            Booking b = all[i];
            if (b == null) continue;
            if (!vehicle.equals(b.getVehicle())) continue;
            if (rangesOverlapInclusive(reqStart, reqEnd, b.getBegin(), b.getEnd())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the employee has any of their own bookings that overlap
     * a date range. Used to prevent self conflicts.
     *
     * @param reqStart requested start date
     * @param reqEnd requested end date
     * @param employee the employee name to match against bookings
     * @return true if the employee has no overlapping bookings; false otherwise
     */
    private boolean isVehicleAvailableSelf(Date reqStart, Date reqEnd, String employee) {
        Booking[] all = reservation.toArray();
        if (all == null) return true;
        for (int i = 0; i < all.length; i++) {
            Booking b = all[i];
            if (b == null) continue;
            if (!b.getEmployee().name().equalsIgnoreCase(employee)) continue;
            if (rangesOverlapInclusive(reqStart, reqEnd, b.getBegin(), b.getEnd())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Computes the date exactly three calendar months from "today".
     *
     * @return a Date showing the day three months after the current day
     */
    private Date getThreeMonthsFromToday(){
        Calendar calendar = Calendar.getInstance(); // today
        calendar.add(Calendar.MONTH, 3); // add 3 months
        String threeMonthsLater = (calendar.get(Calendar.MONTH) + 1) + "/"
                + calendar.get(Calendar.DAY_OF_MONTH) + "/"
                + calendar.get(Calendar.YEAR);
        return new Date(threeMonthsLater);
    }

    /**
     * Moves a completed reservation into the trip list.
     * Removes the booking from reservations, then updates vehicle mileage.
     *
     * @param originalBooking the completed booking
     * @param originalMileage the mileage at trip start
     * @param currentMileage the mileage at trip end
     */
    private void moveFromReservationToTripList(Booking originalBooking, int originalMileage, int currentMileage) {
        if (originalBooking == null) return;
        reservation.remove(originalBooking);

        Vehicle v = originalBooking.getVehicle();
        Campus pickup  = v.getCampus();
        Campus dropOff = originalBooking.getDropoff();
        boolean surcharge = (dropOff != null) && !dropOff.equals(pickup);
        Trip originalTrip = new Trip(originalBooking,originalMileage,currentMileage,surcharge);
        System.out.println("Trip completed: " + originalTrip.toString());

        v.setMileage(currentMileage);
        if (dropOff != null) {
            v.setCampus(dropOff);
        }
        Trip trip = new Trip(originalBooking, originalMileage, currentMileage, surcharge);
        tripList.add(trip);
    }

    /**
     * Finds the index (in reservation) of the booking for the same vehicle
     * whose end date is earliest.
     *
     * @param booking a reference booking used for matching
     * @return the index of the earliest ending booking for that vehicle;
     * -1 if none is found
     */
    private Date findEarliestEndDate(Booking booking){
        Date earliest = booking.getEnd();
        for(int i = 0; i < reservation.Size(); i++){
            if(reservation.toArray()[i].getEnd().compareTo(earliest) <= 0){
                earliest = reservation.toArray()[i].getEnd();
            }

        }
        return earliest;
    }

    /**
     * Method to easily create a vehicle based off which type of Vehicle it is.
     *
     * @param plate license plate
     * @param date obtained date
     * @param make vehicle make
     * @param odo mileage
     * @param campus campus
     * @return the correct vehicle object for its type; otherwise null vehicle object
     */
    private Vehicle createVehicle(String plate, Date date, Make make, Integer odo, Campus campus){
        switch(plate.charAt(plate.length() - 1)){
            case 'D':
                return odo != null ? new Utility(plate,date,make,odo,campus) : new Utility(plate);
            case 'X':
                return odo != null ? new Truck(plate,date,make,odo,campus) : new Truck(plate);
            case 'S':
                return odo != null ? new Sedan(plate,date,make,odo,campus) : new Sedan(plate);
            default:
                System.out.println("Unknown vehicle type: " + plate);
                return null;
        }
    }

    /**
     * Method to check if data tokens are correct for command.
     *
     * @param parts tokens from commandline
     * @param len correct length of the tokens
     * @param command command in token
     * @return true if parts length is equal to len; otherwise false
     */
    private boolean checkCommandParse(String[] parts, int len, String command){
        String commandString = "";
        if(command.equals("A")){
            commandString = "adding";
        }
        else if (command.equals("D")){
            commandString = "removing";
        }
        if(parts.length != len){
            System.out.println("Missing data tokens for " + commandString + " a vehicle.");
            return false;
        }
        return true;
    }

    /**
     * Method that loads the vehicles from vehicle.txt
     */
    private void loadVehicles(){
        try {
            Scanner scanner = new Scanner(new File("vehicles.txt"));
            int count = fleet.load(scanner);
            System.out.println(count + " vehicles loaded.");
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: vehicles.txt");
        }
    }

    /**
     * Checks for valid date for adding to fleet.
     *
     * @param parts commandline tokens
     * @param today today's date
     * @return true if all checks pass; otherwise false with an error message
     */
    private boolean addCheckDate(String[] parts, Date today){
        Date isValidDate = new Date(parts[2]);
        if(!isValidDate.isValid()){
            System.out.println(parts[2] + " - invalid calendar date.");
            return false;
        }
        else if(isValidDate.compareTo(today) >= 0){
            System.out.println(parts[2] + " - is today or a future date.");
            return false;
        }
        return true;
    }

    /**
     * Checks for valid make for adding to fleet.
     *
     * @param parts commandline tokens
     * @param make vehicle's make
     * @return true if all checks pass; otherwise false with an error message
     */
    private boolean addCheckMake(String[] parts, Make make){
        if(make == null){
            System.out.println(parts[3] + " - invalid make.");
            return false;
        }
        return true;
    }

    /**
     * Checks for valid mileage for adding to fleet.
     *
     * @param parts commandline tokens
     * @return true if all checks pass; otherwise false with an error message
     */
    private boolean addCheckMileage(String[] parts){
        int odo;
        try {
            odo = Integer.parseInt(parts[4]);
        } catch (NumberFormatException e) {
            System.out.println("For input string: \"" + parts[4] + "\" - not a valid mileage.");
            return false;
        }
        if (!isValidOdometer(odo)) {
            System.out.println(parts[4] + " - invalid mileage.");
            return false;
        }

        return true;

    }

    /**
     * Checks for valid tokens for adding to fleet.
     *
     * @param parts commandline tokens
     * @return true if all checks pass; otherwise false with an error message
     */
    private boolean addCheckTokens(String[] parts){
        if(parts[1].length() != 6){
            System.out.println(parts[1] + " - license plate number must be exactly 6 characters.");
            return false;
        }
        for (int i = 0; i < parts[1].length(); i++) {
            if (i < 5) {
                if (!Character.isDigit(parts[1].charAt(i))) {
                    System.out.println(parts[1] + " - first 5 characters must be numbers.");
                    return false;
                }
                continue;
            }
            char vehicleCheck = parts[1].charAt(5);
            if (!(vehicleCheck == 'D' || vehicleCheck == 'S' || vehicleCheck == 'X')) {
                System.out.println(parts[1] + " - last character is not a valid vehicle type.");
                return false;
            }
        }
        return true;
    }

    /**
     * Checks for valid campus for adding to fleet.
     *
     * @param parts commandline tokens
     * @param campus campus
     * @return true if all checks pass; otherwise false with an error message
     */
    private boolean addCheckCampus(String[] parts, Campus campus){
        if(campus == null){
            System.out.println(parts[5] + " - invalid location.");
            return false;
        }
        return true;
    }

    /**
     * Adds a vehicle to the fleet.
     *
     * @param parts tokenized input (must be length 5)
     */
    private void addToFleet(String[] parts){
        if(!checkCommandParse(parts, 6, "A")){
            return;
        }
        Date today = getToday();
        Make make = Make.parseMake(parts[3]);
        Campus campus = Campus.parseCampus(parts[5]);
        if(!addCheckDate(parts, today) || !addCheckMake(parts, make) || !addCheckMileage(parts)
                || !addCheckTokens(parts) || !addCheckCampus(parts, campus)){
            return;
        }
        Date isValidDate = new Date(parts[2]);
        int odo = Integer.parseInt(parts[4]);
        Vehicle vehicle = createVehicle(parts[1], isValidDate, make, odo, campus);
        fleet.add(vehicle);
        System.out.println(vehicle.toString() + " has been added to the fleet.");
    }

    /**
     * Checks for if vehicle is already in fleet and if reservation already has existing bookings.
     *
     * @param vehicle the vehicle
     * @param booking the booking
     * @return true if checks all pass; false otherwise
     */
    private boolean removeChecks(Vehicle vehicle, Booking booking){
        if(!fleet.contains(vehicle)){
            System.out.println(vehicle.getPlate() + " is not in the fleet.");
            return false;
        }
        if(reservation.findInstanceOfVehicleOnlyPlate(booking)){
            System.out.println(vehicle.getPlate() + " - has existing bookings; cannot be removed.");
            return false;
        }
        return true;
    }

    /**
     * Removes a vehicle from the fleet.
     *
     * @param parts tokenized input (must be length 2)
     */
    private void removeFromFleet(String[] parts){
        if(!checkCommandParse(parts,2,"D")){
            return;
        }
        Vehicle vehicle = createVehicle(parts[1], null, null, null, null);
        Booking booking = new Booking(vehicle);
        if(!removeChecks(vehicle, booking)){
            return;
        }
        Vehicle getOriginalVehicle = fleet.getVehicle(vehicle);
        System.out.println(getOriginalVehicle.getPlate() + " has been removed from the fleet.");
        fleet.remove(vehicle);
    }

    /**
     * Checks requested booking dates against rules:
     * Prints a specific error message and returns false on the first violation.
     *
     * @param parts tokenized input
     * @param startingDate requested start date
     * @param endingDate requested end date
     * @param today today's date
     * @param threeMonthsFromToday the date three months from today
     * @return true if all checks pass; false otherwise (and will print error message)
     */
    private boolean bookVehicleDateChecks(String[] parts, Date startingDate, Date endingDate, Date today,
                                          Date threeMonthsFromToday){
        if(!startingDate.isValid()){
            System.out.println(parts[1] + " - beginning date is not a valid calendar date.");
            return false;
        }
        if(startingDate.compareTo(today) < 0){
            System.out.println(parts[1] + " - beginning date is not today or a future date.");
            return false;
        }
        if(startingDate.compareTo(threeMonthsFromToday) > 0){
            System.out.println(startingDate + " - beginning date beyond 3 months.");
            return false;
        }
        if(!endingDate.isValid()){
            System.out.println(parts[2] + " - ending date is not a valid calendar date.");
            return false;
        }
        if(endingDate.compareTo(startingDate) < 0){
            System.out.println(parts[2] + " - ending date must be equal or after the beginning date " + startingDate);
            return false;
        }
        if(daysBetween(startingDate, endingDate) >= 7){
            System.out.println(parts[1] + " ~ " + parts[2] + " - duration more than a week.");
            return false;
        }
        return true;
    }

    /**
     * Checks booking date against a 2nd set of rules
     *
     * @param parts tokenized input
     * @param vehicle vehicle
     * @param startingDate the starting date
     * @param endingDate the ending date
     * @param today today
     * @param threeMonthsFromToday day three months from today
     * @param employee employee
     * @param campus campus
     * @return true if checks pass; false otherwise
     */
    private boolean bookingChecks(String[] parts, Vehicle vehicle, Date startingDate, Date endingDate,
                                  Date today,  Date threeMonthsFromToday, Employee employee, Campus campus){
        if (!bookVehicleDateChecks(parts, startingDate, endingDate, today, threeMonthsFromToday)){
            return false;
        }
        if(!fleet.contains(vehicle)){
            System.out.println(vehicle.getPlate() + " is not in the fleet.");
            return false;
        }
        if (!isVehicleAvailable(vehicle, startingDate, endingDate)) {
            System.out.println(vehicle.getPlate() + " - booking with " + parts[1] + " ~ " + parts[2] + " not available.");
            return false;
        }
        if(employee == null){
            System.out.println(parts[4] + " - not an eligible employee to book.");
            return false;
        }
        if (!isVehicleAvailableSelf(startingDate, endingDate, parts[4])) {
            System.out.println(parts[4].toUpperCase() + " - has an existing booking conflicting with booking date "
                    + parts[1] + " ~ " + parts[2]);
            return false;
        }
        if(campus == null){
            System.out.println(parts[5] + " - invalid location.");
            return false;
        }
        return true;
    }

    /**
     * Creates a reservation.
     *
     * @param parts tokenized input (must be length 5)
     */
    private void bookVehicle(String[] parts){
        if(!checkCommandParse(parts, 6,"B")){
            return;
        }
        Date startingDate = new Date(parts[1]);
        Date endingDate = new Date(parts[2]);
        Date today = getToday();
        Date threeMonthsFromToday = getThreeMonthsFromToday();
        Employee employee = Employee.parseEmployee(parts[4]);
        Campus campus = Campus.parseCampus(parts[5]);
        Vehicle vehicle = createVehicle(parts[3], null, null, null, null);
        if(!bookingChecks(parts, vehicle, startingDate, endingDate, today,
                        threeMonthsFromToday, employee, campus)){
            return;
        }
        Booking booking = new Booking(startingDate, endingDate, employee, fleet.getVehicle(vehicle), campus);
        System.out.println(booking.toString() + " booked.");
        reservation.add(booking);
    }

    /**
     * Cancels an existing reservation.
     *
     * @param parts tokenized input (must be length 4)
     */
    private void cancelBooking(String[] parts){
        if(!checkCommandParse(parts, 4,"C")){
            return;
        }
        Booking booking = new Booking(new Date(parts[1]),new Date(parts[2]),createVehicle(parts[3], null,
                null,null,null));
        if(!reservation.contains(booking)){
            System.out.println(parts[3] + ":" + parts[1] + " ~ " + parts[2] + " - cannot find the booking.");
            return;
        }
        Booking getOriginalBooking = reservation.bookingLookup(booking);
        System.out.println(getOriginalBooking.getVehicle().getPlate() + ":" + getOriginalBooking.getBegin() + " ~ "
                + getOriginalBooking.getEnd() + " has been canceled.");
        reservation.remove(booking);
    }

    /**
     * Finds the original booking in the reservation.
     *
     * @param findBooking booking to find
     * @return the Booking if found; null otherwise
     */
    private Booking findBooking(Booking findBooking){
        for(int i = 0; i < reservation.Size(); i++){
            if(reservation.toArray()[i].equalsNoBeginDate(findBooking)){
                return reservation.toArray()[i];
            }

        }
        return null;
    }

    /**
     * Returns a vehicle and archives the trip.
     *
     * @param parts tokenized input (must be length 4)
     */
    private void returnVehicle(String[] parts){
        if(!checkCommandParse(parts, 4,"R")){
            return;
        }
        Booking booking = new Booking(new Date(parts[1]), createVehicle(parts[2], null,null,null,null));
        if(!reservation.findInstanceOfVehicle(booking)){
            System.out.println(parts[2] + " booked with ending date " + parts[1] + " - cannot find the booking.");
            return;
        }
        Date earliest = findEarliestEndDate(booking); //check to see
        if(earliest.compareTo(booking.getEnd()) != 0){
            System.out.println(parts[2] + " booked with end date " + parts[1] + " - returning not in order of ending date.");
            return;
        }
        Date currEnd = booking.getEnd();
        if(currEnd.compareTo(earliest) > 0){
            System.out.println(parts[2] + " booked with ending date " + parts[1] + " - returning not in order of ending date.");
            return;
        }
        int newMileage = Integer.parseInt(parts[3]);
        if (!isValidOdometer(newMileage)) {
            System.out.println(parts[3] + " - invalid mileage.");
            return;
        }
        Booking originalBooking = findBooking(booking);
        int originalMileage = originalBooking.getVehicle().getMileage();
        if(newMileage <= originalMileage){
            System.out.println("Invalid mileage - current mileage: " + originalMileage + " entered mileage: " + parts[3]);
            return;
        }
        moveFromReservationToTripList(originalBooking, originalMileage, newMileage);
    }

    /**
     * Prints all vehicles in the fleet ordered by location, make, then obtained date.
     */
    private void displayVehicles(){
        if(fleet.Size() == 0){
            System.out.println("There is no vehicle in the fleet.");
            return;
        }
        System.out.println("*List of vehicles in the fleet, ordered by location/make/date obtained.");
        fleet.printByMake();
        System.out.println("*end of list.");
    }

    /**
     * Prints reservations ordered by location, license plate, then beginning date.
     */
    private void displayBookings1(){
        if(reservation.Size() == 0){
            System.out.println("There is no booking record.");
            return;
        }
        System.out.println("*List of reservations ordered by location/license plate/beginning date.");
        reservation.printByVehicle();
        System.out.println("*end of list.");
        System.out.println();
    }

    /**
     * Prints reservations ordered by department and employee.
     */
    private void displayBookings2(){
        if(reservation.Size() == 0){
            System.out.println("There is no booking record.");
            return;
        }
        System.out.println("*List of reservations ordered by department and employee.");
        reservation.printByDept();
        System.out.println("*end of list.");
    }

    /**
     * Prints archived trips ordered by license plate and ending date.
     */
    private void displayCompletedTrips(){
        if(tripList.getLast() == null){
            System.out.println("There is no completed trips.");
            return;
        }
        System.out.println("*List of completed trips ordered by license plate and ending date.");
        tripList.print();
        System.out.println("*end of list.");
        System.out.println();
    }

    /**
     * Prints the cost report out for the TripList
     */
    private void displayCostReport(){
        if(tripList.getLast() == null){
            System.out.println("There is no archived trips for the cost report.");
            return;
        }
        tripList.printCharges();
        System.out.println();
    }

    /**
     * Runs the command loop for the Vehicle Management System.
     */
    public void run() {
        System.out.println("Vehicle Management System is running.");
        Scanner sc = new Scanner(System.in);
        while (running) {
            if (!sc.hasNextLine()) break;
            String line = sc.nextLine().trim();
            if (line.equals("Q")) {
                terminate();
                return;
            } else if (!line.isEmpty()) {
                String[] parts = line.split("\\s+");
                String command = parts[0];
                processCommand(command, parts);
            }
        }
        sc.close();
    }
}
