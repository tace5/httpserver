package httpserver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author ruben.hume
 */

// Class for the http header the server sends to the client before sending actual content
public class ResponseHeader { 
    
    private final String contentType;
    private String code;
    
    public ResponseHeader(String contentType, int code) {
        // Checks the filename and gives the content-type header the appropriate value
        this.contentType = contentType;
        if(code == 200) {
            this.code = "200 OK\r\n";
        }
        if(code == 404) {
            this.code = "404 Not Found\r\n";
        }
    }
    
    // Puts all the variables into one string that can be sent to clients
    @Override
    public String toString() { 
        String header = "HTTP/1.1 ";
        header += code;
        header += "Server: A_Simple_Webserver";
        header += "Date: " + getServerTime() + "\r\n";
        // contentType can be null if the file can't be found
        if(contentType != null) {
            header += "Content-Type: " + contentType + "\r\n";
        }
        header += System.lineSeparator();
        return header;
    }
    
    // Gets the current date and time
    public static String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        // Puts date and time in the format required by http
        SimpleDateFormat dateFormat = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }
}