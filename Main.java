/*
    Kiosk for the Redbox Inventory System
    Reads two files builds the tree and processes the orders


    NETID: EXR180014
    Author: Erik Rodriguez
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        Scanner entrada = new Scanner(System.in);

        String filename;
        File invFile;
        File logFile;

        do {
            System.out.print("Enter the inventory file: "); // Get Inventory file
            filename = entrada.nextLine();
            invFile = ValidateFile(filename);
        } while (invFile == null);

        do {
            System.out.print("Enter transaction log file: "); // Get Transaction log file
            filename = entrada.nextLine();
            logFile = ValidateFile(filename);
        } while (logFile == null);

        entrada.close();  // close user input read

        Scanner scanfile1 = new Scanner(invFile);
        BSTree<DVD> BST = new BSTree<>(); // this is where we will take the contents of to build our tree

        while(scanfile1.hasNext()){ // extracting data from the inventory file and builds BST
            String info =
                    scanfile1.nextLine();
            String[] details = info.split(","); // this splits the lines in the files
            String title = details[0]; // takes the title
            int available = Integer.parseInt(details[1]);
            int rented = Integer.parseInt(details[2]);
            BST.Insert(new Node<>(new DVD(title, available, rented)));
        }
        scanfile1.close(); // close the inv reading scanner


        Scanner scanfile2 = new Scanner(logFile);
        String errorlog = "error.log";
        PrintWriter write = new PrintWriter(errorlog);

        while(scanfile2.hasNext()){
            String command = scanfile2.next(); // return, add, rent
            String content = scanfile2.nextLine().trim(); // title and maybe the quantity
            String wholeline = command+" "+content;

            boolean isValid = true; // we will use that to determine if a line's data is good

            if (content.contains(",") && (content.split(",")).length == 2)  // if it has the quantity then split it
            {
                String[] details = content.split(",");
                String movieTitle = details[0];

                int amount = -1; // flag

                if (!movieTitle.startsWith("\"") && !movieTitle.endsWith("\"")){
                    isValid = false;
                }
                else {
                    try{
                        amount = Integer.parseInt(details[1]);
                    } catch (Exception e){
                        amount = -1;
                        isValid = false;
                    }
                }

                if (amount != -1 && isValid){

                    switch (command) {
                        case "add":
                            AddMovie(BST,movieTitle, amount); // add a copy of a movie
                            break;
                        //case "rent":
                        //    isValid = RentMovie(BST,movieTitle,amount); // rent a movie if possible
                        //    break;
                        case "remove":
                            isValid = RemoveMovie(BST, movieTitle, amount); // remove a copy if possible
                            break;
                        //case "return":
                        //    isValid = ReturnMovie(BST, movieTitle, amount); // return a movie if possible
                        //    break;
                        default:
                            isValid = false;
                    }
                }

            }
            else // this means the line inside the transaction log didn't include a quantity of copies
            {
                switch (command) {
                    //case "add":
                    //     AddMovie(BST,content, 1); // add a copy of a movie
                    //     break;
                    case "rent":
                        isValid = RentMovie(BST,content,1); // rent a movie if possible
                        break;
                    // case "remove":
                    //     isValid = RemoveMovie(BST, content, 1); // remove a copy if possible
                    //     break;
                    case "return":
                        isValid = ReturnMovie(BST, content, 1); // return a movie if possible
                        break;
                    default:
                        isValid = false;
                }
            }

            if (!isValid){ // write the error file if we couldn't do nothing with a line
                write.println(wholeline); // write it to the file
            }

        }
        write.close();
        scanfile2.close();
        System.out.println();
        BST.inOrderPrint(BST.getRoot());


    }

    /**
     * Takes a file reads it and returns a file
     * @param filename name of the file to be read
     * @return returns the file or null
     */
    public static File ValidateFile (String filename) {

        File file;

        try {
            file = new File(filename);
            if (!file.exists() && !file.canRead()) {
                throw new Exception();
            }

        } catch (Exception e) {
            System.out.println("There is a problem with the file, try again");
            file = null;
        }

        return file;
    }


    /**
     * // Inserts DVD into a tree or edits the value of current node
     * @param movieTitle a DVD node
     * @param quantity number of copies
     */

    public static void AddMovie(BSTree<DVD> BST,String movieTitle, int quantity){
        Node<DVD> entry = new Node<>(new DVD (movieTitle, quantity, 0));
        Node<DVD> searchEntry = BST.Search(entry);

        if (searchEntry != null) { // if the movie exists in our tree
            searchEntry.getPayload().setAvailable(quantity + searchEntry.getPayload().getAvailable());
        } else { // if the movie doesn't exist insert the new node
            BST.Insert(entry);
        }


    }

    /**
     * Takes in a DVD node and then rents the movie if possible
     * @param BST the binary tree
     * @param movieTitle the title of the entry
     * @param rentQuantity copies of copies to reserve
     * @return returns true if it was able to be rented
     */
    public static boolean RentMovie(BSTree<DVD> BST,String movieTitle, int rentQuantity)
    {
        Node<DVD> searchEntry;
        searchEntry = BST.Search(new Node<>(new DVD(movieTitle,0,0)));

        /*
            it will return false if the dvd isn't in our tree, they try to rent more movies than available
            or they try to rent a negative number of movies
         */
        if (searchEntry == null || searchEntry.getPayload().getAvailable() - rentQuantity < 0 || rentQuantity < 0 ){
            return false;
        }
        else {
            searchEntry.getPayload().setAvailable(searchEntry.getPayload().getAvailable() - rentQuantity);
            searchEntry.getPayload().setRented(searchEntry.getPayload().getRented() + rentQuantity);
            return true;
        }
    }


    /**
     * Return a movie from our selection
     * @param BST binary search tree
     * @param movieTitle number of titles to delete
     * @param returnQuantity number of copies to return
     * @return true if its inside the tree, false if title isn't there or invalid data
     */
    public static boolean ReturnMovie(BSTree<DVD> BST,String movieTitle, int returnQuantity){
        Node<DVD> searchEntry;
        searchEntry = BST.Search(new Node<>(new DVD(movieTitle,0,0)));

        // if the movie isn't in our tree, we try to return more movies than rented, or the rented quantity is negative
        if (searchEntry == null || searchEntry.getPayload().getRented() - returnQuantity < 0 || returnQuantity < 0 ){
            return false;
        }
        else { // increases the available quantity and subtracts from the total quantity
            searchEntry.getPayload().setAvailable(searchEntry.getPayload().getAvailable() + returnQuantity);
            searchEntry.getPayload().setRented(searchEntry.getPayload().getRented() - returnQuantity);
            return true;
        }
    }

    /**
     * Removes a copy of the title from the kiosk, if there is no copies left the entry gets deleted from our tree
     * @param BST binary search tree
     * @param movieTitle movie title
     * @param deleteQuantity number of copies to delete
     * @return true if we remove the copies, false if the title its not there or invalid data
     */
    public static boolean RemoveMovie(BSTree<DVD> BST,String movieTitle, int deleteQuantity){
        Node<DVD> searchEntry;
        searchEntry = BST.Search(new Node<>(new DVD(movieTitle,0,0)));

        // if the DVD isn't in our tree OR deletion is more than available quantity OR deleteQuantity is negative
        if (searchEntry == null || searchEntry.getPayload().getAvailable() - deleteQuantity < 0 || deleteQuantity < 0){
            return false;
        }
        else {
            // if there was to be zero available rented and available delete than node
            if (searchEntry.getPayload().getAvailable() - deleteQuantity == 0 && searchEntry.getPayload().getRented() == 0){
                // delete the movie from our tree
                BST.Remove(searchEntry); // delete the title
                return true;
            }
            // subtract movie from the available quantity
            searchEntry.getPayload().setAvailable(searchEntry.getPayload().getAvailable() - deleteQuantity);
            return true;
        }
    }

}
