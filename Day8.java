import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class Day8 {

    static final String EXAMPLE_INPUT_8 = "O:/Java/advent-of-code-2022/ExampleInputs/Day8";
    static final String ACTUAL_INPUT_8 = "O:/Java/advent-of-code-2022/Inputs/Day8";

    public static void main(String[] args) {
        BufferedReader br = null;
        int columns, rows;
        rows = 1;
        int[][] trees;
        HashMap<String, Integer> EdgeVisibleTreesPos = new HashMap<String, Integer>();
        String INPUT = ACTUAL_INPUT_8;
        int highestScenicScore = 0;

        /* Parse input */
        try {
            br = new BufferedReader(
                    new FileReader(INPUT));
            // First find the column and row
            String line = br.readLine();
            columns = line.length();
            while ((line = br.readLine()) != null) {
                rows++;
            }
            System.out.println("Cols = " + columns);
            System.out.println("Rows = " + rows);

            // Now make the array to hold the info
            trees = new int[columns][rows];
            // plant trees
            br.close();
            br = new BufferedReader(
                    new FileReader(INPUT));
            int currentRow = 0;
            while ((line = br.readLine()) != null) {
                String[] treesInRow = line.split("");
                for (int i = 0; i < columns; i++) {
                    trees[i][currentRow] = Integer.parseInt(treesInRow[i]);
                    if (currentRow == 0 || i == 0 || currentRow + 1 == trees[0].length || i + 1 == trees.length) {
                        // Edge tree so add to map now
                        EdgeVisibleTreesPos.put("" + currentRow + "," + i, trees[i][currentRow]);
                    }
                }
                currentRow++;
            }
            Print2DArray(trees);

            // Edges
            // part1VisibleTrees = (trees[0].length * 2) + (trees.length * 2) - 4;
            System.out.println("Edge trees = " + EdgeVisibleTreesPos.size());
            for (int x = 1; x < columns - 1; x++) {
                for (int y = 1; y < rows - 1; y++) {
                    // Consider this tree
                    int compareHeight = trees[x][y];
                    boolean blockedFromEdge = false;
                    int nTreeV = 0, eTreeV = 0, STreeV = 0, wTreeV = 0;

                    // visible from north boundary?
                    for (int up = y - 1; up >= 0; up--) {
                        nTreeV++;
                        if (trees[x][up] >= compareHeight) {
                            blockedFromEdge = true;
                            break;
                        }
                    }

                    // visible from south boundary?
                    blockedFromEdge = false;
                    for (int down = y + 1; down < rows; down++) {
                        STreeV++;
                        if (trees[x][down] >= compareHeight) {
                            blockedFromEdge = true;
                            break;
                        }
                    }

                    // visible from east boundary?
                    blockedFromEdge = false;
                    for (int right = x + 1; right < columns; right++) {
                        eTreeV++;
                        if (trees[right][y] >= compareHeight) {
                            blockedFromEdge = true;
                            break;
                        }
                    }

                    // visible from west boundary?
                    blockedFromEdge = false;
                    for (int left = x - 1; left >= 0; left--) {
                        wTreeV++;
                        if (trees[left][y] >= compareHeight) {
                            blockedFromEdge = true;
                            break;
                        }
                    }

                    // cannot be seen
                    if (!blockedFromEdge) {
                        // can be seen!
                        System.out
                                .println("Tree with height " + compareHeight + " at " + y + "," + x
                                        + "CAN be seen from edge");
                        EdgeVisibleTreesPos.put("" + y + "," + x, compareHeight);
                    } else {
                        System.out
                                .println("Tree with height " + compareHeight + " at " + y + "," + x
                                        + "cannot be seen from edge");
                    }

                    // Calculate scenic score
                    int thisScenicScore = nTreeV * eTreeV * STreeV * wTreeV;
                    if (thisScenicScore > highestScenicScore) {
                        highestScenicScore = thisScenicScore;
                    }
                }
            }

            System.out.println("total visible trees from edge = " + EdgeVisibleTreesPos.size());
            System.out.println("Highest score = " + highestScenicScore);
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

    private static void Print2DArray(int[][] trees) {
        for (int y = 0; y < trees[0].length; y++) {
            for (int x = 0; x < trees.length; x++) {
                System.out.print(trees[x][y]);
            }
            System.out.println();
        }
    }
}
