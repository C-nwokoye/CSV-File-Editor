/*
Name: UJ Nwokoye
Date: 10/16/2022
Purpose: OOP Database Project; allows you to read in, make edits to, and read out a csv file
 */
import java.io.*;
import java.util.ArrayList;
import java.io.FileWriter;
import java.util.HashSet;
import java.io.IOException;
import java.util.Scanner;

public class Database {

    static ArrayList<Integer> allIds = new ArrayList<Integer>();
    static ArrayList<Record> allRecords = new ArrayList<Record>();
    static int numOfFields;
    static int numOfRecords;
    static String exportedFilePath;
    static String importedFilePath = "/Users/ujnwokoye/Downloads/sample_csv_file.csv";

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        setDatabase();
        System.out.println("Would you like to print the current database?\n1) Yes\n2) No");
        int toPrint = in.nextInt();
        in.nextLine();
        if (toPrint == 1) {
            System.out.println("Would you like to either print the entire database or a single record?");
            System.out.println("1) Entire Database\n2) Single Record");
            print(in.nextInt());
            in.nextLine();
        }
        boolean changeBeingMade = false;
        System.out.println("Would you like to make an edit to this database?\n1) Yes\n2) No");
        do {
            int makeChange = in.nextInt();
            in.nextLine();
            if (makeChange == 1) {
                changeBeingMade = true;
                System.out.println("Which change do you wish to make?\n" +
                        "1) Add record\n2) Delete Record\n3) Edit Record");
                int nextAction = in.nextInt();
                in.nextLine();
                if (nextAction == 1) {
                    addRecord();
                } else if (nextAction == 2) {
                    deleteRecord();
                } else if (nextAction == 3) {
                    editRecord();
                } else {
                    // do nothing
                }
                System.out.println("Do you wish to make another change?\n1) Yes\n2) No");
                int makeAnotherChange = in.nextInt();
                if (makeAnotherChange != 1) {
                    changeBeingMade = false;
                }
            }
        } while (changeBeingMade); // Main menu to make changes to database
        writeFile();
    }

    public static void addRecord() {
        Scanner in = new Scanner(System.in);
        System.out.println("What is the ID of the record that you wish to add?");
        int recordToAdd = in.nextInt();
        in.nextLine();
        boolean uniqueId = true;
        for (int i = 0; i < allRecords.size(); i++) {
            if (recordToAdd == allRecords.get(i).getID()) {
                uniqueId = false;
                break;
            }
        }
        if (uniqueId) {
            ArrayList<String> newRecord = new ArrayList<String>();
            System.out.println("Please give a new value for each field.");
            System.out.println("The fields are as follows: " + Record.fieldNames);
            boolean isRequiredField = false;
            for (int i = 0; i < numOfFields; i++) {
                if (i == 0) {
                    newRecord.add(String.valueOf(recordToAdd));
                } else {
                    System.out.printf("Please type a value for %s: \n", Record.fieldNames.get(i));
                    if (Record.requiredFields.size() > 0) {
                        for (int j = 0; j < Record.requiredFields.size(); j++) {
                            if (Record.fieldNames.get(i).equals(Record.requiredFields.get(j))) {
                                isRequiredField = true;
                            }
                        }
                    }
                    String newValue;
                    if (isRequiredField) {
                        boolean validValue;
                        do {
                            newValue = in.nextLine();
                            String upperNewValue = newValue.toUpperCase();
                            if (upperNewValue.equals("N/A")) {
                                System.out.printf("\"%s\" is not a valid value. \"%s\" is a required field.",
                                        newValue, Record.fieldNames.get(i));
                                System.out.println("Please give another value for the required field.");
                                validValue = false;
                            } else {
                                validValue = true;
                            }
                        } while (!validValue);
                    } else {
                        newValue = in.nextLine();
                    }
                    newRecord.add(newValue);
                }
            }
            allRecords.add(new Record(newRecord));
            allIds.add(recordToAdd);
            numOfRecords++;
        } else {
            System.out.println("The ID of each record must be unique.");
            System.out.println("If you no longer wish to add a record, type \"1\", if you still wish " +
                    "to add a record, type \"2\".");
            int scratchInput = in.nextInt();
            in.nextLine();
            if (scratchInput == 2) {
                addRecord();
            }
        }
        for (int i = 0; i < numOfRecords; i++) {
            if (recordToAdd == allRecords.get(i).getID()) {
                System.out.println("Your new record is: " + allRecords.get(i).getRecord());
                break;
            }
        }
    }

    public static void deleteRecord() {
        boolean recordExists = false;
        Scanner in = new Scanner(System.in);
        System.out.println("What is the ID of the record that you wish to delete?");
        int recordToDelete = in.nextInt();
        in.nextLine();
        int x = 0;
        for (int i = 0; i < allIds.size(); i++) {
            if (allIds.get(i) == recordToDelete) {
                recordExists = true;
                x = i;
                break;
            }
        }
        if (!recordExists) {
            System.out.println("That record does not exist.");
        }
        if (recordExists) {
            for (int i = 0; i < numOfRecords; i++) {
                if (recordToDelete == allRecords.get(i).getID()) {
                    System.out.println(allRecords.get(i).getRecord());
                    System.out.println("Are you sure that this is the record that you wish to delete?");
                    System.out.println("\n1) Yes\n2) No");
                    int nextAction = in.nextInt();
                    in.nextLine();
                    if (nextAction == 1) {
                        allRecords.remove(i);
                        allIds.remove(x);
                        numOfRecords--;
                    } else if (nextAction == 2) {
                        // do nothing
                    }
                    break;
                }
            }
        }
    }

    public static boolean duplicateIdsExist() {
        /*
        Reference for hashset:
        https://javarevisited.blogspot.com/2015/06/3-ways-to-find-duplicate-elements-in-array-java.html#axzz7hZCvBV46
         */
        HashSet<Integer> hCheckSet = new HashSet<>();
        HashSet<Integer> hTargetSet = new HashSet<>();
        for (Integer id : allIds) {
            if (!hCheckSet.add(id)) {
                hTargetSet.add(id);
            }
        }
        if (hTargetSet.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static void editRecord() {
        Scanner in = new Scanner(System.in);
        System.out.println("Please type the ID of the record that you wish to edit.");
        int recordToEdit = in.nextInt();
        in.nextLine();
        boolean recordExists = false;
        for (int i = 0; i < allIds.size(); i++) {
            if (allIds.get(i) == recordToEdit) {
                recordExists = true;
                break;
            }
        }
        if (!recordExists) {
            System.out.println("That record does not exist.");
        }
        if (recordExists) {
            for (int i = 0; i < numOfRecords; i++) {
                if (recordToEdit == allRecords.get(i).getID()) {
                    allRecords.get(i).edit();
                    break;
                }
            }
        }
    }

    public static String finalizeDatabase(int x, int specRecord) {
        if (x == 1) {
            String fieldNames = "";
            for (int i = 0; i < numOfFields; i++) {
                fieldNames = fieldNames + Record.fieldNames.get(i) + ",";
            }
            return fieldNames.substring(0, fieldNames.length() - 1);
        }
        String concatRecord = "";
        for (int i = 0; i < numOfFields; i++) {
            concatRecord = concatRecord + allRecords.get(specRecord).getFieldValue(i) + ",";
        }
        return concatRecord.substring(0, concatRecord.length() - 1);
    }

    public static ArrayList<String> findReqFields() throws Exception {
        ArrayList<String> reqFields = new ArrayList<String>();
        Scanner sc = new Scanner(new File(importedFilePath));
        String allFields = sc.nextLine();
        Scanner fields = new Scanner(allFields);
        fields.useDelimiter(",");
        for (int i = 0; i < getNumOfFields(); i++) {
            String x = fields.next();
            if (x.charAt(0) == '*') { // required fields indicated with a "*" as the first character
                reqFields.add(x);
            }
        }
        fields.close();
        sc.close();
        return reqFields;
    }

    public static int getNumOfFields() throws Exception {
        Scanner sc = new Scanner(new File(importedFilePath));
        sc.useDelimiter(",");    //sets the delimiter pattern
        int x = 0;
        int counter = 0;
        while (sc.hasNext()) {    //returns a boolean value
            String nextString = sc.next();
            if (x == 0) {
                nextString = sc.next();
                x++;
            }
            if (!(nextString.charAt(0) == 'i' || nextString.charAt(0) == 'd'
                    || nextString.charAt(0) == 's' || nextString.charAt(0) == '*')) {
                break;
            }
            counter++;
        }
        sc.close();
        return (counter + 1);
    }

    public static void print(int beingPrinted) {
        Scanner in = new Scanner(System.in);
        if (beingPrinted == 1) {
            for (int i = 0; i < Record.fieldNames.size(); i++) {
                System.out.print(Record.fieldNames.get(i) + " ");
            }
            System.out.println();
            for (int i = 0; i < numOfRecords; i++) {
                System.out.println(allRecords.get(i).getRecord());
            }
        } else if (beingPrinted == 2) {
            boolean recordExists = false;
            System.out.println("What is the ID of the record you wish to print?");
            int recordToPrint = in.nextInt();
            in.nextLine();
            for (int i = 0; i < allIds.size(); i++) {
                if (allIds.get(i) == recordToPrint) {
                    recordExists = true;
                    break;
                }
            }
            if (!recordExists) {
                System.out.println("That record does not exist.");
                System.out.println("Would you like to print a different record?\n1) Yes\n2) No");
                int x = in.nextInt();
                in.nextLine();
                if (x == 1) {
                    print(2);
                } else {
                    // do nothing
                }
            }
            if (recordExists) {
                for (int i = 0; i < numOfRecords; i++) {
                    if (recordToPrint == allRecords.get(i).getID()) {
                        System.out.println(allRecords.get(i).getRecord());
                        break;
                    }
                }
            }
        }
    }

    public static void setDatabase() throws Exception {
        numOfFields = getNumOfFields();
        Scanner sc = new Scanner(new File(importedFilePath));
        sc.useDelimiter(",");
        int counter = 0;
        ArrayList<ArrayList<String>> recordDatabase = new ArrayList<ArrayList<String>>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine(); // process the line
            ArrayList<String> scratchList = new ArrayList<String>();
            Scanner currentLine = new Scanner(line);
            currentLine.useDelimiter(",");
            while (currentLine.hasNext()) {
                scratchList.add(currentLine.next());
            }
            recordDatabase.add(scratchList);
            counter++;
        }
        sc.close();
        for (int i = 0; i < counter; i++) {
            if (i == 0) {
                for (int j = 0; j < numOfFields; j++) {
                    Record.fieldNames.add(recordDatabase.get(i).get(j));
                }
            } else {
                allRecords.add(new Record(recordDatabase.get(i))); // Var allRecords is arraylist of record objects
            }
        }
        numOfRecords = allRecords.size();
        for (int i = 0; i < numOfRecords; i++) {
            allIds.add(allRecords.get(i).getID());
        }
        boolean duplicates = duplicateIdsExist();
        if (duplicates) {
            System.out.println("There are duplicate IDs within this database, " +
                    "please enter a database without duplicate IDs.");
            System.exit(1); // Stops program if duplicate IDs are detected at the beginning
        }
        Record.requiredFields = new ArrayList<String>(findReqFields()); // ArrayList of which fields are required
    }

    public static void writeFile() {
        /*
        Reference for writeFile:
        https://www.geeksforgeeks.org/java-program-to-write-into-a-file/
         */
        Scanner sc = new Scanner(System.in);
        String text;
        System.out.println("Where do you wish to write this file to?");
        System.out.print("Please type in the name of the new file: ");
        exportedFilePath = sc.nextLine();
        System.out.println();
        try {
            FileWriter fWriter = new FileWriter(exportedFilePath); // Create a FileWriter object to write in the file
            //Creates new file with name given to "exportedFilePath"
            int x = 1;
            for (int i = -1; i < numOfRecords; i++) {
                text = finalizeDatabase(x, i);
                x++;
                fWriter.write(text + "\n");
                System.out.println(text); //prints content of file
            }
            fWriter.close();
            System.out.println("File is created successfully with the content.");
            //display message for successful completion of file
        }
        // Catch block to handle if exception occurs
        catch (IOException e) {
            // Print the exception
            System.out.print(e.getMessage());
        }
    }
}