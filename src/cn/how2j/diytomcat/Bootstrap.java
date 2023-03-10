package cn.how2j.diytomcat;

import cn.hutool.core.util.NetUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Bootstrap {
    public static void mainT(String[] args) {
        try{
            int port = 18080;
            if (!NetUtil.isUsableLocalPort(port)){
                System.out.println("port + \" has been used，please use the method on this website to check and close ports：\\r\\nhttps://how2j.cn/k/tomcat/tomcat-portfix/545.html\" = " + port + " 端口已经被占用了，排查并关闭本端口的办法请用：\r\nhttps://how2j.cn/k/tomcat/tomcat-portfix/545.html");
                return;
            }

            ServerSocket ss = new ServerSocket(port);

            while (true){
                Socket s = ss.accept();
                InputStream is = s.getInputStream();
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                is.read(buffer);

                String requestString = new String(buffer, "utf-8");
                System.out.println("web browser input： \r\n" + requestString);


                OutputStream os = s.getOutputStream();
                String response_head = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n\r\n";
                String responseString = "Hello DIY Tomcat from how2j.cn";
                String res = response_head + responseString;
                os.write(res.getBytes());
                os.flush();
                os.close();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}

