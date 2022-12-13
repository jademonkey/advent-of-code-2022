import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.*;

public class Day12 {

    static final String EXAMPLE_INPUT_12 = "C:/Users/101585866/Downloads/AOC/ExampleInputs/Day12";
    static final String ACTUAL_INPUT_12 = "C:/Users/101585866/Downloads/AOC/Inputs/Day12";

    public static void main(String[] args) {
        BufferedReader br = null;
        String INPUT = ACTUAL_INPUT_12;
        JFrame frame = new JFrame("test");
        JTextArea textArea;

        AStarNode[][] grid;

        int x, y = 0;

        int StartX = 0, StartY = 0;
        int EndX = 0, EndY = 0;
        int curXNode, curYNode;

        /* Parse input */
        try {
            br = new BufferedReader(
                    new FileReader(INPUT));

            // monkey header or none
            String line = br.readLine();
            if (line == null) {
                throw new Exception("early end");
            }
            /* parse! */
            // find size of stuff
            y++;
            x = line.length();
            while ((line = br.readLine()) != null) {
                y++;
            }
            // bound set, reopen for height map generation
            br.close();
            br = new BufferedReader(
                    new FileReader(INPUT));
            grid = new AStarNode[y][x];

            // System.out.println(String.format("Grid bounds [%d][%d]", y,x));

            int curY = 0;

            while ((line = br.readLine()) != null) {
                for (int xP = 0; xP < x; xP++) {
                    char check = line.charAt(xP);
                    boolean end = false;
                    if (check == 'S') {
                        // start
                        StartX = xP;
                        StartY = curY;
                        check = 'a';
                    } else if (check == 'E') {
                        // end
                        EndX = xP;
                        EndY = curY;
                        check = 'z';
                        end = true;
                    }
                    grid[curY][xP] = new AStarNode((int) check, end);
                }
                curY++;
            }

            System.out.println("Grid created. finding A*");

            PrintGrid(grid);

            textArea = new JTextArea(y, x);
            textArea.setEditable(false);
            Font f = new Font("Consolas", Font.PLAIN, 12);
            textArea.setFont(f);
            frame.add(textArea);
            frame.pack();
            frame.setAlwaysOnTop(true);

            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // ok we have the grid. mark the start and set our pointers there
            grid[StartY][StartX].gScore = 0;
            grid[StartY][StartX].fScore = hScoreGuess(StartX, StartY, EndX, EndY);
            grid[StartY][StartX].start = true;
            curXNode = StartX;
            curYNode = StartY;
            int toConsider = x * y;

            // A* that grid
            while (stillTOVisit(grid)) {
                // find current
                int lowestfScore = Integer.MAX_VALUE;
                for (y = 0; y < grid.length; y++) {
                    for (x = 0; x < grid[0].length; x++) {
                        if (grid[y][x].visited == false && grid[y][x].fScore < lowestfScore) {
                            curYNode = y;
                            curXNode = x;
                            lowestfScore = grid[y][x].fScore;
                        }
                    }
                }

                frame.setTitle(String.format("Considering %d nodes - fscore %d", toConsider, lowestfScore));

                if (grid[curYNode][curXNode].end) {
                    // we at the end!
                    break;
                }
                textArea.setText(GetGridInProgress(grid, curXNode, curYNode));
                // PrintGridInProgress(grid,curXNode,curYNode);

                grid[curYNode][curXNode].visited = true; // removes from set
                toConsider--;
                // Check our neighbours
                int tentative_gScore;
                // North
                int lookX = curXNode;
                int lookY = curYNode - 1;
                if (lookY >= 0 && lookY < y) {
                    // we can look north
                    if (-1 <= grid[curYNode][curXNode].height - grid[lookY][lookX].height) {
                        // we can move this way
                        tentative_gScore = grid[curYNode][curXNode].gScore + 1;
                    } else {
                        // we can't move this way
                        tentative_gScore = Integer.MAX_VALUE;
                    }
                    if (tentative_gScore < grid[lookY][lookX].gScore) {
                        // this path is better than neighbour
                        grid[lookY][lookX].gScore = tentative_gScore;
                        grid[lookY][lookX].fScore = tentative_gScore + hScoreGuess(lookX, lookY, EndX, EndY);
                        if (grid[lookY][lookX].visited) {
                            grid[lookY][lookX].visited = false;
                            toConsider++;
                        }
                    }
                }
                // South
                lookY = curYNode + 1;
                if (lookY >= 0 && lookY < y) {
                    // we can look south
                    if (-1 <= grid[curYNode][curXNode].height - grid[lookY][lookX].height) {
                        // we can move this way
                        tentative_gScore = grid[curYNode][curXNode].gScore + 1;
                    } else {
                        // we can't move this way
                        tentative_gScore = Integer.MAX_VALUE;
                    }
                    if (tentative_gScore < grid[lookY][lookX].gScore) {
                        // this path is better than neighbour
                        grid[lookY][lookX].gScore = tentative_gScore;
                        grid[lookY][lookX].fScore = tentative_gScore + hScoreGuess(lookX, lookY, EndX, EndY);
                        if (grid[lookY][lookX].visited) {
                            grid[lookY][lookX].visited = false;
                            toConsider++;
                        }
                    }
                }
                lookY = curYNode;
                lookX = curXNode - 1;
                // West
                if (lookX >= 0 && lookX < x) {
                    // we can look West
                    if (-1 <= grid[curYNode][curXNode].height - grid[lookY][lookX].height) {
                        // we can move this way
                        tentative_gScore = grid[curYNode][curXNode].gScore + 1;
                    } else {
                        // we can't move this way
                        tentative_gScore = Integer.MAX_VALUE;
                    }
                    if (tentative_gScore < grid[lookY][lookX].gScore) {
                        // this path is better than neighbour
                        grid[lookY][lookX].gScore = tentative_gScore;
                        grid[lookY][lookX].fScore = tentative_gScore + hScoreGuess(lookX, lookY, EndX, EndY);
                        if (grid[lookY][lookX].visited) {
                            grid[lookY][lookX].visited = false;
                            toConsider++;
                        }
                    }
                }
                // East
                lookX = curXNode + 1;
                if (lookX >= 0 && lookX < x) {
                    // we can look East
                    if (-1 <= grid[curYNode][curXNode].height - grid[lookY][lookX].height) {
                        // we can move this way
                        tentative_gScore = grid[curYNode][curXNode].gScore + 1;
                    } else {
                        // we can't move this way
                        tentative_gScore = Integer.MAX_VALUE;
                    }
                    if (tentative_gScore < grid[lookY][lookX].gScore) {
                        // this path is better than neighbour
                        grid[lookY][lookX].gScore = tentative_gScore;
                        grid[lookY][lookX].fScore = tentative_gScore + hScoreGuess(lookX, lookY, EndX, EndY);
                        if (grid[lookY][lookX].visited) {
                            grid[lookY][lookX].visited = false;
                            toConsider++;
                        }
                    }
                }
            }

            // print some stuff out
            grid[EndY][EndX].Print();

            System.out.println("--press enter to continue to part 2--");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();

            int allToCheck = grid.length * grid[0].length;
            int currentChecking = 0;

            System.out.println("Finding shortest to lowest");
            int shortestToA = Integer.MAX_VALUE;
            int shortestTX = 0, shortestTY = 0;
            for (int newTargetY = 0; newTargetY < grid.length; newTargetY++) {
                aa: for (int newTargetX = 0; newTargetX < grid[0].length; newTargetX++) {
                    currentChecking++;
                    if (grid[newTargetY][newTargetX].height != (int) 'a') {
                        continue;
                    }
                    // new target!
                    System.out.println(String.format("checking %d out of %d", currentChecking, allToCheck));

                    // reset grid
                    for (y = 0; y < grid.length; y++) {
                        for (x = 0; x < grid[0].length; x++) {
                            grid[y][x].visited = false;
                            grid[y][x].fScore = Integer.MAX_VALUE;
                            grid[y][x].gScore = Integer.MAX_VALUE;
                            if (grid[y][x].end) {
                                // now the start
                                grid[y][x].gScore = 0;
                                grid[y][x].fScore = hScoreGuess(y, x, newTargetX, newTargetY);
                            }
                        }
                    }
                    toConsider = allToCheck;

                    // System.out.println(GetGridInProgress(gridPart2, 0, 0));
                    // textArea.setText(GetGridInProgress(gridPart2,curXNode,curYNode));

                    // A* again
                    // A* that grid
                    while (stillTOVisit(grid)) {
                        // find current
                        int lowestfScore = Integer.MAX_VALUE;
                        for (y = 0; y < grid.length; y++) {
                            for (x = 0; x < grid[0].length; x++) {
                                if (grid[y][x].visited == false && grid[y][x].fScore < lowestfScore) {
                                    curYNode = y;
                                    curXNode = x;
                                    lowestfScore = grid[y][x].fScore;
                                }
                            }
                        }

                        if (lowestfScore == Integer.MAX_VALUE) {
                            // probably got stuck
                            continue aa;
                        }

                        // frame.setTitle(String.format("Considering %d nodes - fscore %d", toConsider,
                        // lowestfScore));

                        if (curYNode == newTargetY && curXNode == newTargetX) {
                            // we at the end!
                            break;
                        }
                        // textArea.setText(GetGridInProgress(grid,curXNode,curYNode));
                        // PrintGridInProgress(grid,curXNode,curYNode);

                        grid[curYNode][curXNode].visited = true; // removes from set
                        toConsider--;

                        // Check our neighbours
                        int tentative_gScore;
                        // North
                        int lookX = curXNode;
                        int lookY = curYNode - 1;
                        if (lookY >= 0 && lookY < y) {
                            // we can look north
                            if (1 >= grid[curYNode][curXNode].height - grid[lookY][lookX].height) {
                                // we can move this way
                                tentative_gScore = grid[curYNode][curXNode].gScore + 1;
                            } else {
                                // we can't move this way
                                tentative_gScore = Integer.MAX_VALUE;
                            }
                            if (tentative_gScore < grid[lookY][lookX].gScore) {
                                // this path is better than neighbour
                                grid[lookY][lookX].gScore = tentative_gScore;
                                grid[lookY][lookX].fScore = tentative_gScore
                                        + hScoreGuess(lookX, lookY, newTargetX, newTargetY);
                                if (grid[lookY][lookX].visited) {
                                    grid[lookY][lookX].visited = false;
                                    toConsider++;
                                }
                            }
                        }
                        // South
                        lookY = curYNode + 1;
                        if (lookY >= 0 && lookY < y) {
                            // we can look south
                            if (1 >= grid[curYNode][curXNode].height - grid[lookY][lookX].height) {
                                // we can move this way
                                tentative_gScore = grid[curYNode][curXNode].gScore + 1;
                            } else {
                                // we can't move this way
                                tentative_gScore = Integer.MAX_VALUE;
                            }
                            if (tentative_gScore < grid[lookY][lookX].gScore) {
                                // this path is better than neighbour
                                grid[lookY][lookX].gScore = tentative_gScore;
                                grid[lookY][lookX].fScore = tentative_gScore
                                        + hScoreGuess(lookX, lookY, newTargetX, newTargetY);
                                if (grid[lookY][lookX].visited) {
                                    grid[lookY][lookX].visited = false;
                                    toConsider++;
                                }
                            }
                        }
                        lookY = curYNode;
                        lookX = curXNode - 1;
                        // West
                        if (lookX >= 0 && lookX < x) {
                            // we can look West
                            if (1 >= grid[curYNode][curXNode].height - grid[lookY][lookX].height) {
                                // we can move this way
                                tentative_gScore = grid[curYNode][curXNode].gScore + 1;
                            } else {
                                // we can't move this way
                                tentative_gScore = Integer.MAX_VALUE;
                            }
                            if (tentative_gScore < grid[lookY][lookX].gScore) {
                                // this path is better than neighbour
                                grid[lookY][lookX].gScore = tentative_gScore;
                                grid[lookY][lookX].fScore = tentative_gScore
                                        + hScoreGuess(lookX, lookY, newTargetX, newTargetY);
                                if (grid[lookY][lookX].visited) {
                                    grid[lookY][lookX].visited = false;
                                    toConsider++;
                                }
                            }
                        }
                        // East
                        lookX = curXNode + 1;
                        if (lookX >= 0 && lookX < x) {
                            // we can look East
                            if (1 >= grid[curYNode][curXNode].height - grid[lookY][lookX].height) {
                                // we can move this way
                                tentative_gScore = grid[curYNode][curXNode].gScore + 1;
                            } else {
                                // we can't move this way
                                tentative_gScore = Integer.MAX_VALUE;
                            }
                            if (tentative_gScore < grid[lookY][lookX].gScore) {
                                // this path is better than neighbour
                                grid[lookY][lookX].gScore = tentative_gScore;
                                grid[lookY][lookX].fScore = tentative_gScore
                                        + hScoreGuess(lookX, lookY, newTargetX, newTargetY);
                                if (grid[lookY][lookX].visited) {
                                    grid[lookY][lookX].visited = false;
                                    toConsider++;
                                }
                            }
                        }
                    }
                    if (shortestToA > grid[curYNode][curXNode].gScore) {
                        shortestToA = grid[curYNode][curXNode].gScore;
                        shortestTX = curXNode;
                        shortestTY = curYNode;
                    }
                }
            }

            // let's visualise the shortest like a fool, just for Josie

            // print some stuff out
            System.out.println("shortest to A = " + shortestToA);

            System.out.println("--press enter to visulise to part 2--");
            scanner.nextLine();

            // reset grid
            for (y = 0; y < grid.length; y++) {
                for (x = 0; x < grid[0].length; x++) {
                    grid[y][x].visited = false;
                    grid[y][x].fScore = Integer.MAX_VALUE;
                    grid[y][x].gScore = Integer.MAX_VALUE;
                    if (grid[y][x].end) {
                        // now the start
                        grid[y][x].gScore = 0;
                        grid[y][x].fScore = hScoreGuess(y, x, shortestTX, shortestTY);
                    }
                }
            }
            toConsider = allToCheck;

            // System.out.println(GetGridInProgress(gridPart2, 0, 0));
            // textArea.setText(GetGridInProgress(gridPart2,curXNode,curYNode));

            // A* again
            // A* that grid
            while (stillTOVisit(grid)) {
                // find current
                int lowestfScore = Integer.MAX_VALUE;
                for (y = 0; y < grid.length; y++) {
                    for (x = 0; x < grid[0].length; x++) {
                        if (grid[y][x].visited == false && grid[y][x].fScore < lowestfScore) {
                            curYNode = y;
                            curXNode = x;
                            lowestfScore = grid[y][x].fScore;
                        }
                    }
                }

                frame.setTitle(String.format("Considering %d nodes - fscore %d", toConsider, lowestfScore));

                if (curYNode == shortestTY && curXNode == shortestTX) {
                    // we at the end!
                    break;
                }
                textArea.setText(GetGridInProgress(grid, curXNode, curYNode));
                // PrintGridInProgress(grid,curXNode,curYNode);

                grid[curYNode][curXNode].visited = true; // removes from set
                toConsider--;

                // Check our neighbours
                int tentative_gScore;
                // North
                int lookX = curXNode;
                int lookY = curYNode - 1;
                if (lookY >= 0 && lookY < y) {
                    // we can look north
                    if (1 >= grid[curYNode][curXNode].height - grid[lookY][lookX].height) {
                        // we can move this way
                        tentative_gScore = grid[curYNode][curXNode].gScore + 1;
                    } else {
                        // we can't move this way
                        tentative_gScore = Integer.MAX_VALUE;
                    }
                    if (tentative_gScore < grid[lookY][lookX].gScore) {
                        // this path is better than neighbour
                        grid[lookY][lookX].gScore = tentative_gScore;
                        grid[lookY][lookX].fScore = tentative_gScore
                                + hScoreGuess(lookX, lookY, shortestTX, shortestTY);
                        if (grid[lookY][lookX].visited) {
                            grid[lookY][lookX].visited = false;
                            toConsider++;
                        }
                    }
                }
                // South
                lookY = curYNode + 1;
                if (lookY >= 0 && lookY < y) {
                    // we can look south
                    if (1 >= grid[curYNode][curXNode].height - grid[lookY][lookX].height) {
                        // we can move this way
                        tentative_gScore = grid[curYNode][curXNode].gScore + 1;
                    } else {
                        // we can't move this way
                        tentative_gScore = Integer.MAX_VALUE;
                    }
                    if (tentative_gScore < grid[lookY][lookX].gScore) {
                        // this path is better than neighbour
                        grid[lookY][lookX].gScore = tentative_gScore;
                        grid[lookY][lookX].fScore = tentative_gScore
                                + hScoreGuess(lookX, lookY, shortestTX, shortestTY);
                        if (grid[lookY][lookX].visited) {
                            grid[lookY][lookX].visited = false;
                            toConsider++;
                        }
                    }
                }
                lookY = curYNode;
                lookX = curXNode - 1;
                // West
                if (lookX >= 0 && lookX < x) {
                    // we can look West
                    if (1 >= grid[curYNode][curXNode].height - grid[lookY][lookX].height) {
                        // we can move this way
                        tentative_gScore = grid[curYNode][curXNode].gScore + 1;
                    } else {
                        // we can't move this way
                        tentative_gScore = Integer.MAX_VALUE;
                    }
                    if (tentative_gScore < grid[lookY][lookX].gScore) {
                        // this path is better than neighbour
                        grid[lookY][lookX].gScore = tentative_gScore;
                        grid[lookY][lookX].fScore = tentative_gScore
                                + hScoreGuess(lookX, lookY, shortestTX, shortestTY);
                        if (grid[lookY][lookX].visited) {
                            grid[lookY][lookX].visited = false;
                            toConsider++;
                        }
                    }
                }
                // East
                lookX = curXNode + 1;
                if (lookX >= 0 && lookX < x) {
                    // we can look East
                    if (1 >= grid[curYNode][curXNode].height - grid[lookY][lookX].height) {
                        // we can move this way
                        tentative_gScore = grid[curYNode][curXNode].gScore + 1;
                    } else {
                        // we can't move this way
                        tentative_gScore = Integer.MAX_VALUE;
                    }
                    if (tentative_gScore < grid[lookY][lookX].gScore) {
                        // this path is better than neighbour
                        grid[lookY][lookX].gScore = tentative_gScore;
                        grid[lookY][lookX].fScore = tentative_gScore
                                + hScoreGuess(lookX, lookY, shortestTX, shortestTY);
                        if (grid[lookY][lookX].visited) {
                            grid[lookY][lookX].visited = false;
                            toConsider++;
                        }
                    }
                }
            }

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

    private static boolean stillTOVisit(AStarNode[][] grid) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (!grid[y][x].visited) {
                    return true;
                }
            }
        }
        return false;
    }

    // private static void PrintGridInProgress(AStarNode[][] grid, int curX, int
    // curY) {
    // for (int y = 0; y < grid.length; y++) {
    // for (int x = 0; x < grid[0].length; x++) {
    // if (grid[y][x].end) {
    // System.out.print("X");
    // } else if (grid[y][x].start) {
    // System.out.print("S");
    // } else if (curX == x && curY == y) {
    // System.out.print("+");
    // } else if (grid[y][x].visited == false) {
    // // System.out.print((char)grid[y][x].height);
    // System.out.print(".");
    // } else if (grid[y][x].visited == true) {
    // System.out.print("#");
    // }
    // }
    // System.out.println();
    // }
    // }

    private static String GetGridInProgress(AStarNode[][] grid, int curX, int curY) {
        String back = "";
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x].end) {
                    back += "X";
                } else if (grid[y][x].start) {
                    back += "S";
                } else if (curX == x && curY == y) {
                    back += "+";
                } else if (grid[y][x].visited == false) {
                    // back += (char)grid[y][x].height;
                    back += '.';
                } else if (grid[y][x].visited == true) {
                    back += "#";
                } else {
                    back += "?";
                }
            }
            back += "\n";
        }
        return back;
    }

    private static int hScoreGuess(int thisX, int thisY, int targetX, int targetY) {
        int xdiff = Math.abs(targetX - thisX);
        int yDiff = Math.abs(targetY - thisY);
        return xdiff + yDiff;
    }

    private static void PrintGrid(AStarNode[][] grid) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x].end) {
                    System.out.print("X");
                } else if (grid[y][x].start) {
                    System.out.print("S");
                } else {
                    System.out.print(String.format("%c", (char) grid[y][x].height));
                }
            }
            System.out.println();
        }
    }

    // private static void PrintGridF(AStarNode[][] grid) {
    // for (int y = 0; y < grid.length; y++) {
    // for (int x = 0; x < grid[0].length; x++) {
    // if (grid[y][x].end) {
    // System.out.print("[0X0]");
    // } else if (grid[y][x].start) {
    // System.out.print("[0S0]");
    // } else {
    // System.out.print(String.format("[%03d]", grid[y][x].fScore));
    // }
    // }
    // System.out.println();
    // }
    // }

    // private static void PrintGridFMAxN(AStarNode[][] grid) {
    // for (int y = 0; y < grid.length; y++) {
    // for (int x = 0; x < grid[0].length; x++) {
    // if (grid[y][x].end) {
    // System.out.print("[0X0]");
    // } else if (grid[y][x].start) {
    // System.out.print("[0S0]");
    // } else {
    // System.out.print(
    // String.format("[%03d]", (grid[y][x].fScore == Integer.MAX_VALUE) ? -1 :
    // grid[y][x].fScore));
    // }
    // }
    // System.out.println();
    // }
    // }

    // private static void PrintGridConsidered(AStarNode[][] grid) {
    // for (int y = 0; y < grid.length; y++) {
    // for (int x = 0; x < grid[0].length; x++) {
    // if (grid[y][x].end) {
    // System.out.print("X");
    // } else if (grid[y][x].start) {
    // System.out.print("S");
    // } else {
    // System.out.print(String.format("%c", (grid[y][x].fScore == Integer.MAX_VALUE)
    // ? '.' : '#'));
    // }
    // }
    // System.out.println();
    // }
    // }

    // private static void PrintGridG(AStarNode[][] grid) {
    // for (int y = 0; y < grid.length; y++) {
    // for (int x = 0; x < grid[0].length; x++) {
    // if (grid[y][x].end) {
    // System.out.print("[0X0]");
    // } else if (grid[y][x].start) {
    // System.out.print("[0S0]");
    // } else {
    // System.out.print(String.format("[%03d]", grid[y][x].gScore));
    // }
    // }
    // System.out.println();
    // }
    // }

    // private static void PrintGridH(AStarNode[][] grid) {
    // for (int y = 0; y < grid.length; y++) {
    // for (int x = 0; x < grid[0].length; x++) {
    // if (grid[y][x].end) {
    // System.out.print("[0X0]");
    // } else if (grid[y][x].start) {
    // System.out.print("[0S0]");
    // } else {
    // System.out.print(String.format("[%03d]", grid[y][x].gScore +
    // grid[y][x].fScore));
    // }
    // }
    // System.out.println();
    // }
    // }

    // private static void PrintGridP(AStarNode[][] grid) {
    // // find lowest F
    // int lowF = Integer.MAX_VALUE;
    // for (int y = 0; y < grid.length; y++) {
    // for (int x = 0; x < grid[0].length; x++) {
    // if (lowF > grid[y][x].fScore) {
    // lowF = grid[y][x].fScore;
    // }
    // }
    // }

    // PrintGridP(grid, lowF);
    // }

    // private static void PrintGridP(AStarNode[][] grid, int lowF) {
    // // print grid now
    // for (int y = 0; y < grid.length; y++) {
    // for (int x = 0; x < grid[0].length; x++) {
    // if (grid[y][x].end) {
    // System.out.print("X");
    // } else if (grid[y][x].start) {
    // System.out.print("S");
    // } else if (grid[y][x].fScore == lowF) {
    // System.out.print("#");
    // } else {
    // System.out.print(".");

    // }
    // }
    // System.out.println();
    // }
    // }
}

class AStarNode {
    int height;
    int gScore;
    int fScore;
    boolean end = false;
    boolean start = false;
    boolean visited = false;

    public AStarNode(int height, boolean end) {
        this.height = height;
        this.end = end;
        gScore = Integer.MAX_VALUE;
        fScore = Integer.MAX_VALUE;
    }

    public void Print() {
        System.out.println("gScore=" + gScore + " fScore=" + fScore);
    }
}
