package uk.ac.ic.wlgitbridge.util;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Winston on 19/11/14.
 */
public class Util {

    private static String SERVICE_NAME;
    private static String HOSTNAME;
    private static int PORT;
    private static String POSTBACK_URL;
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");

    public static String entries(int entries) {
        if (entries == 1) {
            return "entry";
        } else {
            return "entries";
        }
    }

    public static int booleanToInt(boolean b) {
        if (b) {
            return 1;
        } else {
            return 0;
        }
    }

    public static boolean intToBoolean(int i) {
        return i != 0;
    }

    private static String removeAllSuffix(String str, String suffix) {
        int lastIndexOfSuffix;
        String result = str;
        while ((lastIndexOfSuffix = result.lastIndexOf(suffix)) > -1) {
            result = result.substring(0, lastIndexOfSuffix);
        }
        return result;
    }

    /* removeAllSuffixes("something.git///", "/", ".git") => "something" */
    public static String removeAllSuffixes(String str, String... suffixes) {
        String result = str;
        for (String suffix : suffixes) {
            result = removeAllSuffix(result, suffix);
        }
        return result;
    }

    public static String getContentsOfReader(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String line; (line = reader.readLine()) != null; ) {
            sb.append(line);
        }
        return sb.toString();
    }

    public static void setServiceName(String serviceName) {
        SERVICE_NAME = serviceName;
    }

    public static String getServiceName() {
        return SERVICE_NAME;
    }

    public static int getPort() {
        return PORT;
    }

    public static void setPort(int port) {
        PORT = port;
    }

    public static void setPostbackURL(String postbackURL) {
        POSTBACK_URL = postbackURL;
    }

    public static String getPostbackURL() {
        return POSTBACK_URL;
    }

    private static void println(PrintStream ps, String ln) {
        ps.println(getStringBuilder().append(ln));
    }

    public static void sout(String ln) {
        println(System.out, ln);
    }

    public static void serr(String ln) {
        println(System.err, ln);
    }

    private static StringBuilder getStringBuilder() {
        StringBuilder sb = new StringBuilder("[");
        sb.append(dateFormat.format(new Date()));
        sb.append("] ");
        return sb;
    }

    public static void sout() {
        sout("");
    }

    public static void serr() {
        serr("");
    }

    public static void serr(Object obj) {
        serr(obj.toString());
    }

    public static void printStackTrace(Throwable t) {
        serr();
        t.printStackTrace();
    }

    public static void deleteDirectory(File directory) {
        if (directory != null) {
            deleteInDirectory(directory);
            directory.delete();
        }
    }

    public static void deleteInDirectory(File directory) {
        if (directory != null) {
            deleteInDirectoryApartFrom(directory);
        }
    }

    public static void deleteInDirectoryApartFrom(File directory, String... apartFrom) {
        if (directory != null) {
            Set<String> excluded = new HashSet<String>(Arrays.asList(apartFrom));
            for (File file : directory.listFiles()) {
                if (!excluded.contains(file.getName())) {
                    if (file.isDirectory()) {
                        deleteInDirectory(file);
                    }
                    file.delete();
                }
            }
        }
    }

    public static List<String> linesFromStream(InputStream stream, int skip, String trimSuffix) throws IOException {
        List<String> lines = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        for (int i = 0; i < skip; i++) {
            reader.readLine();
        }
        while ((line = reader.readLine()) != null) {
            String trim = line.trim();
            trim = trim.replaceAll("\\p{C}", "");
            int endIndex = trim.lastIndexOf(trimSuffix);
            if (endIndex >= 0) {
                trim = trim.substring(0, endIndex);
            }
            lines.add(trim);
        }
        return lines;
    }

    public static String fromStream(InputStream stream, int skip) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder out = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;
        for (int i = 0; i < skip; i++) {
            reader.readLine();
        }
        while ((line = reader.readLine()) != null) {
            out.append(line);
            out.append(newLine);
        }
        return out.toString();
    }

    public static String fromStream(InputStream stream) throws IOException {
        return fromStream(stream, 0);
    }

}
