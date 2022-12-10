import java.io.BufferedReader;
import java.io.FileReader;

public class Day10 {

    static final String EXAMPLE_INPUT_10 = "O:/Java/advent-of-code-2022/ExampleInputs/Day10";
    static final String ACTUAL_INPUT_10 = "O:/Java/advent-of-code-2022/Inputs/Day10";

    public static void main(String[] args) {
        int xregister = 1;
        int clockTick = 0;
        boolean addInProgress = false;
        int toAddValue = 0;
        boolean programRunning = true;
        BufferedReader br = null;
        int signalValueT = 0;

        final int width = 40;
        final int height = 7;
        int currentheight = 0;
        int currentwidth = 0;

        char[][] screen = new char[width][height];

        /* Parse input */
        try {
            br = new BufferedReader(
                    new FileReader(ACTUAL_INPUT_10));
            while (programRunning) {
                clockTick++;
                // Read signal strength
                switch (clockTick) {
                    case 20:
                    case 60:
                    case 100:
                    case 140:
                    case 180:
                    case 220:
                        System.out.println("clocktick " + clockTick + " x = " + xregister);
                        signalValueT += (xregister * clockTick);
                        break;
                }
                // draw pixel
                if (xregister - 1 == currentwidth || xregister == currentwidth || xregister + 1 == currentwidth) {
                    // draw lit
                    screen[currentwidth][currentheight] = '#';
                } else {
                    screen[currentwidth][currentheight] = '.';
                }
                currentwidth++;
                if (currentwidth == width) {
                    currentwidth = 0;
                    currentheight++;
                }

                if (addInProgress) {
                    addInProgress = false;
                    xregister += toAddValue;
                } else {
                    // read next command
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    /* parse! */
                    if (line.equalsIgnoreCase("noop")) {
                        // shrug
                    } else {
                        addInProgress = true;
                        toAddValue = Integer.parseInt(line.split(" ")[1]);
                    }
                }
            }
            System.out.println(signalValueT);
            PrintScreen(screen, width, height);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void PrintScreen(char[][] screen, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(screen[x][y]);
            }
            System.out.println();
        }
    }

}
