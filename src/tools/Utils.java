package tools;

import java.io.File;
import java.io.FileReader;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
    public static String getLastSubstring(String input, int index){
        return input.substring(input.length()-index);
    }
    public static String getFileAsString(String path){
        // Inspired from : https://stackoverflow.com/questions/320542/how-to-get-the-path-of-a-running-jar-file
        try {
            if(isRunningFromJar()){
                String absoluteRoot = URLDecoder.decode((new File(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(".")).getPath())).getAbsolutePath());
                Scanner scan= new Scanner(new FileReader(path.replace("./",absoluteRoot+"/")));
                StringBuilder output = new StringBuilder();
                while(scan.hasNext()){
                    output.append(scan.next()).append(" ");
                }
                return output.toString();
            } else {
                return Files.readString(Path.of(path));
            }
        } catch (Exception e){
            System.out.println("Error in getFileAsString. Running from Jar : "+isRunningFromJar()+". "+e);
            return "";
        }
    }

    public static boolean isRunningFromJar(){
        String protocol = Objects.requireNonNull(Utils.class.getResource("Utils.class")).getProtocol();
        return (protocol.equals("jar") || protocol.equals("rsrc")); // jar is the default protocol when running for jar file, rsrc is used by Eclipse-exported jar files, file is used otherwise (from IDE)
    }

    public static String anonymizeCardNumber(String cardNumber){
        return cardNumber.substring(0,4)+" xxxx xxxx "+getLastSubstring(cardNumber,4);
    }

    public static String anonymizePhoneNumber(String phoneNumber){
        return phoneNumber.substring(0,2)+" xx xx xx xx "+getLastSubstring(phoneNumber,2);
    }

    public static String capitalizeFirstLetter(String input){
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static String numberToString(Number number){
        return number+"";
    }

    public static String escapeSQLChars(String input){
        return input
            .replace("'","")
            .replace("\"","");
    }

    public static HashMap readJsonFile(String path){
        try {
            return new ObjectMapper().readValue(getFileAsString(path), HashMap.class);
        } catch(JsonProcessingException e){
            System.out.println("There have been an issue reading the following JSON file : "+path+"\n"+e);
            return new HashMap();
        }
    }

}
