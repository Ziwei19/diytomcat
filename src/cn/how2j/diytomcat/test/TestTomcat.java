package cn.how2j.diytomcat.test;

import cn.how2j.util.MiniBrowser;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;

public class TestTomcat {

    private static int port = 18080;
    private static String ip = "127.0.0.1";

    @BeforeClass
    public static void beforeClass(){

        if (NetUtil.isUsableLocalPort(port)){
            System.err.println("Please boot up server: " +port+ " diy tomcat，Otherwise cannot carry out junit test");
            System.exit(1);
        } else {
            System.out.println("Diy tomcat has already been booted up, unit test starts...");
        }
    }


    public static String getContentString(String uri) throws MalformedURLException {
        String url = StrUtil.format("http://{}:{}{}", ip, port, uri);
        return MiniBrowser.getContentString(url);
    }

    @Test
    public void testHelloTomcat() throws MalformedURLException {
        String contentString = getContentString("/");
        Assert.assertEquals(contentString, "Hello DIY Tomcat from how2j.cn");
    }
}
