/** This code can be used as a basis for storing and manipulating data */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

public class employeesFun {

    // global variables
    public static int lines = 0; // initialize variable for line counting
    public static String filePath; // initialize variable for storing file path
    public static ArrayList<String> names = new ArrayList<>(); // array for names
    public static ArrayList<String> dates = new ArrayList<>(); // array for dates
    public static ArrayList<String> salaries = new ArrayList<>(); // array for salaries
    public static double avgSalary; // for storing the average salary
    public static int countAboveAvg; // for storing the number above the average
    public static long avgAge; // for storing the average age


    /**
     * This is the main function that calls the other functions.
     * @param args args
     */
    public static void main(String args[]) {

        // define the scanner
        Scanner scan = new Scanner(System.in); // used for defining file path

        // do the stuff
        readFile(scan);
        lineCount();
        parseFile();
        findMax(salaries);
        theMax();
        findAverage();
        aboveAverage();
        writeFile();

    }

    /**
     * This function asks the user to specify a file path and checks for the existence of the file.
     */
    public static void readFile(Scanner scan) {

        try {

            System.out.print("Enter the file path and name with extension: "); // asks the user for input
            filePath = scan.nextLine(); // stores the file path
            scan.close(); // close the scanner

        }

        catch (Exception ex) {

            printError("file not found");
            ex.printStackTrace(); // error catching

        }

    }

    /**
     * This function reads the number of lines in the file, adding 1 to int lines for each line that exists in the file.
     */
    public static void lineCount() {

        try {

            System.out.println("Working with file: " + filePath); // print the file path we are working with

            BufferedReader reader = new BufferedReader(new FileReader(filePath)); // initialize the reader
            while (reader.readLine() != null) lines++; // while there's a line, add one to the line count
            reader.close(); // close the reader

            System.out.println("There are " + lines + " employees."); // print the line count

        }

        catch (Exception ex) {

            printError("file not found");
            ex.printStackTrace(); // error catching

        }

    }

    /**
     * This function parses the csv file into separate ArrayLists for each variable.
     */
    public static void parseFile() {

        try {

            BufferedReader reader = new BufferedReader(new FileReader(filePath)); // initialize the reader

            for (String line = reader.readLine(); line != null; line = reader.readLine()) { // for all the remaining rows

                String[] parts = line.split(","); // use a comma as the delimiter
                String name = parts[0]; // name is 1st entry
                String date = parts[1]; // date is 2nd entry
                String salary = parts[2]; // salary is 3rd entry

                names.add(name); // add parsed names to names array
                dates.add(date); // add parsed dates to dates array
                salaries.add(salary); // add parsed salaries to salaries array

            }

            //System.out.println(names);
            //System.out.println(dates);
            //System.out.println(salaries);

        }

        catch (Exception ex) {

            printError("file not found");
            ex.printStackTrace(); // error catching

        }

    }

    /**
     * This function finds the maximum of an array list.
     */
    public static Integer findMax(ArrayList<String> array) {

        Integer i=0, maxIndex=-1, max=null;

        // for every string entry in the array
        for (String x : array) {

            // if the current entry is larger than the to-date max
            if ((x!=null) && ((max==null) || (Integer.parseInt(x)>max))) {

                // set that integer as the max
                max = Integer.parseInt(x);
                // store the position of the max entry
                maxIndex = i;

            }

            i++;

        }

        // return the position of the max entry
        return maxIndex;

    }

    /**
     * This function calls the findMax function and prints the maximum salary, and the individual.
     */
    public static void theMax() {

        // call the maximum salary
        Integer maxIndex = findMax(salaries);
        // get the name associated with that max salary
        String maxSalaryEmployeeName = names.get(maxIndex);
        // print the result
        System.out.println(maxSalaryEmployeeName + " has the highest salary and earns " + salaries.get(maxIndex) + ".");

    }

