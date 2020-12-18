/*
    Defines the attributes of a DVD used for the Redbox kiosk

    NetID: EXR180014
    Author: Erik Rodriguez
 */
public class DVD implements Comparable<DVD>{
    private String title;
    private int available;
    private int rented;

    DVD(){} // default constructor

    public DVD(String title, int available, int rented) { // overloaded constructor
        this.title = title;
        this.available = available;
        this.rented = rented;
    }

    public int getAvailable() { return available; } // getters
    public int getRented() { return rented; }
    public String getTitle() { return title; }

    public void setAvailable(int available) { this.available = available; } // setters
    public void setRented(int rented) { this.rented = rented; }
    public void setTitle(String title) { this.title = title; }

    public void rentMovies(int rentAmount){ this.rented += rentAmount; } // actions
    public void addAvailable(int available){this.available += available; }

    @Override
    public int compareTo(DVD o) { return this.getTitle().compareTo(o.getTitle()); } // make DVD comparable

    @Override
    public String toString() {
        title = title.replace("\"","");
        return  String.format("%-30s%-10s%10d",title,available,rented);
    }
}
