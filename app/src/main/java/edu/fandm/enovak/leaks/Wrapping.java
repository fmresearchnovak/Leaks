package edu.fandm.enovak.leaks;

import java.net.HttpURLConnection;
import java.io.IOException;
import java.io.BufferedReader;

public class Wrapping {

    public static int getResponseCode(HttpURLConnection connInstance) throws IOException{
        int code = connInstance.getResponseCode();
        System.out.println("<--- getResponseCode() code: " + code + " from " + connInstance.getURL() + " (" + connInstance.getRequestMethod() + ")");
        return code;
    }

    public static String readLine(BufferedReader readerInstance) throws IOException {
        String s = readerInstance.readLine();
        if(s == null) {
            System.out.println("<--- readline() null");
            return null;
        }
        System.out.println("<--- readline() " + s.length() + " characters");
        return s;
    }

}
