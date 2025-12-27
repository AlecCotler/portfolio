package util;
import java.util.Iterator;
/**
 * This class creates a circularly linked list of trips.
 * Additionally, it inserts the trips in order of earliest end date, and can print the list/find the number of trips in the list.
 *
 * @author Alec Cotler
 */
public class List<E> implements Iterable<E> {
    public static final int CAPACITY = 4;
    public static final int NOT_FOUND = -1;

    private E[] objects; //E is the name for the generic type
    private int size;

    /**
     * List constructor.
     */
    public List() {
        objects = (E[]) new Object[CAPACITY];
        size = 0;
    } //new an array type-casted to E with a capacity of 4.

    /**
     * Find the index of an object in the list.
     *
     * @param e The object to be found in the list.
     * @return the index of the object you are trying to find, or -1 if it is not present.
     */
    private int find(E e) {
        if (e == null){
            return NOT_FOUND;
        }
        for (int i = 0; i < size; i++){
            if (e.equals(objects[i])){
                return i;
            }
        }
        return NOT_FOUND;
    } //return -1 if not found

    /**
     * create a new list with the size of the current list+capacity, and copy all elements of the current list into the new one
     */
    private void grow() {
        E[] newObjects = (E[]) new Object[this.objects.length + CAPACITY];
        for(int i = 0; i < size; i++) {
            newObjects[i] = this.objects[i]; // copy over each element
        }
        this.objects = newObjects; // replace old array
    }

    /**
     * Find whether an object is in the list.
     *
     * @param e The object to be found in the list.
     * @return true if the object is in the list, or false if it is not
     */
    public boolean contains(E e) {
        if (e == null){
            return false;
        }
        for (int i=0; i<size; i++){
            if (objects[i].equals(e)){
                return true;
            }
        }
        return false;
    }

    /**
     * Insert an object into the list if it is not yet inside the list.
     *
     * @param e the object to be added
     */
    public void add(E e) {
        if (e == null) {
            return;
        }
        if (contains(e)) {
            return;
        }
        if (this.size == this.objects.length) {
            this.grow();
        }
        this.objects[size++] = e;
    }

    /**
     * Remove an object from the list if it exists inside the list.
     *
     * @param e The object to be removed.
     */
    public void remove(E e) {
        int index = NOT_FOUND;
        if (contains(e)) {
            index = find(e);
        } else return;

        this.objects[index] = objects[size - 1];
        this.objects[size - 1] = null;
        size--;
    }

    /**
     * See if the list is empty.
     *
     * @return true if the list is empty, or false if it isn't.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Check how many objects are in the list.
     *
     * @return the size of the list.
     */
    public int Size() {
        return size;
    }

    /**
     * Create a new list iterator
     *
     * @return an iterator.
     */
    @Override
    public Iterator<E> iterator() {
        return new ListIterator();
    }

    /**
     * Find the object at an index of the list.
     *
     * @param index , the index of the list to retrieve the object.
     * @return the object at index.
     */
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new RuntimeException("Index out of bounds");
        }
        return (E) objects[index];
    }

    /**
     * Override an object at a specific index with a new object.
     *
     * @param index to override the object at.
     * @param e the object to override.
     */
    public void set(int index, E e) {
        if (index < 0 || index >= size) {
            throw new RuntimeException("Index out of bounds");
        }
        objects[index] = e;
    }

    /**
     * Find the index of an object.
     *
     * @param e the object to be found
     * @return the index of e
     */
    public int indexOf(E e) {
        if (e == null){
            return NOT_FOUND;
        }
        for (int i = 0; i < size; i++) {
            if (objects[i].equals(e)) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * creates an object to iterate through the list
     * @param <E>, an iterator over any type of object.
     */
    private class ListIterator<E> implements Iterator<E> {

        int current = 0; //current index when traversing the list (array)

        /**
         * Check if there are more objects in the array past the iterator.
         *
         * @return true if there are more objects in the array past the iterator, false otherwise.
         */
        @Override
        public boolean hasNext(){
            return current <size;
        }

        /**
         * Return the next object in the array.
         *
         * @return the next object in the array.
         */
        @Override
        public E next(){
            if (!hasNext()) {
                throw new RuntimeException("No more elements");
            }
            return (E) objects[current++];
        } //return the next object in the list
    }

}