import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static java.nio.file.StandardOpenOption.CREATE;

/**
 * ProductReader - Reads product data from delimited text files
 * This class handles all file reading operations for Product records
 */
public class ProductReader {

    /**
     * Reads a delimited Product data file and returns the records as an ArrayList
     * @return ArrayList of Product records as strings, or null if no file selected
     */
    public static ArrayList<String> readProductFile() {
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
     * Reads and parses a delimited Product data file, printing formatted output
     * Expected format: ID, Name, Description, Cost
     */
    public static void readAndDisplayProductFile() {
        final int FIELDS_LENGTH = 4;

        String id, name, description;
        double cost;

        ArrayList<String> lines = readProductFile();

        if(lines == null || lines.isEmpty()) {
            System.out.println("No data to process.");
            return;
        }

        // Print header with column titles
        String header = String.format("\n%-10s %-25s %-40s %s",
                "ID#", "Name", "Description", "Cost");
        System.out.println(header);
        System.out.println("=".repeat(85));

        // Process each line and split into fields
        String[] fields;
        for(String l : lines) {
            fields = l.split(","); // Split the record into fields

            if(fields.length == FIELDS_LENGTH) {
                id          = fields[0].trim();
                name        = fields[1].trim();
                description = fields[2].trim();

                // Remove dollar sign if present before parsing
                String costStr = fields[3].trim();
                if(costStr.startsWith("$")) {
                    costStr = costStr.substring(1);
                }
                cost = Double.parseDouble(costStr);

                // Use String.format to create neatly formatted columnar display
                String formattedRecord = String.format("%-10s %-25s %-40s $%8.2f",
                        id, name, description, cost);
                System.out.println(formattedRecord);
            } else {
                System.out.println("Found a record that may be corrupt: ");
                System.out.println(l);
            }
        }
    }

    /**
     * Main method for testing ProductReader
     */
    public static void main(String[] args) {
        readAndDisplayProductFile();
    }
}
