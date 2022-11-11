package tools;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

public class Utils {
    public static String getLastSubstring(String input, int index){
        return input.substring(input.length()-index);
    }
    public static String getFileAsString(String path){
        try {
            FileInputStream file;
            file = new FileInputStream(path);
            String output = IOUtils.toString(file, StandardCharsets.UTF_8);
            file.close();
            return output;
        } catch (Exception e){
            System.out.println(e);
            return "";
        }
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

}
