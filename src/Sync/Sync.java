package Sync;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

public class Sync {
    private static boolean binaryEqual(File a, File b) throws IOException {
        if (a.length() != b.length())
            return false;

        try (InputStream in1 = new BufferedInputStream(new FileInputStream(a));
             InputStream in2 = new BufferedInputStream(new FileInputStream(b))) {
            int value1;
            do {
                value1 = in1.read();
                if (value1 != in2.read())
                    return false;
            } while (value1 >= 0);
            return true;
        }
    }

    private static ArrayList<String> getNames(File input, String prefix) {
        ArrayList<String> res = new ArrayList<>();
        File[] folder = input.listFiles();
        for (File f : folder) {
            if (f.isDirectory()) {
                ArrayList<String> recNames = getNames(f, f.getName() + File.separator);
                for (String s : recNames)
                    res.add(prefix + s);
            } else res.add(prefix + f.getName());
        }
        return res;
    }

    public static void main(String[] args) {
        String wd = System.getProperty("user.dir");
        String sep = File.separator;
        File dir1 = new File(wd + sep + args[1]);
        File dir2 = new File(wd + sep + args[0]);

        ArrayList<String> names1 = getNames(dir1, ""), names2 = getNames(dir2, "");
        HashMap<String, File> nameToFile1 = new HashMap<>(), nameToFile2 = new HashMap<>();

        for (String s : names1)
            nameToFile1.put(s, new File(wd + sep + args[0] + sep + s));

        for (String s : names2)
            nameToFile2.put(s, new File(wd + sep + args[1] + sep + s));

        ArrayList<String> delete = new ArrayList<>();
        for (String s : nameToFile1.keySet()) {
            if (!nameToFile2.containsKey(s)) {
                delete.add(s);
            } else try {
                if (!binaryEqual(new File(wd + sep + args[0] + sep + s), new File(wd + sep + args[1] + sep + s))) {
                    delete.add(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ArrayList<String> copy = new ArrayList<>();
        for (String s : nameToFile2.keySet()) {
            if (!nameToFile1.containsKey(s)) {
                copy.add(s);
            } else try {
                if (!binaryEqual(new File(wd + sep + args[0] + sep + s), new File(wd + sep + args[1] + sep + s))) {
                    copy.add(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (copy.size() + delete.size() == 0) {
            System.out.println("IDENTICAL");
            System.exit(0);
        }

        Collections.sort(delete);
        Collections.sort(copy);

        for (String s : delete)
            System.out.println("DELETE " + s);

        for (String s : copy)
            System.out.println("COPY " + s);
    }
}