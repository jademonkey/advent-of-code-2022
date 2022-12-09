import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Day9 {

    static final String EXAMPLE_INPUT_9 = "O:/Java/advent-of-code-2022/ExampleInputs/Day9";
    static final String EXAMPLE_INPUT_9_2 = "O:/Java/advent-of-code-2022/ExampleInputs/Day9_2";
    static final String ACTUAL_INPUT_9 = "O:/Java/advent-of-code-2022/Inputs/Day9";

    public static void main(String[] args) {
        BufferedReader br = null;
        HashMap<String, Integer> TailVisitSpots = new HashMap<String, Integer>();
        ArrayList<RopeKnot> knots = new ArrayList<RopeKnot>();

        /* Parse input */
        try {
            br = new BufferedReader(
                    new FileReader(ACTUAL_INPUT_9));
            int numberOfKnots = 10;
            RopeKnot headToAdd = new RopeKnot(0, 0, "head");
            knots.add(headToAdd);
            for (int i = 1; i < numberOfKnots; i++) {
                RopeKnot toAdd = new RopeKnot(0, 0, "Tail-" + i);
                knots.add(toAdd);
            }

            TailVisitSpots = markTailPos(knots, TailVisitSpots);

            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                /* parse! */
                String[] bits = line.split(" ");
                DIRECTION d = DIRECTION.getDir(bits[0]);
                int num = Integer.parseInt(bits[1]);

                for (int i = 0; i < num; i++) {
                    RopeKnot head = knots.get(0);
                    head = move(head, d);
                    knots.remove(0);
                    knots.add(0, head);

                    // System.out.println("Head at ");
                    // head.PrintPos();

                    // Move each knot in chain
                    for (int i2 = 1; i2 < numberOfKnots; i2++) {
                        RopeKnot curTail = knots.get(i2);
                        RopeKnot preKnot = knots.get(i2 - 1);
                        // System.out.println("Moving knot " + curTail.name + " at index " + i2 + "
                        // from: ");
                        // curTail.PrintPos();
                        curTail.MoveMe(preKnot.x, preKnot.y);
                        // System.out.println(" to: ");
                        // curTail.PrintPos();
                        knots.remove(i2);
                        knots.add(i2, curTail);
                    }

                    TailVisitSpots = markTailPos(knots, TailVisitSpots);
                }
            }

            System.out.println("Places visited = " + TailVisitSpots.size());

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

    public static HashMap<String, Integer> markTailPos(ArrayList<RopeKnot> list, HashMap<String, Integer> spots) {
        int x, y;
        HashMap<String, Integer> toreturn = spots;
        RopeKnot tail = list.get(list.size() - 1);
        x = tail.x;
        y = tail.y;

        // System.out.println("Tail at ");
        // tail.PrintPos();

        spots.put("" + x + "," + y, 0);
        return toreturn;
    }

    public static RopeKnot move(RopeKnot head, DIRECTION d) throws Exception {
        switch (d) {
            case UP:
                head.y++;
                break;
            case DOWN:
                head.y--;
                break;
            case LEFT:
                head.x--;
                break;
            case RIGHT:
                head.x++;
                break;
        }
        return head;
    }
}

class RopeKnot {
    public int x;
    public int y;
    public String name;

    public RopeKnot(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public void PrintPos() {
        System.out.print(x + "," + y);
        System.out.println();
    }

    public void MoveMe(int nextX, int nextY) throws Exception {
        int rowDist = nextY - y;
        int columnDist = nextX - x;
        if (columnDist == 0 && rowDist == 0) {
            // no move
            return;
        }

        if (columnDist == 0) {
            // Same column
            if (Math.abs(rowDist) == 1) {
                // close enough
                return;
            } else {
                // need to move
                if (rowDist == 2) {
                    y++;
                    return;
                } else if (rowDist == -2) {
                    y--;
                    return;
                }
            }
        } else if (rowDist == 0) {
            // Same row
            if (Math.abs(columnDist) == 1) {
                // close enough
                return;
            } else {
                // need to move
                if (columnDist == 2) {
                    x++;
                    return;
                } else if (columnDist == -2) {
                    x--;
                    return;
                }
            }
        } else {
            // diagonal
            if (Math.abs(columnDist) == 1 && Math.abs(rowDist) == 1) {
                // close enough
                return;
            }
            // Move 1 column
            if (columnDist > 0) {
                x++;
            } else {
                x--;
            }

            // move 1 row
            if (rowDist > 0) {
                y++;
            } else {
                y--;
            }
            return;
        }
        throw new Exception("Shouldn't have got here..." + columnDist + "," + rowDist);
    }
}

enum DIRECTION {
    UP, DOWN, LEFT, RIGHT;

    public static DIRECTION getDir(String input) throws Exception {
        char c = input.toLowerCase().charAt(0);

        switch (c) {
            case 'u':
                return UP;
            case 'd':
                return DOWN;
            case 'r':
                return RIGHT;
            case 'l':
                return LEFT;
            default:
                throw new Exception("Unknwon direction " + c);
        }
    }
}