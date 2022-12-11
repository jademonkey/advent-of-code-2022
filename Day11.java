import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Day11 {

    static final String EXAMPLE_INPUT_9 = "O:/Java/advent-of-code-2022/ExampleInputs/Day11";
    static final String ACTUAL_INPUT_9 = "O:/Java/advent-of-code-2022/Inputs/Day11";

    static ArrayList<Monkey> allMonkies = new ArrayList<Monkey>();
    static int supermodulo = 1;

    public static void main(String[] args) {
        BufferedReader br = null;

        /* Parse input */
        try {
            br = new BufferedReader(
                    new FileReader(ACTUAL_INPUT_9));

            int curMon = 0;
            while (true) {
                // monkey header or none
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                /* parse! */
                // read the monkies into a list
                Monkey toAdd = new Monkey();
                toAdd.num = curMon;

                // starting items
                line = br.readLine();
                line = line.replaceAll("  Starting items: ", "");
                line.replaceAll(" ", "");
                String[] splitted = line.split(",");
                for (String s : splitted) {
                    toAdd.items.add((long) Integer.parseInt(s.trim()));
                }

                // Operation
                line = br.readLine();
                line = line.replaceAll("  Operation: new = old ", "");
                splitted = line.split(" ");
                if (splitted[1].equals("old")) {
                    toAdd.operationAction = Operation.SQUARE;
                    toAdd.operationAmount = 0;
                } else {
                    if (splitted[0].equals("+")) {
                        toAdd.operationAction = Operation.ADD;
                    } else if (splitted[0].equals("*")) {
                        toAdd.operationAction = Operation.MULTIPLY;
                    } else {
                        throw new Exception("unknwon operation " + splitted[0]);
                    }
                    toAdd.operationAmount = Integer.parseInt(splitted[1]);
                }

                // Test
                line = br.readLine();
                line = line.replaceAll("  Test: divisible by ", "");
                toAdd.testAmount = Integer.parseInt(line);
                supermodulo *= toAdd.testAmount;

                // true target
                line = br.readLine();
                line = line.replaceAll("    If true: throw to monkey ", "");
                toAdd.trueTarget = Integer.parseInt(line);

                // false target
                line = br.readLine();
                line = line.replaceAll("    If false: throw to monkey ", "");
                toAdd.falseTarget = Integer.parseInt(line);

                // done
                allMonkies.add(toAdd);
                curMon++;

                // skip blank line
                line = br.readLine();
            }

            for (Monkey m : allMonkies) {
                m.PrintAll();
            }
            System.out.println("Super module " + supermodulo);
            System.out.println("----------------");

            // now monkeys inspect. Each round
            int rounds = 10000;
            for (int r = 0; r < rounds; r++) {
                // each monkey
                for (int i = 0; i < allMonkies.size(); i++) {
                    Monkey thisMon = allMonkies.get(i);
                    // each item they are holding
                    for (int item = 0; item < thisMon.items.size(); item++) {
                        thisMon.itemsInspected++;
                        // Inspect item - Worry level changes per oepration
                        long worryLevel = (long) thisMon.items.get(item);
                        switch (thisMon.operationAction) {
                            case ADD:
                                worryLevel += thisMon.operationAmount;
                                break;
                            case MULTIPLY:
                                worryLevel *= thisMon.operationAmount;
                                break;
                            case SQUARE:
                                worryLevel *= worryLevel;
                                break;
                        }

                        // After inspect worry level changes / 3 round down
                        // worryLevelD = Math.floor(worryLevelD / 3);

                        // Monkey test worry level
                        // Monkey throw item per worry level
                        worryLevel %= supermodulo;
                        if (worryLevel % thisMon.testAmount == 0) {
                            // true
                            allMonkies.get(thisMon.trueTarget).items.add((long) worryLevel);

                        } else {
                            // false
                            allMonkies.get(thisMon.falseTarget).items.add((long) worryLevel);
                        }
                    }

                    // NOW clear the list because we've handled each item
                    thisMon.items.clear();
                    allMonkies.remove(i);
                    allMonkies.add(i, thisMon);
                }
            }

            // Calculate monkey business
            Integer[] monkeyBusiness = new Integer[allMonkies.size()];
            for (Monkey m : allMonkies) {
                monkeyBusiness[m.num] = m.itemsInspected;
                m.Print();
                // System.out.println("Monkey " + m.num + " inspected " + m.itemsInspected);
            }
            Arrays.sort(monkeyBusiness);
            Collections.reverse(Arrays.asList(monkeyBusiness));
            System.out.println(Arrays.asList(monkeyBusiness));

            System.out.println((long) ((long) monkeyBusiness[0] * (long) monkeyBusiness[1]));

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

class Monkey {
    public int num;
    public ArrayList<Long> items = new ArrayList<Long>();
    public Operation operationAction;
    public int operationAmount;
    public int testAmount;
    public int trueTarget;
    public int falseTarget;
    public int itemsInspected = 0;

    public void PrintAll() {
        System.out.print("Monkey " + num + " Operation " + operationAction + " " + operationAmount
                + " test divide by " + testAmount + " true->" + trueTarget + " false->" + falseTarget + " holding:[");
        for (int i = 0; i < items.size(); i++) {
            if (i != 0) {
                System.out.print(",");
            }
            System.out.print(items.get(i));
        }
        System.out.println("]");
    }

    public void Print() {
        System.out.print("Monkey " + num + " holding:[");
        for (int i = 0; i < items.size(); i++) {
            if (i != 0) {
                System.out.print(",");
            }
            System.out.print(items.get(i));
        }
        System.out.println("] inspected=" + itemsInspected);
    }
}

enum Operation {
    ADD,
    MULTIPLY,
    SQUARE
}