package fleet;

import util.Date;

/**
 * This class implements bookings for the fleet including an equals and toString method.
 *
 * @author Alec Cotler
 */
public class Booking {
    private Date begin;
    private Date end;
    private Employee employee;
    private Vehicle vehicle;
    private Campus dropoff;

    /**
     * Constructor of a booking with just a vehicle.
     *
     * @param vehicle the vehicle being booked
     */
    public Booking(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * Constructor of a booking with the same beginning and ending date, and a vehicle.
     *
     * @param end the ending date of the booking
     * @param vehicle the vehicle being booked
     */
    public Booking(Date end, Vehicle vehicle){
        this.begin = end;
        this.end = end;
        this.vehicle = vehicle;
    }

    /**
     * Constructor of a booking with a beginning date, end date, and vehicle.
     *
     * @param begin the beginning date of the booking
     * @param end the ending date of the booking
     * @param vehicle the vehicle being booked
     */
    public Booking(Date begin, Date end, Vehicle vehicle){
        this.begin = begin;
        this.end = end;
        this.vehicle = vehicle;
    }

    /**
     * Constructor of a booking with a beginning date, end date, vehicle, and employee.
     *
     * @param begin the beginning date of the booking
     * @param end the ending date of the booking
     * @param employee the employee booking the vehicle
     * @param vehicle the vehicle being booked
     */
    public Booking(Date begin, Date end, Employee employee, Vehicle vehicle, Campus dropoff) {
        this.begin = begin;
        this.end = end;
        this.employee = employee;
        this.vehicle = vehicle;
        this.dropoff = dropoff;
    }

    /**
     * Returns the beginning date of the booking.
     *
     * @return begin the beginning date of the booking
     */
    public Date getBegin() {
        return begin;
    }

    /**
     * Returns the ending date of the booking.
     *
     * @return end the ending date of the booking
     */
    public Date getEnd() {
        return end;
    }

    /**
     * Returns the employee associated with the booking.
     *
     * @return employee the employee in the booking
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Returns the vehicle associated with the booking.
     *
     * @return vehicle the vehicle in the booking
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * Returns the drop-off campus location
     * .
     * @return the campus
     */
    public Campus getDropoff() {
        return dropoff;
    }

    /**
     * Tests if two bookings with beginning dates, ending dates, and a vehicle are equal to each other.
     *
     * @param obj   the reference object with which to compare.
     * @return true if the bookings are the same as each other, false if not
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Booking other = (Booking) obj;
        return begin.equals(other.begin) && end.equals(other.end) && vehicle.equals(other.vehicle);
    }

    /**
     * The string representation of a booking.
     *
     * @return the string representation of a booking
     */
    @Override
    public String toString() {
        return this.vehicle.getPlate() + ":" + this.vehicle.getMake() + " [" + this.vehicle.getCampus() + ":" +
                this.vehicle.getCampus().getCity() + "] " + this.begin.toString() + " ~ "
                + this.end.toString() + " [drop off:" + this.dropoff + "] [" + this.employee + "]";
    }

    /**
     * Tests if two bookings with no beginning dates are equal.
     *
     * @param booking the booking being compared to
     * @return true if the bookings are equal, false if they are not
     */
    public boolean equalsNoBeginDate(Booking booking){
        return this.end.equals(booking.end) && this.vehicle.equals(booking.vehicle);
    }

    /**
     * Tests if two bookings with no beginning or end dates are equal.
     *
     * @param booking the booking being compared to
     * @return true if the bookings are equal, false if not
     */
    public boolean equalsNoDate(Booking booking){
        return this.vehicle.equals(booking.vehicle);
    }

}
