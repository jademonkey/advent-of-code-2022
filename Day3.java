import java.io.BufferedReader;
import java.io.FileReader;

public class Day3 {

    static final String EXAMPLE_INPUT_1 = "O:/Java/advent-of-code-2022/ExampleInputs/Day3";

    static final String ACTUAL_INPUT_1 = "O:/Java/advent-of-code-2022/Inputs/Day3";

    public static void main(String[] args) {
        Part1Solution();
        Part2Solution();
    }

    private static void Part2Solution() {
        BufferedReader br = null;
        try {
            int total = 0;
            br = new BufferedReader(
                    new FileReader(ACTUAL_INPUT_1));

            while (true) {
                String bag1, bag2, bag3;
                char common = 0;
                bag1 = br.readLine();
                if (bag1 == null || bag1.length() == 0) {
                    /* EOF */
                    break;
                }
                bag2 = br.readLine();
                bag3 = br.readLine();

                /* Find the common letter */
                for (int a = 0; a < bag1.length(); a++) {
                    boolean continueY = false;
                    /* look through second bag */
                    for (int b = 0; b < bag2.length(); b++) {
                        if (bag1.charAt(a) == bag2.charAt(b)) {
                            /* found that letter here! */
                            continueY = true;
                            break;
                        }
                    }
                    if (!continueY) { /* we didn't find that letter in bag2 so it's not common. Next one! */
                        continue;
                    }

                    /* look through third bag */
                    for (int c = 0; c < bag3.length(); c++) {
                        if (bag1.charAt(a) == bag3.charAt(c)) {
                            /* found that letter here! */
                            common = bag1.charAt(a);
                            continueY = false;
                            break;
                        }
                    }
                    if (!continueY) { /* We found the common one! */
                        break;
                    }
                }

                /* Convert to priority and add the total */
                total += CovertToPriority(common);
            }
            System.out.println("Part2=" + total);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void Part1Solution() {
        BufferedReader br = null;
        try {
            int total = 0;
            br = new BufferedReader(
                    new FileReader(ACTUAL_INPUT_1));

            while (true) {
                String compart1, compart2;
                String line = br.readLine();
                if (line == null || line.length() == 0) {
                    /* EOF */
                    break;
                }

                /* Split into two compartments */
                compart1 = line.substring(0, line.length() / 2);
                compart2 = line.substring(line.length() / 2);
                // System.out.println(compart1);
                // System.out.println(compart2);
                char common = 0;

                for (int i = 0; i < compart1.length(); i++) {
                    for (int b = 0; b < compart2.length(); b++) {
                        if (compart1.charAt(i) == compart2.charAt(b)) {
                            common = compart1.charAt(i);
                            break;
                        }
                    }
                    if (common != 0) {
                        break;
                    }
                }

                // System.out.println("Common=" + common);
                /* Convert to priority */
                total += CovertToPriority(common);

                // System.out.println("priority =" + total);

            }
            System.out.println("Part1=" + total);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int CovertToPriority(char c) {
        if (Character.isLowerCase(c)) {
            return (c - 96);
        } else {
            return (c - 38);
        }
    }
}
