package com.smartcold.manage.cold.util;

import java.util.Random;
import java.util.UUID;

public class UUIDUtils {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static int getType(String uuid){
       return Integer.parseInt(uuid.substring(15,16));
    }


    public static String getRUUID(int type){
        int sum=0;
        Random r=   new Random();
        String uuid=   System.currentTimeMillis()+""+r.nextInt(10);
        for (char c : uuid.substring(10, 14).toCharArray()) {  sum+=   Integer.parseInt(c+"");  }
        uuid=uuid+""+sum%4+""+type;
       return uuid;
    }

    public static boolean verUUID(String uuid) {
        if( uuid!=null&&!uuid.isEmpty()&&uuid.length()==16&&System.currentTimeMillis()>=Long.parseLong(uuid.substring(0,13))){
            int sum=0;
            for (char c : uuid.substring(10, 14).toCharArray()) {  sum+=   Integer.parseInt(c+"");  }
            return sum%4==Integer.parseInt(uuid.substring(14,15));
        }
        return false;
    }

}
