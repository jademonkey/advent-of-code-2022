import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

public class Day1 {

    static final String EXAMPLE_INPUT_1 = "O:/Java/advent-of-code-2022/ExampleInputs/Day1";

    static final String ACTUAL_INPUT_1 = "O:/Java/advent-of-code-2022/Inputs/Day1";

    public static void main(String[] args) {
        ArrayList<Integer> elvesCalories = new ArrayList<Integer>();
        int top3 = 0;
        /* Parse input */
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(ACTUAL_INPUT_1));
            try {
                int currentCals = 0;
                int i = 1;
                while (true) {
                    String line = br.readLine();
                    if (line == null) {
                        /* EOF */
                        break;
                    } else if (line.length() == 0) {
                        /* next elf */
                        elvesCalories.add(currentCals);
                        System.out.println("Elf " + i + ": " + currentCals);
                        currentCals = 0;
                        i++;
                    } else {
                        /* number! */
                        currentCals += Integer.parseInt(line);
                    }
                }
            } finally {
                br.close();
            }

            /* find max */
            Collections.sort(elvesCalories, Collections.reverseOrder());
            System.out.println("Max calories = " + elvesCalories.get(0));

            top3 = elvesCalories.get(0) + elvesCalories.get(1) + elvesCalories.get(2);

            System.out.println("Top 3 = " + top3);

        } catch (Exception e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
    }
}
