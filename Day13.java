import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Day13 {

    static final String EXAMPLE_INPUT_13 = "O:/Java/advent-of-code-2022/ExampleInputs/Day13";
    static final String ACTUAL_INPUT_13 = "O:/Java/advent-of-code-2022/Inputs/Day13";

    public static void main(String[] args) {
        BufferedReader br = null;
        ArrayList<IntOrList> fullList = new ArrayList<IntOrList>();
        int rightOrderPairTotals = 0;

        /* Parse input */
        try {
            br = new BufferedReader(
                    new FileReader(ACTUAL_INPUT_13));
            while (true) {
                /* read the first line which is cd / to get to the root directory */
                String line = br.readLine();
                if (line == null) {
                    /* EOF */
                    break;
                }
                if (line.length() == 0) {
                    // blank line
                    continue;
                }
                /* Parse! */
                IntOrList currentLineRoot = new IntOrList(INTLIST.LIST);
                // trim first and last [ ]
                if (line.length() == 2) {
                    // just []
                    // System.out.println(line);
                    // add to list as empty
                    fullList.add(currentLineRoot);
                    continue;
                } else {
                    line = line.substring(1, line.length() - 1);
                }
                fullList.add(buildList(currentLineRoot, line));
            }

            /* Compare! pairs */
            // for (IntOrList i : fullList) {
            // i.Print();
            // System.out.println();
            // }
            int pair = 1;
            for (int i = 0; i < fullList.size(); i += 2) {
                INTLISTCOMPARE comparre = new INTLISTCOMPARE();
                // System.out.print("Comparing: ");
                // fullList.get(i).Print();
                // System.out.println();
                // System.out.print(" with: ");
                // fullList.get(i + 1).Print();
                // System.out.println();

                int compareResult = comparre.compare(fullList.get(i), fullList.get(i + 1));

                if (compareResult == 1) {
                    // System.out.println(String.format("Pair %d was in wrong order", pair));
                } else if (compareResult == -1) {
                    // System.out.println(String.format("Pair %d was in right order", pair));
                    rightOrderPairTotals += pair;
                } else {
                    // 0?
                    // System.out.println("Pair %d was inconclusive");
                }
                pair++;
            }
            // System.out.println("Number of wrong orders=" + wrongOrderC);
            System.out.println("Part1=" + rightOrderPairTotals);

            /* PART 2 */
            /* add the two new ones */
            String line1 = "[2]"; // Purposefully dropping the enclosing [] as my copied logic does it like that
            String line2 = "[6]";
            IntOrList newAdd1 = new IntOrList(INTLIST.LIST);
            newAdd1.decoder = true;
            fullList.add(buildList(newAdd1, line1));
            IntOrList newAdd2 = new IntOrList(INTLIST.LIST);
            newAdd2.decoder = true;
            fullList.add(buildList(newAdd2, line2));

            // Now sort! (urgh)
            int Part2 = 1;
            Collections.sort(fullList, new INTLISTCOMPARE());
            for (int i = 0; i < fullList.size(); i++) {
                IntOrList thisone = fullList.get(i);
                if (thisone.decoder) {
                    Part2 *= (i + 1);
                }
                thisone.Print();
                System.out.println();
            }

            System.out.println("Part2=" + Part2);

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

    private static IntOrList buildList(IntOrList thisone, String line) {
        while (line.length() != 0) {
            if (line.charAt(0) != '[') {
                IntOrList toAdd = new IntOrList(INTLIST.INT);
                int nextComma = line.indexOf(',');
                if (nextComma == -1) {
                    // end of string?
                    nextComma = line.length();
                }
                toAdd.value = Integer.parseInt(line.substring(0, nextComma));
                // System.out.println("adding int! " + toAdd.value);
                // Int!
                thisone.nested.add(toAdd);
                if (nextComma == line.length()) {
                    line = "";
                } else {
                    line = line.substring(nextComma + 1);
                }
            } else {
                // list!
                IntOrList toAdd = new IntOrList(INTLIST.LIST);
                String theirline = line.substring(1, findMatchingBracket(line));
                // System.out.println("adding array " + theirline);
                thisone.nested.add(buildList(toAdd, theirline));
                if (theirline.length() + 3 >= line.length()) {// [],
                    line = "";
                } else {
                    line = line.substring(theirline.length() + 3);
                }
            }
        }

        return thisone;
    }

    private static int findMatchingBracket(String line) {
        int openedBrackets = 0;
        for (int i = 0; i < line.length(); i++) {
            switch (line.charAt(i)) {
                case '[':
                    openedBrackets++;
                    break;
                case ']':
                    openedBrackets--;
                    if (openedBrackets == 0) {
                        // found it
                        return i;
                    }
            }
        }
        return -1;
    }
}

class IntOrList {
    public INTLIST thisType;
    public int value;
    public ArrayList<IntOrList> nested = new ArrayList<IntOrList>();
    public boolean decoder = false;

    public IntOrList(INTLIST thTyp) {
        thisType = thTyp;
    }

    public void Print() {
        if (thisType == INTLIST.INT) {
            System.out.print(value);
        } else {
            System.out.print("[");
            boolean skipone = false;
            for (IntOrList i : nested) {
                if (skipone) {
                    System.out.print(",");
                } else {
                    skipone = true;
                }
                i.Print();
            }
            System.out.print("]");
        }
    }
}

enum INTLIST {
    INT,
    LIST;
}

class INTLISTCOMPARE implements Comparator<IntOrList> {

    @Override
    public int compare(IntOrList left, IntOrList right) {
        // both ints
        if (left.thisType == INTLIST.INT && right.thisType == INTLIST.INT) {
            if (left.value < right.value) {
                return -1;
            } else if (left.value > right.value) {
                return 1;
            } else {
                return 0;
            }
        }
        // both lists
        else if (left.thisType == INTLIST.LIST && right.thisType == INTLIST.LIST) {
            int maxSize = Integer.max(left.nested.size(), right.nested.size());
            for (int i = 0; i < maxSize; i++) {
                // reach end of left before right
                if (i == left.nested.size()) {
                    return -1;
                }
                // end of right before left
                else if (i == right.nested.size()) {
                    return 1;
                }
                // Compare the inputs!
                int back = compare(left.nested.get(i), right.nested.get(i));
                if (back != 0) {
                    return back;
                }
            }
            // if we get here then both lists are the same.
            return 0;
        }
        // one is an int and one is a list
        else {
            // find the one that is just an int and convert it
            IntOrList newleft, newright;
            if (left.thisType == INTLIST.LIST) {
                newleft = left;
            } else {
                IntOrList nest = new IntOrList(INTLIST.INT);
                nest.value = left.value;
                newleft = new IntOrList(INTLIST.LIST);
                newleft.nested.add(nest);
            }
            if (right.thisType == INTLIST.LIST) {
                newright = right;
            } else {
                IntOrList nest = new IntOrList(INTLIST.INT);
                nest.value = right.value;
                newright = new IntOrList(INTLIST.LIST);
                newright.nested.add(nest);
            }

            // now call us again with these new values
            return compare(newleft, newright);
        }
        // dead code land
    }

}