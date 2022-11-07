package tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {
    public static String getLastSubstring(String input, int index){
        return input.substring(input.length()-index);
    }
    public static String getFileAsString(String path){
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e){
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

}
