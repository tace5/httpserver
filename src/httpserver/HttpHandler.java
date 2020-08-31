package httpserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 *
 * @author ruben.hume
 */

//The runnable that 
public class HttpHandler implements Runnable {
    private Socket clientSocket;
    private int fileLength;
    public HttpHandler(Socket clientSock) {
        clientSocket = clientSock;
    }
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream fileOut = clientSocket.getOutputStream();
            PrintWriter textOut = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            
            String msg = in.readLine(); // Reads message from client
            StringTokenizer tokenizer = new StringTokenizer(msg);
            String method = tokenizer.nextToken();
            
            if(method.equals("GET")) {
                String requestedFile = tokenizer.nextToken();
                if(requestedFile.equals("/")) {
                    requestedFile = "/index.html";
                }
                boolean fileExists = new File("root" + requestedFile).exists();
                if(fileExists) {
                    
                    // Send response header + textfile with utf-8 encoding
                    if(requestedFile.endsWith("html") || requestedFile.endsWith("htm")) {
                        textOut.print(new ResponseHeader("text/html", 200) + "\r\n" + readTextFile(requestedFile));
                        Gui.msgToConsole("INFO", clientSocket.getInetAddress() + "requested the following page: " + requestedFile);
                    }
                    if(requestedFile.endsWith("css")) {
                        textOut.print(new ResponseHeader("text/css", 200) + "\r\n" + readTextFile(requestedFile));
                    }
                    if(requestedFile.endsWith("js")) {
                        textOut.print(new ResponseHeader("application/javascript", 200) + "\r\n" + readTextFile(requestedFile));
                    }
                    
                    // Send response header + files
                    if(requestedFile.endsWith("jpg")) {
                        textOut.print(new ResponseHeader("image/jpeg", 200));
                        fileOut.write(readFile(requestedFile), 0, fileLength);
                    }
                    if(requestedFile.endsWith("png")) {
                        textOut.print(new ResponseHeader("image/png", 200));
                        fileOut.write(readFile(requestedFile), 0, fileLength);
                    }
                    if(requestedFile.endsWith("gif")) {
                        textOut.print(new ResponseHeader("image/gif", 200));
                        fileOut.write(readFile(requestedFile), 0, fileLength);
                    }
                    if(requestedFile.endsWith("ttf")) {
                        textOut.print(new ResponseHeader("application/octet-stream", 200));
                        fileOut.write(readFile(requestedFile), 0, fileLength);
                    }
                }
                else {
                    textOut.print(new ResponseHeader(null, 404));
                }
            }
            textOut.flush();
            textOut.close();
        } catch(IOException ex) {
            Gui.msgToConsole("ERROR", ex.toString());
        }
    }
    
    // Reads files with utf-8 encoding
    public String readTextFile(String file) throws FileNotFoundException, IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader("root" + file));
        StringBuilder fileContent = new StringBuilder("");
        String line;
        do {
            line = fileReader.readLine();
            if(line != null) {
                fileContent.append(line);
                fileContent.append(System.lineSeparator());
            }
        } while(line != null);
        fileReader.close();
        return fileContent.toString();
    }
    
    // Method that reads files without utf-8 encoding
    public byte[] readFile(String file) throws FileNotFoundException, IOException {
        // Creates an inputstream that reads the bytes of the specified file
        DataInputStream fileIn = new DataInputStream(new FileInputStream("root" + file)); 
        // Gets the size of the file so that the buffer is of appropriate size
        fileLength = (int) new File("root" + file).length(); 
        // Creates a buffer where the file is stored
        byte[] buffer = new byte[fileLength];
        // Copies the bytes from the inputstream to the buffer
        fileIn.readFully(buffer); 
        fileIn.close();
        return buffer;
    }
}
