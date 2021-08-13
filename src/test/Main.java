package test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Main {
    private static final String FILE_STRING = "Enter a full path to the database file\n(or to a new database file you want to create):";
    private static final String PREDICATE_STRING = "Enter a predicate:";
    private static final String NUMBER_STRING = "Enter a number of the cell you want to %s:\n";
    private static final String DESCRIPTION_STRING = "Enter a description of the %s product:\n";
    private static final String PRICE_STRING = "Enter a price of the product:";
    private static final String QUANTITY_STRING = "Enter a quantity of the product:";

    public static String redact(String str, int len) {
        for (int i = 1; i < str.length(); i++) {
            char previousChar = str.charAt(i - 1);
            char currentChar = str.charAt(i);
            boolean isUpCaseAfterLowCase = Character.isUpperCase(currentChar) && Character.isLowerCase(previousChar);
            boolean isPrevNonDigitOrLetter = !Character.isLetterOrDigit(previousChar) && previousChar != ' ';
            boolean isPointPrevNumber = previousChar == '.' && Character.isDigit(currentChar);
            boolean isOpenBracket = currentChar == '(' || currentChar == '[' || currentChar == '{' || currentChar == '\'' || currentChar == '\"';
            boolean isPreviousOpenBracket = previousChar == '(' || previousChar == '[' || previousChar == '{' || previousChar == '\'' || previousChar == '\"';
            boolean isPreviousTire = previousChar == '-';
            boolean isPreviousStar = previousChar == '*';

            if (isPointPrevNumber || isPreviousOpenBracket || isPreviousTire || isPreviousStar) {
            }
            else if (isUpCaseAfterLowCase || isPrevNonDigitOrLetter) {
                str = str.substring(0, i) + " " + str.substring(i);
            }
            else if (isOpenBracket) {
                str = str.substring(0, i) + " " + str.substring(i);
                i++;
            }
        }
        if (str.length() > len) {
            str = str.substring(0, len);
        }
        else {
            while (str.length() < len) {
                str += " ";
            }
        }
        return str;
    }
    public static List<String> getListOfStrings(String fileName) throws IOException {
        List<String> list = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String s;
        while ((s = reader.readLine()) != null) {
            list.add(s);
        }
        reader.close();
        return list;
    }
    public static String findStringByIndex(List<String> strings, int index) {
        for (String string : strings) {
            String s = "";
            for (char c: string.toCharArray()) {
                if (Character.isDigit(c)) s += c;
                else break;
            }
            if (s.equals(Integer.toString(index))) {
                return string;
            }
        }
        return null;
    }

    public static Integer findMaximalIndexAndIncrement(List<String> strings) {
        if (strings.isEmpty()) return 0;

        Integer max = null;
        int counter = 0;
        while (max == null) {
            if (counter >= strings.size()) return max;
            max = (strings.get(counter).length() >= 8) ?
                    Integer.parseInt(strings.get(counter).substring(0, 8).replaceAll(" ", ""))
                    : null;
            counter++;
        }

        while (counter < strings.size()) {
            if (strings.get(counter).length() < 8) {
                continue;
            }
            max = (Integer.parseInt(strings.get(counter).substring(0, 8).replaceAll(" ", "")) > max) ?
                    Integer.parseInt(strings.get(counter).substring(0, 8).replaceAll(" ", "")) :
                    max;
            counter++;
        }
        return ++max;
    }

    private static final String FIRST_STRING_IN_DOC = redact("#", 8) + redact("Description", 50) + redact("Price", 8) + redact("Quantity", 8);
    private static final String CREATOR = "\n\nCreator: @Staffsterr2002";

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(FILE_STRING);
        String fileName = reader.readLine();
        File file = new File(fileName);
        boolean fileCreated = file.createNewFile();

        List<String> allFileStrings = getListOfStrings(fileName);
        List<String> fileStrings;

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        if (fileCreated) {
            System.out.println("File created!");
        }
        else System.out.println("File founded!");

        try {
            fileStrings = (fileCreated || allFileStrings.isEmpty()) ? allFileStrings : allFileStrings.subList(1, allFileStrings.size() - 3);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("The file cannot be the base: or create new(empty) file, or use database file.".toUpperCase(), e);
        }

        System.out.println(PREDICATE_STRING);
        String choice = reader.readLine();
        switch (choice) {
            case "-u": {
                if (fileStrings.isEmpty()) {
                    System.out.println("Database is empty");
                    break;
                }
                System.out.printf(NUMBER_STRING, "update");
                String inputId = reader.readLine();
                String stringToReplace = findStringByIndex(fileStrings, Integer.parseInt(inputId));
                if (stringToReplace == null) {
                    System.out.println("Here is no " + inputId + " cell. Do you want to create new one? Y/N");
                    boolean toCreate = reader.readLine().equals("Y");
                    if (toCreate) {
                    } else
                        return;
                } else {
                    String id = redact(inputId, 8);
                    System.out.printf(DESCRIPTION_STRING, "updatable");
                    String productName = redact(reader.readLine(), 50);
                    System.out.println(PRICE_STRING);
                    String price = redact(reader.readLine(), 8);
                    System.out.println(QUANTITY_STRING);
                    String quantity = redact(reader.readLine(), 8);

                    String newString = id + productName + price + quantity;
                    fileStrings.set(fileStrings.indexOf(stringToReplace), newString);
                    System.out.println("Successfully updated");
                    break;
                }
            }
            case "-c": {
                String id = redact(findMaximalIndexAndIncrement(fileStrings).toString(), 8);
                System.out.printf(DESCRIPTION_STRING, "creatable");
                String productName = redact(reader.readLine(), 50);
                System.out.println(PRICE_STRING);
                String price = redact(reader.readLine(), 8);
                System.out.println(QUANTITY_STRING);
                String quantity = redact(reader.readLine(), 8);

                String newString = id + productName + price + quantity;
                fileStrings.add(newString);
                System.out.println("Successfully created");
                break;
            }
            case "-d": {
                if (fileStrings.isEmpty()) {
                    System.out.println("Database is empty");
                    break;
                }
                System.out.printf(NUMBER_STRING, "delete");
                String inputId = reader.readLine();
                String stringToDelete = findStringByIndex(fileStrings, Integer.parseInt(inputId));
                if (stringToDelete == null) {
                    System.out.println("Here is no " + inputId + " cell.");
                    return;
                }
                fileStrings.remove(stringToDelete);
                System.out.println("Successfully deleted. Deleted: " + stringToDelete);
                break;
            }
            default:
                System.out.println("Unknown predicate");
                return;
        }
        reader.close();
        writer.write(FIRST_STRING_IN_DOC);
        writer.write("\n" + fileStrings.get(0));
        if (!fileStrings.isEmpty()) {
            for (int i = 1; i < fileStrings.size(); i++) {
                writer.newLine();
                writer.write(fileStrings.get(i));
            }
        }
        writer.write(CREATOR);
        writer.write("\nLast update: " + new SimpleDateFormat("MMM d, yyyy / HH:mm:ss", Locale.ENGLISH).format(new Date()));
        writer.close();
    }
}
