package edu.ucsd.cse110.cse110lab4part5;

import java.util.Random;

public class UserUUID {

    public static int generate_own_uid(){
        Random rand = new Random();
        int upperbound = 999999999;
        int random_uuid = rand.nextInt(upperbound);

        return random_uuid;
    }

    public static String uuid_toString(int uuid){
        int cur_digits;
        if (uuid == 0){
            cur_digits = 1;
        }
        else {
            cur_digits = ((int) Math.floor(Math.log10(uuid)) + 1);
        }
        int max_digits = 9;
        int digits_needed = max_digits - cur_digits;
        StringBuilder str_digits = new StringBuilder(Integer.toString(uuid));
        while (digits_needed > 0){
            str_digits.insert(0, "0");
            digits_needed -= 1;
        }
        return str_digits.toString();
    }

    public static int String_toUUID(String uuid_str){
        int upperbound = 999999999;
        try {
            return Integer.parseInt(uuid_str);
        } catch (NumberFormatException e) {
            return Math.abs(uuid_str.hashCode() % upperbound);
        }

    }
}
