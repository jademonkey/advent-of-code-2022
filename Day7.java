import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Day7 {

    static final String EXAMPLE_INPUT_7 = "C:/Users/101585866/Downloads/AOC/ExampleInputs/Day7";
    static final String ACTUAL_INPUT_7 = "C:/Users/101585866/Downloads/AOC/Inputs/Day7";

    static final int MINDIRSIZETOPRINT = 100000;
    static final int FILESYSTEMSIZE = 70000000;
    static final int UPDATESIZE = 30000000;

    static int part1solution;

    static int TARGET_DELETE_SIZE = 0;


    public static void main(String[] args) {
        BufferedReader br = null;

        /* Parse input */
        try {
            br = new BufferedReader(
                    new FileReader(ACTUAL_INPUT_7));
            while (true) {
                /* read the first line which is cd / to get to the root directory */
                String line = br.readLine();
                if (line == null) {
                    /* EOF */
                    break;
                }

                /* oh no it's the root dir! */
                AOCDir root = new AOCDir("/");
                root = buildDirContents(root, br);

                /* Now we have built up the contents... */
                System.out.println(part1solution);

                /* Now find a directory to delete */
                int freespace = FILESYSTEMSIZE - root.dirsize;
                System.out.println("free size=" + freespace);
                TARGET_DELETE_SIZE = UPDATESIZE - freespace;
                System.out.println("need to free size=" + TARGET_DELETE_SIZE);

                /* Find which folders meet the requirements */
                ArrayList<AOCDir> candidates = new ArrayList<AOCDir>();
                candidates = findCandidates(candidates, root);

                /* now find the smallest of those */
                int currentSmallest = FILESYSTEMSIZE;
                String smallestDir = "";
                for(AOCDir d : candidates) {
                    if(d.dirsize < currentSmallest) {
                        currentSmallest = d.dirsize;
                        smallestDir = d.dirName;
                    }
                }

                /* done */
                System.out.println("Smallest dir we could delete is " + smallestDir + " with size of " + currentSmallest);
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

    /* Recursively find the directories we could delete */
    public static ArrayList<AOCDir> findCandidates(ArrayList<AOCDir> cur, AOCDir curExamine){
        if(curExamine.dirsize >= TARGET_DELETE_SIZE) {
            cur.add(curExamine);
            for(AOCDir d : curExamine.dirs) {
                cur = findCandidates(cur, d);
            }
        } else {
            // no point going down anymore because if this directory size not big enough then it's children won't be
        }
        
        return cur;
    }

    /* Recursively build up  the directories contents */
    public static AOCDir buildDirContents(AOCDir thisone, BufferedReader br) throws Exception
    {
        while(true) {
            String line = br.readLine();
            if (line == null) {
                /* EOF */
                return thisone;
            }
            if(line.startsWith("$")) {
                // command
                if(line.equals("$ cd ..")) {
                    // leave recursive loop
                    return thisone;
                } else if(line.equals("$ ls")) {
                    // nothing, we're already doing this
                } else {
                    // time to go deeeeeeeper
                    String[] parts = line.split(" ");
                    AOCDir dir = new AOCDir(parts[2]);
                    // list it
                    dir = buildDirContents(dir, br);
                    thisone.dirs.add(dir);
                    thisone.dirsize += dir.dirsize;
                    /* for part 1... */
                    if(dir.dirsize < MINDIRSIZETOPRINT) {
                        part1solution += dir.dirsize;
                    }
                }
            } else {
                // file or dir listing
                if(line.startsWith("dir")) {
                    // how lovely, we'll ignore it until we cd into it
                } else {
                    String nam;
                    int siz;
                    String[] parts = line.split(" ");
                    nam = parts[1];
                    siz = Integer.parseInt(parts[0]);
                    AOCFile f = new AOCFile(nam, siz);
                    thisone.files.add(f);
                    thisone.dirsize += siz;
                }
            }
        }
    }
}

/* Because java doesn't have structs i have to make whole classes to store data */
class AOCDir 
{
    public String dirName;
    public int dirsize;
    public ArrayList<AOCFile> files;
    public ArrayList<AOCDir> dirs;

    public AOCDir(String dirName)
    {
        this.dirName = dirName;
        this.dirsize = 0;
        this.files = new ArrayList<AOCFile>();
        this.dirs = new ArrayList<AOCDir>();
    }
}

class AOCFile
{
    public String name;
    public int size;

    public AOCFile(String name, int size)
    {
        this.name = name;
        this.size = size;
    }
}