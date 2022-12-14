import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.*;

public class Day14 {

    static final String EXAMPLE_INPUT_14 = "C:/Users/101585866/Downloads/AOC/ExampleInputs/Day14";
    static final String ACTUAL_INPUT_14 = "C:/Users/101585866/Downloads/AOC/Inputs/Day14";

    static JFrame frame = new JFrame("Day 14");
    static JTextArea textArea;

    public static void main(String[] args) {
        BufferedReader br = null;
        String INPUT = ACTUAL_INPUT_14;
        

        int minx = Integer.MAX_VALUE, maxx = 0, miny = 0, maxy = 0;
        int xOffset, yOffset, rested;
        ArryFill[][] cavern;

        /* Parse input */
        try {
            br = new BufferedReader(
                    new FileReader(INPUT));

            while (true) {
                // monkey header or none
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                /* parse! */

                // find bounds
                String[] splitts = line.split(" -> ");
                for (String string : splitts) {
                    System.out.println(string);
                    String[] finSplit = string.split(",");
                    int x, y;
                    x = Integer.parseInt(finSplit[0]);
                    y = Integer.parseInt(finSplit[1]);
                    if (x < minx) {
                        minx = x;
                    }
                    if (x > maxx) {
                        maxx = x;
                    }
                    if (y < miny) {
                        miny = y;
                    }
                    if (y > maxy) {
                        maxy = y;
                    }
                }
            }

            br.close();
            br = new BufferedReader(
                    new FileReader(INPUT));

            // build cavern
            System.out.println(String.format("bounds = X: %d -> %d, y: %d -> %d", minx, maxx, miny, maxy));
            xOffset = maxx - minx;
            yOffset = maxy - miny;
            cavern = new ArryFill[yOffset + 1][xOffset + 1];

            textArea = new JTextArea(yOffset + 1, xOffset + 1);
            textArea.setEditable(false);
            Font f = new Font("Consolas", Font.PLAIN, 8);
            textArea.setFont(f);
            frame.add(textArea);
            frame.pack();
            frame.setAlwaysOnTop(true);

            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            for (int y = 0; y < cavern.length; y++) {
                for (int x = 0; x < cavern[0].length; x++) {
                    cavern[y][x] = ArryFill.AIR;
                }
            }
            textArea.setText(getArrayString(cavern));
            System.out.println(getArrayString(cavern));

            while (true) {
                // monkey header or none
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                /* parse! */

                // find bounds
                String[] splitts = line.split(" -> ");
                for (int dest = 1; dest < splitts.length; dest++) {
                    System.out.println(String.format("line %s -> %s", splitts[dest], splitts[dest - 1]));
                    String[] endSplit = splitts[dest].split(",");
                    String[] startSplit = splitts[dest - 1].split(",");
                    int startX = Integer.parseInt(startSplit[0]);
                    int startY = Integer.parseInt(startSplit[1]);
                    int endX = Integer.parseInt(endSplit[0]);
                    int endY = Integer.parseInt(endSplit[1]);

                    if (startX > endX) {
                        // switch
                        int temp = endX;
                        endX = startX;
                        startX = temp;
                    }
                    if (startY > endY) {
                        // switch
                        int temp = endY;
                        endY = startY;
                        startY = temp;
                    }

                    // y or x line?
                    if ((startY - endY) == 0) {
                        // x line
                        for (int i = startX; i <= endX; i++) {
                            cavern[startY][i - minx] = ArryFill.ROCK;
                        }
                    } else {
                        // y line
                        for (int i = startY; i <= endY; i++) {
                            cavern[i][startX - minx] = ArryFill.ROCK;
                        }
                    }
                }
            }
            System.out.println("--cavern start--");
            System.out.println(getArrayString(cavern));
            textArea.setText(getArrayString(cavern));

            rested = produceSand(cavern, 500 - minx, 0);

            System.out.println("--cavern end--");
            System.out.println(getArrayString(cavern));
            textArea.setText(getArrayString(cavern));

            System.out.println("rested sand =" + rested);

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

    private static int produceSand(ArryFill[][] toPrint, int sourceX, int sourceY){
        int rested = -1;
        int currentX, currentY;
        int desiredX, desiredY;
        boolean falling;
        boolean end = false;
        
        while(!end) {
            // source another sand
            currentX = sourceX;
            currentY = sourceY;
            if(toPrint[currentY][currentX]!=ArryFill.AIR) {
                System.out.println("Source blocked");
                return rested;
            }
            toPrint[currentY][currentX] = ArryFill.SAND;
            falling = true;
            desiredX = currentX;
            desiredY = currentY+1;

            // now we make it fall until it is rested or off edge
            // try down
            while(falling){
                if(inBounds(toPrint, desiredX,desiredY)) {
                    // move down
                    if(toPrint[desiredY][desiredX] == ArryFill.AIR) {
                        toPrint[desiredY][desiredX] = ArryFill.SAND;
                    } 
                    else {
                        desiredX -= 1;
                        if(inBounds(toPrint, desiredX,desiredY)) {
                            // move down left
                            if(toPrint[desiredY][desiredX] == ArryFill.AIR) {
                                toPrint[desiredY][desiredX] = ArryFill.SAND;
                            } else {
                                desiredX += 2;
                                if(inBounds(toPrint, desiredX,desiredY)) {
                                    // move down right
                                    if(toPrint[desiredY][desiredX] == ArryFill.AIR) {
                                        toPrint[desiredY][desiredX] = ArryFill.SAND;
                                    } else {
                                        //resting
                                        break;
                                    }
                                } else {
                                    // falling forever
                                    end = true;
                                    break;
                                }
                            }
                        } else {
                            // falling forever
                            end = true;
                            break;
                        }
                    }
                } else {
                    // falling forever
                    end = true;
                    break;
                }
                toPrint[currentY][currentX] = ArryFill.AIR;
                currentX = desiredX;
                currentY = desiredY;
                desiredX = currentX;
                desiredY = currentY+1;
               // System.out.println(getArrayString(toPrint));
              //  textArea.setText(getArrayString(toPrint));
            }


            // it came to a rest!
            if(!end){
                rested++;
            }
        }

        return rested;
    }

    private static boolean inBounds(ArryFill[][] toPrint, int x, int y) {
        if (x < 0 || x >= toPrint[0].length) {
            return false;
        }
        if (y < 0 || y >= toPrint.length) {
            return false;
        }
        return true;
    }

    private static String getArrayString(ArryFill[][] toPrint) {
        String back = "";
        for (int y = 0; y < toPrint.length; y++) {
            back += String.format("%02d ", y);
            for (int x = 0; x < toPrint[0].length; x++) {
                switch (toPrint[y][x]) {
                    case AIR:
                        back += '.';
                        break;
                    case ROCK:
                        back += '#';
                        break;
                    case SAND:
                        back += 'o';
                        break;
                }
            }
            back += '\n';
        }
        return back;
    }
}

enum ArryFill {
    AIR,
    ROCK,
    SAND;
}