    /**
     * This function finds the average salary and age (separately).
     */
    public static void findAverage() {

        // average salary
        double sumAmount = 0;

        // for every entry in the array
        for (String amount : salaries) {

            try {

                // add that amount to a collective total
                sumAmount += Double.parseDouble(amount);

            }

            catch (Exception ex) {

                printError("cannot find the average salary");
                ex.printStackTrace();

            }

        }

        // divide the total sum by the number of entries
        avgSalary = sumAmount / lines;
        // format for easy readability
        DecimalFormat df = new DecimalFormat("#.00");
        // print the result
        System.out.println("The average salary is $" + df.format(avgSalary) + ".");

        // average age
        String dateNow = "2/1/2016"; // the current date
        long sumDifferences = 0;
        double normalizer;
        double normal;

        // get the current date
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

        // for every entry in the array
        for (String date : dates) {

            try {

                Date dateToSubtract = sdf.parse(date); // parse the entry
                Date dateNowToSubtract = sdf.parse(dateNow); // parse the current date

                long differenceInMillis = dateNowToSubtract.getTime() - dateToSubtract.getTime(); // subtract the differences
                sumDifferences += differenceInMillis; // add those differences to a collective sum

            }

            catch (java.text.ParseException e) {

                printError("cannot find the average age");
                e.printStackTrace();

            }

        }

        avgAge = (sumDifferences / lines); // divide the sum by the number of entries
        normalizer = 31556952000.0; // converts milliseconds to years
        normal = avgAge / normalizer; // convert to years
        System.out.println("The average age is " + df.format(normal) + " years."); // print the result

    }

    public static void aboveAverage() {

        // for every entry in the array
        for (String amount : salaries) {

            // if the salary is above the average
            if (Double.parseDouble(amount) > avgSalary) {

                // add one to the total count
                countAboveAvg += 1;

            }

        }

        // print the result
        System.out.println(countAboveAvg + " employees have salaries above the average."); // prints amount that are above the average

    }

    /**
     * This function writes the working file to the output directory.
     */
    public static void writeFile() {

        try { // a big try block, could be smaller

            // create a new array for the salaries
            int[] copySalary = new int[salaries.size()];

            // create a new array for the names
            String[] sortedNameArray = new String[salaries.size()];

            // copy the content into the new array
            for (int i = 0; i < salaries.size(); i++) {

                copySalary[i] = Integer.parseInt(salaries.get(i)) ;

            }

            // copy the content into the new array
            for (int i = 0; i < names.size(); i++ ) {

                sortedNameArray[i] = names.get(i);

            }

            // for every element in the new array
            for (int i = 0; i < copySalary.length; i++) {

                // if the value is larger in the array, then swap positions
                for (int j = 0; j < copySalary.length-1; j++) {

                    if ( copySalary[j+1] < copySalary[j]) {

                        int tempValue = copySalary[j+1];
                        copySalary[j+1] = copySalary[j];
                        copySalary[j] = tempValue;

                        String tempString = sortedNameArray[j+1];
                        sortedNameArray[j+1] = sortedNameArray[j];
                        sortedNameArray[j] = tempString;

                    }

                }

            }

            // create a writer for writing the csv file
            BufferedWriter br = new BufferedWriter(new FileWriter("assets/output.csv"));
            StringBuilder sb = new StringBuilder();

            // for i in the number of entries
            for (int i = 0; i < lines; i++) {

                sb.append(sortedNameArray[i]); // append the name
                sb.append(","); // append a comma
                sb.append(copySalary[i]); // append the salary
                sb.append("\n"); // start a new line

            }

            br.write(sb.toString()); // write the contents of the loop to csv
            br.close(); // close the writer

            // print message
            System.out.println("A sorted file has been written to the assets folder.");

        }

        catch (Exception ex) {

            printError("cannot export file");
            ex.printStackTrace();

        }


    }

    /**
     * This function is used for printing errors.
     */
    public static void printError(String s) {

        System.out.println("\nError: " + s);

    } // end function

}