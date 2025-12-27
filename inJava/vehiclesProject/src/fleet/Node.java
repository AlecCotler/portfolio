package fleet;
/**
 * Implements the nodes of a linked list.
 *
 * @author Alec Cotler
 */
public class Node {

    public Trip trip;
    public Node next;

    /**
     * Creates a Node for a Trip.
     *
     * @param trip the trip to be added to the linked list
     */
    public Node(Trip trip) {
        this.trip = trip;
        this.next = null;
    }
}

