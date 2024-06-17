package listeners;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class InstructionReader {

    public String readInstructions(String fileName) {
        StringBuilder instructions = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                instructions.append(line).append("<br>"); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "<html>" + instructions.toString() + "</html>";
    }
}
