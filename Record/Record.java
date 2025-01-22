/*
Name: UJ Nwokoye
Date: 10/16/2022
Purpose: OOP Database Project; record object to be used with database class
 */
import java.util.ArrayList;
import java.util.Scanner;

public class Record {

    public static ArrayList<String> fieldNames = new ArrayList<String>();
    public static ArrayList<String> requiredFields = new ArrayList<String>();
    public ArrayList<String> currentRecord = new ArrayList<String>();
    public Integer recordId;


    public Record(ArrayList<String> currentRecord) {
        for (int i = 0; i < currentRecord.size(); i++) {
            this.currentRecord.add(currentRecord.get(i));
        }
        this.recordId = getID();
    }

    public void edit() {
        Scanner in = new Scanner(System.in);
        System.out.println("The current record is: " + getRecord());
        System.out.println("Please type the number corresponding to the field which you wish to edit.");
        System.out.println("Which field do you wish to edit?");
        for (int i = 1; i < fieldNames.size() + 1; i++) {
            System.out.println(i + ". " + fieldNames.get(i - 1));
        }
        int fieldToEdit = (in.nextInt() - 1);
        in.nextLine();
        boolean isRequiredField = false;
        String toEdit = currentRecord.get(fieldToEdit);
        if (requiredFields.size() > 0) {
            for (int i = 0; i < requiredFields.size(); i++) {
                if (toEdit.equals(requiredFields.get(i))) {
                    isRequiredField = true;
                    break;
                }
            }
        } //checks if field is required
        if (fieldToEdit == 0) {
            System.out.println("You may not edit the ID of a record.");
        } else {
            System.out.println("The current value of " + fieldNames.get(fieldToEdit) + " is: "
                    + currentRecord.get(fieldToEdit));
            System.out.println("What would you like the new value to be?");
            String newValue = in.nextLine();
            String upperNewValue = newValue.toUpperCase();
            do {
                if (upperNewValue.equals("N/A")) {
                    if (isRequiredField) {
                        System.out.println("This is a required field. It must have a value");
                        in.next();
                        newValue = in.nextLine();
                    }
                } else {
                    isRequiredField = false;
                }
            } while (isRequiredField);
            System.out.println("Are you sure that you wish for \"" + newValue + "\" to replace \""
                    + currentRecord.get(fieldToEdit) + "\"?");
            System.out.println("1) Yes\n2) No");
            int confirmedEdit = in.nextInt();
            if (confirmedEdit == 1) {
                currentRecord.set((fieldToEdit), newValue);
            } else {
                // do nothing
            }
        }
        System.out.println("The current record is: " + getRecord());
    }

    public String getFieldValue(int x) {
        return currentRecord.get(x);
    }

    public Integer getID() {
        String x = currentRecord.get(0);
        Integer currentId = Integer.parseInt(x);
        return currentId;
    }

    public ArrayList<String> getRecord() {
        return currentRecord;
    }
}
