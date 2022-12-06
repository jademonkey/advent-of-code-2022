import java.io.BufferedReader;
import java.io.FileReader;

public class Day6 {

    static final String EXAMPLE_INPUT_6 = "O:/Java/advent-of-code-2022/ExampleInputs/Day6";
    static final String ACTUAL_INPUT_6 = "O:/Java/advent-of-code-2022/Inputs/Day6";

    public static void main(String[] args) {
        BufferedReader br = null;
        final int packetMarkerLength = 4;
        int startOfPacketMarkerPointer = packetMarkerLength;

        final int messageMarkerLength = 14;
        int startOfMessageMarkerLength = messageMarkerLength;

        /* Parse input */
        try {
            br = new BufferedReader(
                    new FileReader(ACTUAL_INPUT_6));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    /* EOF */
                    break;
                }

                /* Find the start of packet marker */
                while (true) {
                    String tocheck = line.substring(startOfPacketMarkerPointer - packetMarkerLength,
                            startOfPacketMarkerPointer);
                    System.out.println(tocheck);
                    if (uniqueLetters(tocheck)) {
                        break;
                    }
                    // else
                    startOfPacketMarkerPointer++;
                }

                /* Find the start of message marker */
                while (true) {
                    String tocheck = line.substring(startOfMessageMarkerLength - messageMarkerLength,
                            startOfMessageMarkerLength);
                    System.out.println(tocheck);
                    if (uniqueLetters(tocheck)) {
                        break;
                    }
                    // else
                    startOfMessageMarkerLength++;
                }

                System.out.println("Start of Packet Marker at char " + startOfPacketMarkerPointer);
                System.out.println("Start of Message Marker at char " + startOfMessageMarkerLength);
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

    private static boolean uniqueLetters(String input) {
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            for (int i2 = i + 1; i2 < input.length(); i2++) {
                if (input.charAt(i2) == c) {
                    return false;
                }
            }
        }
        return true;
    }
}
