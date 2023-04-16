package pengliu.me.backend.demo.util;

import java.util.UUID;

public class UUidUtils {
    public static String generateuuid(){
        return UUID.randomUUID().toString().replace("-","").substring(0,15);
    }
}

