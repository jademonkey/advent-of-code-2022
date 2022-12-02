import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Day2 {

    static final String EXAMPLE_INPUT_1 = "O:/Java/advent-of-code-2022/ExampleInputs/Day2";

    static final String ACTUAL_INPUT_1 = "O:/Java/advent-of-code-2022/Inputs/Day2";

    static final int ROCK_VALUE = 1; // A or X
    static final int PAPER_VALUE = 2; // B or Y
    static final int SCISSOR_VALUE = 3; // C or Z

    static final int LOSE_VALUE = 0;
    static final int DRAW_VALUE = 3;
    static final int WIN_VALUE = 6;

    public static void main(String[] args) {
        Part1Solution();
        Part2Solution();

    }

    public static void Part2Solution() {
        BufferedReader br = null;
        try {
            int total = 0;
            br = new BufferedReader(
                    new FileReader(ACTUAL_INPUT_1));

            while (true) {
                int them, desire, linescore;
                String line = br.readLine();
                if (line == null || line.length() == 0) {
                    /* EOF */
                    break;
                } else {
                    /* starts! */
                    String[] parts = line.split(" ");

                    if (parts[0].equals("A")) {
                        them = ROCK_VALUE;
                    } else if (parts[0].equals("B")) {
                        them = PAPER_VALUE;
                    } else if (parts[0].equals("C")) {
                        them = SCISSOR_VALUE;
                    } else {
                        throw new Exception("x not a b or c");
                    }

                    if (parts[1].equals("X")) {
                        desire = LOSE_VALUE;
                    } else if (parts[1].equals("Y")) {
                        desire = DRAW_VALUE;
                    } else if (parts[1].equals("Z")) {
                        desire = WIN_VALUE;
                    } else {
                        throw new Exception("x not a b or c");
                    }
                    linescore = desire + getPart2Result(desire, them);
                    total += linescore;
                }
            }

            System.out.println("Part 2 Total = " + total);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    public static void Part1Solution() {
        BufferedReader br = null;
        try {
            int total = 0;
            br = new BufferedReader(
                    new FileReader(ACTUAL_INPUT_1));

            while (true) {
                int them, me, linescore;
                String line = br.readLine();
                if (line == null || line.length() == 0) {
                    /* EOF */
                    break;
                } else {
                    /* starts! */
                    String[] parts = line.split(" ");

                    if (parts[0].equals("A")) {
                        them = ROCK_VALUE;
                    } else if (parts[0].equals("B")) {
                        them = PAPER_VALUE;
                    } else if (parts[0].equals("C")) {
                        them = SCISSOR_VALUE;
                    } else {
                        throw new Exception("x not a b or c");
                    }

                    if (parts[1].equals("X")) {
                        me = ROCK_VALUE;
                    } else if (parts[1].equals("Y")) {
                        me = PAPER_VALUE;
                    } else if (parts[1].equals("Z")) {
                        me = SCISSOR_VALUE;
                    } else {
                        throw new Exception("x not a b or c");
                    }
                    linescore = me + getPart1Result(me, them);
                    total += linescore;
                }
            }
            System.out.println("Part 1 total = " + total);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int getPart1Result(int me, int them) {
        if (me == them) {
            return DRAW_VALUE;
        }
        if (me == ROCK_VALUE) {
            if (them == SCISSOR_VALUE) {
                return WIN_VALUE;
            } else if (them == PAPER_VALUE) {
                return LOSE_VALUE;
            }
        } else if (me == SCISSOR_VALUE) {
            if (them == ROCK_VALUE) {
                return LOSE_VALUE;
            } else if (them == PAPER_VALUE) {
                return WIN_VALUE;
            }
        } else if (me == PAPER_VALUE) {
            if (them == ROCK_VALUE) {
                return WIN_VALUE;
            } else if (them == SCISSOR_VALUE) {
                return LOSE_VALUE;
            }
        }
        return -1;
    }

    public static int getPart2Result(int desire, int them) {
        if (desire == DRAW_VALUE) {
            return them;
        }
        if (them == ROCK_VALUE) {
            if (desire == LOSE_VALUE) {
                return SCISSOR_VALUE;
            } else if (desire == WIN_VALUE) {
                return PAPER_VALUE;
            }
        } else if (them == SCISSOR_VALUE) {
            if (desire == LOSE_VALUE) {
                return PAPER_VALUE;
            } else if (desire == WIN_VALUE) {
                return ROCK_VALUE;
            }
        } else if (them == PAPER_VALUE) {
            if (desire == LOSE_VALUE) {
                return ROCK_VALUE;
            } else if (desire == WIN_VALUE) {
                return SCISSOR_VALUE;
            }
        }
        return -1;
    }
}