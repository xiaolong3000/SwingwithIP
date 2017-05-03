package isReach;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by Administrator on 2017/5/3 0003.
 */
public class isReach {
    public static boolean getReach(String ip){
        boolean state=false;
        try {
            InetAddress address = InetAddress.getByName(ip);
            state= address.isReachable(500);
        }catch (IOException e){
            e.printStackTrace();
        }
   return state;
    }
}
