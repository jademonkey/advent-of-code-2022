import java.io.BufferedReader;
import java.io.FileReader;

public class Day4 {

    static final String EXAMPLE_INPUT_1 = "O:/Java/advent-of-code-2022/ExampleInputs/Day4";

    static final String ACTUAL_INPUT_1 = "O:/Java/advent-of-code-2022/Inputs/Day4";

    public static void main(String[] args) {
        Part1Solution();
        Part2Solution();
    }

    private static void Part2Solution() {

    }

    private static void Part1Solution() {
        BufferedReader br = null;
        int conflicits = 0;
        int conflicits2 = 0;
        try {
            br = new BufferedReader(
                    new FileReader(ACTUAL_INPUT_1));

            while (true) {
                String line = br.readLine();
                String[] elves;
                String elf1, elf2;
                String[] elf1Sections, elf2Sections;
                int elf1Start, elf1End, elf2Start, elf2End;
                if (line == null || line.length() == 0) {
                    /* EOF */
                    break;
                }
                elves = line.split(",");
                elf1 = elves[0];
                elf2 = elves[1];

                /* Find start and end of each */
                elf1Sections = elf1.split("-");
                elf2Sections = elf2.split("-");
                elf1Start = Integer.parseInt(elf1Sections[0]);
                elf1End = Integer.parseInt(elf1Sections[1]);
                elf2Start = Integer.parseInt(elf2Sections[0]);
                elf2End = Integer.parseInt(elf2Sections[1]);

                // System.out.println("Elf1=" + elf1);
                // System.out.println("Elf2=" + elf2);

                /* now is one in the other? */
                if (elf2Start >= elf1Start && elf2End <= elf1End) {
                    conflicits++;
                    // System.out.println("Conflict! Elf2 is inside Elf1");
                } else if (elf1Start >= elf2Start && elf1End <= elf2End) {
                    conflicits++;
                    // System.out.println("Conflict! Elf1 is inside Elf2");
                }

                if (elf2Start >= elf1Start && elf2Start <= elf1End) {
                    conflicits2++;
                } else if (elf1Start >= elf2Start && elf1Start <= elf2End) {

                    conflicits2++;
                }

                // System.out.println("---");
            }
            System.out.println("Part1=" + conflicits);
            System.out.println("Part1=" + conflicits2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
