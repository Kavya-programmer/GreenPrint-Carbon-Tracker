 

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/** The application must save its state when closed
     */

public class StateManager {
    /** Saves all emission entries to the file "greenprint_state.txt"
     */
    public static void saveState(ArrayList<EmissionSource> list){

        try{
            //Create writer 
            BufferedWriter writer= new BufferedWriter(new FileWriter("greenprint_state.txt"));

            //Loop through emission entries

            for (EmissionSource e: list) {

                //Converting data into single line
                // Format: Type|ID|User|Value

                String line= e.getType() + "|" + e.getId() + "|" + e.getUser() + "|" + e.getValue();
                writer.write(line); // Write the line to the file
                writer.newLine();  // Used to move to the next line


            }

            writer.close(); // Closes the file
            System.out.println("State saved successfully");
        } catch(IOException e) {
            //Handles file errors
            System.out.println("Error saving state");
        }
        
    }

    public static ArrayList<EmissionSource> loadState(){
        ArrayList<EmissionSource> list= new ArrayList<>();

        try{
            BufferedReader reader = new BufferedReader(new FileReader("greenprint_state.txt"));
            String line;

            //Read file line by line 

            while((line=reader.readLine())!=null){
                String[] parts = line.split("\\|");

                //Extract values

                String type=parts[0];
                String id= parts[1];
                String user= parts[2];
                double value=Double.parseDouble(parts[3]);

                 // Recreate object
                EmissionSource e = new EmissionSource(type, id, user, value);

                // Add to list
                list.add(e);
            }

            // Close file after reading
            reader.close();

            System.out.println("State loaded successfully.");

        } catch (IOException e) {
            // If file doesn't exist or error occurs
            System.out.println("No previous state found.");
        }

        return list;
    }

   static class EmissionSource {
    private String type;
    private String id;
    private String user;
    private double value;

    public EmissionSource(String type, String id, String user, double value) {
        this.type = type;
        this.id = id;
        this.user = user;
        this.value = value;
    }

    public String getType() { 
        return type; 
    }
    public String getId() { 
        return id;
     }
    public String getUser() { 
        return user; 
    }
    public double getValue() { 
        return value; 
    }
}

  public static void main(String[] args) {

        // Load previous data
        ArrayList<EmissionSource> list = StateManager.loadState();

        // Add new data
        list.add(new EmissionSource("Transport", "T-001", "Alice", 120));

        // Save before exit
        StateManager.saveState(list);
    }
}