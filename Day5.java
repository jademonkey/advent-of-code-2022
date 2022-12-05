import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Day5 {

    static final String EXAMPLE_INPUT_5 = "C:/Users/101585866/Downloads/AOC/ExampleInputs/Day5";

    static final String ACTUAL_INPUT_5 = "C:/Users/101585866/Downloads/AOC/Inputs/Day5";

    public static void main(String[] args) {
        Part1Solution();
        Part2Solution();
    }

    private static void Part2Solution() {

    }

    private static void Part1Solution() {
        BufferedReader br = null;
        int height =0, width =0;
        String highestcrateP1 = "";
        String highestcrateP2 = "";
        String line;
        char[][] cratestacksPart1;
        char[][] cratestacksPart2;
        String INPUT = ACTUAL_INPUT_5;
        try {
            br = new BufferedReader(
                    new FileReader(INPUT));

            /* Find the crate stack dimensions */
            while (true) {
                line = br.readLine();
                if (line == null || line.length() == 0) {
                    /* EOF */
                    throw new Exception("Eearly finish");
                }
                if(line.charAt(0) == ' ' && line .charAt(1) != ' ')
                {
                    break;
                }
                else
                {
                    height++;
                }
            }
            /* This line is the width */
            width = Integer.parseInt(line.split(" ")[line.split(" ").length-1]);
            System.out.println("Height = " + height);
            System.out.println("Width = " + width);

            /* Make array for the data and reset */
            int actualHeight = 100;
            cratestacksPart1 = new char[width][actualHeight];
            cratestacksPart2 = new char[width][actualHeight];
            // set all to ' '
            for(int x =0; x < width; x++ ) {
                for (int y =0; y < actualHeight; y++) {
                    cratestacksPart1[x][y] = ' ';
                }
            }
            br.close();
            br = new BufferedReader(
                    new FileReader(INPUT));
            for(int h = height-1; h >= 0; h--) {
                line = br.readLine();
                int column = 0;
                // no need to check as we know this
                for(int p = 0; p < line.length(); p++){
                    if(line.charAt(p) == '[') {
                        cratestacksPart1[column][h] = line.charAt(p+1);
                    } else {
                        cratestacksPart1[column][h] = ' ';
                    }
                    // Next one
                    p += 3;
                    column++;
                }
            }
            /* clone for part 2 */
            for(int x =0; x < width; x++ ) {
                for (int y =0; y < actualHeight; y++) {
                    cratestacksPart2[x][y] = cratestacksPart1[x][y];
                }
            }

            System.out.println("--BEFORE--");
            for(int y=actualHeight-1; y>=0; y--){
                for(int x = 0; x < width; x++){
                    System.out.print(cratestacksPart1[x][y]);
                }
                System.out.println();
            }
            // Line points at the label so skip two
            line = br.readLine();
            line = br.readLine();
            /* Now we're at the instructions!! */
            while(true) {
                line = br.readLine();
                if (line == null || line.length() == 0) {
                    break;
                }
                // 0   1   2  3  4 5
                //move 1 from 2 to 1
                String[] actions = line.split(" ");
                int num, from, to;
                num = Integer.parseInt(actions[1]);
                from = Integer.parseInt(actions[3])-1; //to index
                to = Integer.parseInt(actions[5])-1;//to index

                cratestacksPart1 = manipulate9000(cratestacksPart1, num, from, to, actualHeight);
                cratestacksPart2 = manipulate9001(cratestacksPart2, num, from, to, actualHeight);
            }

            System.out.println("--AFTER--");
            for(int y=actualHeight-1; y>=0; y--){
                for(int x = 0; x < width; x++){
                    System.out.print(cratestacksPart1[x][y]);
                }
                System.out.println();
            }

            /* find the highest in each stack */
            for(int x =0; x < width; x++) {
                for(int y=actualHeight-1; y>=0; y--){
                    if(cratestacksPart1[x][y] !=' ') {
                        highestcrateP1 += cratestacksPart1[x][y];
                        break;
                    }
                }
            }

            for(int x =0; x < width; x++) {
                for(int y=actualHeight-1; y>=0; y--){
                    if(cratestacksPart2[x][y] !=' ') {
                        highestcrateP2 += cratestacksPart2[x][y];
                        break;
                    }
                }
            }

            
            System.out.println("HighestCratesPart1: " + highestcrateP1);
            System.out.println("HighestCratesPart2: " + highestcrateP2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static char[][] manipulate9000(char[][] stack, int num, int from, int to, int height)
    {
        char[][] toReturn = stack;
        char crateToMove =' ';

        for(int i = 0; i < num; i++){
            /* find the top crate of the from stack */
            for(int y=height-1; y>=0; y--){
                crateToMove = toReturn[from][y];
                if(crateToMove != 0 && crateToMove != ' ') {
                    toReturn[from][y] = ' ';
                    break;
                }
            }
            /* we have it move it to the top of the previous stack*/
            boolean found = false;
            char findBlank;
            for(int y=height-1; y>=0; y--){
                findBlank = toReturn[to][y];
                if(findBlank != ' ') {
                    found = true;
                    toReturn[to][y+1] = crateToMove;
                    break;
                }
            }
            if(!found) {
                // bottom of stack case
                toReturn[to][0] = crateToMove;
            }
        }

        return toReturn;
    }

    private static char[][] manipulate9001(char[][] stack, int num, int from, int to, int height)
    {
        char[][] toReturn = stack;
        char[] armhold = new char[num];


        int y = height-1;
        for(int i = 0; i < num; i++){
            /* find the top crate of the from stack */
            for(; y>=0; y--){
                armhold[i] = toReturn[from][y];
                if(armhold[i] != 0 && armhold[i] != ' ') {
                    toReturn[from][y] = ' ';
                    break;
                }
            }
        }
        /* we have all the boxes now move them to the top of the target stack */
        boolean found = false;
        char findBlank;
        for(y=height-1; y>=0; y--){
            findBlank = toReturn[to][y];
            if(findBlank != ' ') {
                found = true;
                y+=1;
                for(int i = num -1; i >= 0; i--) {
                    toReturn[to][y] = armhold[i];
                    y+=1;
                } 
                break;
            }
        }
        if(!found) {
            // bottom of stack case
            y = 0;
            for(int i = num -1; i >= 0; i--) {
                toReturn[to][y] = armhold[i];
                y+=1;
            }
        }

        return toReturn;
    }
}
