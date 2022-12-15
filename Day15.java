import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;

public class Day15 {

    static final String EXAMPLE_INPUT_15 = "C:/Users/101585866/Downloads/AOC/advent-of-code-2022-main/ExampleInputs/Day15";
    static final String ACTUAL_INPUT_15 = "C:/Users/101585866/Downloads/AOC/advent-of-code-2022-main/Inputs/Day15";

    public static void main(String[] args) {
        BufferedReader br = null;
        ArrayList<SensorBeacon> fullList = new ArrayList<SensorBeacon>();
        int minX = Integer.MAX_VALUE, maxX = -Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxY = -Integer.MAX_VALUE;
        int cornerstart = 0;
        int cornerend = 0;
        int targetY = 2000000;
        int maxBeaconCoord = 4000000;
        String INPUT = "";
        boolean doPrint = false;

        boolean runExample = false;

        if (runExample) {
            // example
            doPrint = true;
            INPUT = EXAMPLE_INPUT_15;
            targetY = 10;
            maxBeaconCoord = 20;
        } else {
            // actual
            doPrint = false;
            INPUT = ACTUAL_INPUT_15;
            targetY = 2000000;
            maxBeaconCoord = 4000000;
        }

        /* Parse input */
        try {
            br = new BufferedReader(
                    new FileReader(INPUT));

            while (true) {
                String strX, strY, strDX;
                int x, y, dX, dY;
                // monkey header or none
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                /* parse! */
                // Sensor at x=2, y=18: closest beacon is at x=-2, y=15
                line = line.substring(line.indexOf('=') + 1);
                strX = line.substring(0, line.indexOf(','));
                x = Integer.parseInt(strX);

                line = line.substring(line.indexOf('=') + 1);
                strY = line.substring(0, line.indexOf(':'));
                y = Integer.parseInt(strY);

                line = line.substring(line.indexOf('=') + 1);
                strDX = line.substring(0, line.indexOf(','));
                dX = Integer.parseInt(strDX);

                line = line.substring(line.indexOf('=') + 1);
                dY = Integer.parseInt(line);

                // find the bounds!
                if (x > maxX) {
                    maxX = x;
                }
                if (x < minX) {
                    minX = x;
                }
                if (y > maxY) {
                    maxY = y;
                }
                if (y < minY) {
                    minY = y;
                }

                if (dX > maxX) {
                    maxX = dX;
                }
                if (dX < minX) {
                    minX = dX;
                }
                if (dY > maxY) {
                    maxY = dY;
                }
                if (dY < minY) {
                    minY = dY;
                }
                fullList.add(new SensorBeacon(x, y, dX, dY));
            }

            if (cornerstart > minX) {
                cornerstart = minX;
            }
            if (cornerstart > minY) {
                cornerstart = minY;
            }

            if (cornerend < maxX) {
                cornerend = maxX;
            }
            if (cornerend < maxY) {
                cornerend = maxY;
            }

            // now find the biggest distance
            int largestDist = 0;
            for (SensorBeacon sensorBeacon : fullList) {
                sensorBeacon.Print();
                if (sensorBeacon.beaconDistance > largestDist) {
                    largestDist = sensorBeacon.beaconDistance;
                }
            }

            System.out.println("biggest dist = " + largestDist);
            // assume that a with largest distance is in each corner +2 for buffer
            cornerstart -= (largestDist + 2);
            cornerend += (largestDist + 2);

            // now cornerstart and corner end define the full area that could be

            System.out.println(
                    String.format("Sensor/Beacon Bounds = X: %d -> %d    Y: %d -> %d", minX, maxX, minY, maxY));
            System.out.println("corner start = " + cornerstart);
            System.out.println("corner end   = " + cornerend);

            // consider points in a row if they are in bounds of sensor (part 1)
            int posCovered = 0;
            for (int x = cornerstart; x <= cornerend; x++) {
                boolean isCovered = false;
                for (SensorBeacon sb : fullList) {
                    if (sb.beaconX == x && sb.BeaconY == targetY) {
                        // there's a beacon here!
                        isCovered = false;
                        break;
                    }
                    if (sb.pointIsCovered(x, targetY)) {
                        isCovered = true;
                        continue;
                    }
                }
                if (isCovered) {
                    posCovered++;
                }
            }

            // Print out the grid (example only because of size)
            if (doPrint) {
                System.out.println("--- Full grid---");
                for (int y = cornerstart; y <= cornerend; y++) {
                    for (int x = cornerstart; x <= cornerend; x++) {
                        boolean draw = true;
                        boolean pointWasCovered = false;

                        for (SensorBeacon sb : fullList) {
                            if (sb.sensorX == x && sb.sensorY == y) {
                                System.out.print("S");
                                draw = false;
                            } else if (sb.beaconX == x && sb.BeaconY == y) {
                                System.out.print("B");
                                draw = false;
                            }
                            if (!pointWasCovered && sb.pointIsCovered(x, y)) {
                                // posCovered++;
                                pointWasCovered = true;
                                break;
                            }
                        }
                        if (draw) {
                            if (pointWasCovered) {
                                System.out.print("#");
                            } else {
                                System.out.print(".");
                            }
                        }
                    }
                    System.out.println();
                }
            }

            // Find distress beacon (part 2) now with threads
            ArrayList<searcher> threadList = new ArrayList<searcher>();
            int numofthreads = 1;
            int splits = maxBeaconCoord / numofthreads;
            int curX = 0, curY = 0;
            int threadNum = 1;
            while (curX != maxBeaconCoord && curY != maxBeaconCoord) {
                searcher thread1 = new searcher(threadNum, curX, curY, curX+splits, curY+splits, fullList);
                threadList.add(thread1);
                threadNum++;
                curX += splits;
                if(curX == maxBeaconCoord) {
                    curX = 0;
                    curY += splits;
                }
            }

            for (searcher searcher : threadList) {
                searcher.start();
            }

            for (searcher searcher : threadList) {
                while (searcher.isAlive()) {
                }
            }

            // wait for all the threads to finish
            System.out.println("all threads finished");

            // Print out the grid (example only because of size)
            if (doPrint) {
                System.out.println("--- Beacon search grid---");
                for (int y = 0; y <= maxBeaconCoord; y++) {
                    for (int x = 0; x <= maxBeaconCoord; x++) {
                        boolean draw = true;
                        boolean pointWasCovered = false;

                        for (SensorBeacon sb : fullList) {
                            if (sb.sensorX == x && sb.sensorY == y) {
                                System.out.print("S");
                                draw = false;
                            } else if (sb.beaconX == x && sb.BeaconY == y) {
                                System.out.print("B");
                                draw = false;
                            }
                            if (!pointWasCovered && sb.pointIsCovered(x, y)) {
                                // posCovered++;
                                pointWasCovered = true;
                                break;
                            }
                        }
                        if (draw) {
                            if (pointWasCovered) {
                                System.out.print("#");
                            } else {
                                System.out.print(".");
                            }
                        }
                    }
                    System.out.println();
                }
            }

            System.out.println("pos covered = " + posCovered);
            BigInteger frequency = new BigInteger(""+threadList.get(0).gapX);
            frequency = frequency.multiply(new BigInteger("4000000"));
            frequency = frequency.add(new BigInteger("" + threadList.get(0).gapY));
            System.out.println("Frequency " + frequency.toString());
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
}

class searcher extends Thread {
    public static int gapX = 0;
    public static int gapY = 0;
    static boolean done = false;
    int x, y, maxX, maxY;
    ArrayList<SensorBeacon> fullList;
    int num;

    public searcher(int num, int x, int y, int maxX, int maxY, ArrayList<SensorBeacon> listofbeacons) {
        this.num = num;
        this.x = x;
        this.y = y;
        this.maxX = maxX;
        this.maxY = maxY;
        fullList = listofbeacons;

    }

    public int distanceBetweenPoints(int x, int y, int destX, int desY) {
        // taxi cab ditance
        int dist = Math.abs(x - destX) + Math.abs(y - desY);
        return dist;
    }

    public void run() {
        if (done)
            return;
        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
             //   System.out.println(String.format("Looking at %d,%d", x, y));
                boolean isInvalid = false;
                for (SensorBeacon sb : fullList) {
                    if (sb.pointIsCovered(x, y)) {
                        isInvalid = true;
                        // a.centre.x - c.x + a.size - absint(c.y-a.centre.y) + 1
                 //       sb.Print();
                        int toadd = sb.sensorX - x + (sb.beaconDistance - Math.abs(y - sb.sensorY));


                   //     System.out.println("Skipping " + toadd);
                        x += toadd;
                      //  done = true;     
                        break;            
                        }
                }
                if (!isInvalid) {
                    // here!
                    System.out.println(String.format("Found gap at %d,%d. Ending early", x, y));
                    gapX = x;
                    gapY = y;
                    done = true;
                    break;
                } else {
                    // add to x so we can skip over the distance
                }
                if (done)
                    break;
            }
            if (done)
                break;
        }
        System.out.println(String.format("Thread %d searching %d->%d %d->%d done!", num, x,maxX, y, maxY));
    }
}

class SensorBeacon {
    public int sensorX, sensorY, beaconX, BeaconY, beaconDistance;

    public SensorBeacon(int sensorX, int sensorY, int beaconX, int BeaconY) {
        this.sensorX = sensorX;
        this.sensorY = sensorY;
        this.beaconX = beaconX;
        this.BeaconY = BeaconY;

        beaconDistance = distanceToPoint(beaconX, BeaconY);
    }

    public int distanceToPoint(int destX, int desY) {
        // taxi cab ditance
        int dist = Math.abs(sensorX - destX) + Math.abs(sensorY - desY);
        return dist;
    }

    public boolean pointIsCovered(int x, int y) {
        int distToX = distanceToPoint(x, y);
        return distToX <= beaconDistance;
    }

    public void Print() {
        System.out.println(String.format("Sensor: %d,%d -> Beacon %d,%d  (%d)", sensorX, sensorY, beaconX, BeaconY,
                beaconDistance));

    }
}