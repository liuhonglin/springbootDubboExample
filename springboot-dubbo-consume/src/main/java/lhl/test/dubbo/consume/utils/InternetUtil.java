package lhl.test.dubbo.consume.utils;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.Charset;

/**
 * Created by HL.L on 2017/6/5.
 */
public class InternetUtil {

    public static String getIP(HttpServletRequest request)
    {
        String ip = request.getHeader("Cdn-Src-Ip");
        if ((ip != null) && (!"".equals(ip))) {
            return ip;
        }
        ip = request.getHeader("x-forwarded-for");
        if ((ip == null) || (ip.length() == 0)) {
            ip = request.getRemoteAddr();
        } else {
            String[] ips = ip.split(",");
            if (ips != null) {
                for (int i = 0; i < ips.length; i++) {
                    String temp = ips[i];
                    if ((!temp.startsWith("192.168.")) && (!temp.startsWith("10.")) && (!temp.startsWith("172."))) {
                        return temp;
                    }
                }

                return ips[0];
            }
        }
        return ip;
    }

    /**
     *
     * @param ip 用户ip
     * @param limit 限流大小
     * @return
     * @throws IOException
     */
    public static boolean acquire(String ip, String limit) throws IOException {
        StringBuffer luaScript = new StringBuffer();
        /*List<String> lines = Files.readAllLines(Paths.get("lua/limit.lua"), Charset.defaultCharset());
        for(String line : lines) {
            luaScript.append(line);
        }*/
        //new File(InternetUtil.class.getResource("/").getPath()+File.separator+"lua/limit.lua")

        BufferedInputStream bi = new BufferedInputStream(InternetUtil.class.getClassLoader().getResourceAsStream("lua/limit.lua"));
        BufferedReader br = new BufferedReader(new InputStreamReader(bi));
        String line = null;
        while ((line = br.readLine()) != null) {
            luaScript.append(line).append("\r");
        }
        br.close();
        bi.close();

        //String luaScript = Files.toString(new File(InternetUtil.class.getClassLoader().getResourceAsStream("lua/limit.lua"),
        //                                            Charset.defaultCharset()));

        Jedis jedis = new Jedis("127.0.0.1", 6379);
        // 此处将当前时间截取秒数
        String key = ip + ":" + System.currentTimeMillis() / 1000;
        return (Long)jedis.eval(luaScript.toString(), Lists.newArrayList(key),
                            Lists.newArrayList(limit)) == 1;
    }

}
