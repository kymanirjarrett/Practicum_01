import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static java.nio.file.StandardOpenOption.CREATE;

/**
 * PersonReader - Reads person data from delimited text files
 * This class handles all file reading operations for Person records
 */
public class PersonReader {

    /**
     * Reads a delimited Person data file and returns the records as an ArrayList
     * @return ArrayList of Person records as strings, or null if no file selected
     */
    public static ArrayList<String> readPersonFile() {
        JFileChooser chooser = new JFileChooser();
        File selectedFile;
        String rec = "";
        ArrayList<String> lines = new ArrayList<>();

        try {
            // use the toolkit to get the current working directory of the IDE
            File workingDirectory = new File(System.getProperty("user.dir"));

            chooser.setCurrentDirectory(workingDirectory);

            if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                Path file = selectedFile.toPath();

                // Wrap a BufferedReader around a BufferedInputStream
                InputStream in = new BufferedInputStream(Files.newInputStream(file, CREATE));
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                // Read all lines from the file
                int line = 0;
                while(reader.ready()) {
                    rec = reader.readLine();
                    lines.add(rec);
                    line++;
                    // echo to screen
                    System.out.printf("\nLine %4d %-60s ", line, rec);
                }
                reader.close();
                System.out.println("\n\nData file read!");

                return lines;
            } else {
                System.out.println("No file selected!!! ... exiting.");
                return null;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!!!");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads and parses a delimited Person data file, printing formatted output
     * Expected format: ID, FirstName, LastName, Title, YearOfBirth
     */
    public static void readAndDisplayPersonFile() {
        final int FIELDS_LENGTH = 5;

        String id, firstName, lastName, title;
        int yob;

        ArrayList<String> lines = readPersonFile();

        if(lines == null || lines.isEmpty()) {
            System.out.println("No data to process.");
            return;
        }

        // Print header with column titles
        String header = String.format("\n%-10s %-20s %-20s %-8s %s",
                "ID#", "Firstname", "Lastname", "Title", "YOB");
        System.out.println(header);
        System.out.println("=".repeat(80));

        // Process each line and split into fields
        String[] fields;
        for(String l : lines) {
            fields = l.split(","); // Split the record into fields

            if(fields.length == FIELDS_LENGTH) {
                id        = fields[0].trim();
                firstName = fields[1].trim();
                lastName  = fields[2].trim();
                title     = fields[3].trim();
                yob       = Integer.parseInt(fields[4].trim());

                // Use String.format to create neatly formatted columnar display
                String formattedRecord = String.format("%-10s %-20s %-20s %-8s %d",
                        id, firstName, lastName, title, yob);
                System.out.println(formattedRecord);
            } else {
                System.out.println("Found a record that may be corrupt: ");
                System.out.println(l);
            }
        }
    }

    /**
     * Main method for testing PersonReader
     */
    public static void main(String[] args) {
        readAndDisplayPersonFile();
    }
}
