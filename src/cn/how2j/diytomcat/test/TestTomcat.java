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
            System.err.println("请先启动 位于端口: " +port+ " 的diy tomcat，否则无法进行单元测试");
            System.exit(1);
        } else {
            System.out.println("检测到 diy tomcat已经启动，开始进行单元测试");
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
