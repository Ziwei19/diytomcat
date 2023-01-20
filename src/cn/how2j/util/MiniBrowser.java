package cn.how2j.util;
 
import java.io.*;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
 
public class MiniBrowser {

    public static void main(String[] args) throws MalformedURLException {
       String url = "http://static.how2j.cn/diytomcat.html";
//       String httpString = getHttpString(url, false);
//       System.out.println("result = " + httpString);
        String contentString = getContentString(url, false);
        System.out.println("contentString = " + contentString);
    }


    public static byte[] getContentBytes(String url) throws MalformedURLException {
        return getContentBytes(url, false);
    }

    public static String getContentString(String url) throws MalformedURLException {
        return getContentString(url,false);
    }

    public static String getContentString(String url, boolean gzip) throws MalformedURLException {
        byte[] contentBytes = getContentBytes(url, gzip);
        return new String(contentBytes).trim();
    }

    public static byte[] getContentBytes(String url, boolean gzip) throws MalformedURLException {
        byte[] response = getHttpBytes(url, gzip);
//        System.out.println("response = " + new String(response).trim());
        String doubleReturn = "\r\n\r\n";

        int pos = -1;

//        System.out.println("response length = " + response.length);
        for (int i = 0; i < response.length - doubleReturn.length(); i++) {
            byte[] temp = Arrays.copyOfRange(response, i, i + doubleReturn.length());
            boolean arrEq = Arrays.equals(temp, doubleReturn.getBytes());

            if (arrEq){
                pos = i;
                break;
            }
        }

        if (-1 == pos){
            return null;
        }

        pos += doubleReturn.length();
        byte[] res = Arrays.copyOfRange(response, pos, response.length);

        return res;
    }

    public static String getHttpString(String url, boolean gzip) throws MalformedURLException {
        byte[] httpBytes = getHttpBytes(url, gzip);
        return new String(httpBytes).trim();
    }

    public static byte[] getHttpBytes(String url, boolean gzip) throws MalformedURLException {

        URL requestUrl = new URL(url);

        int port = requestUrl.getPort();
        if (-1 == port){
            port = 80;
        }

        InetSocketAddress socketAddress = new InetSocketAddress(requestUrl.getHost(), port);
        Socket client = new Socket();

        try {
            client.connect(socketAddress, 1000);
            if (!client.isConnected()) {
                System.out.println("client isConnected: " + client.isConnected());
                return null;
            }

            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Host", requestUrl.getHost() + ":" + port);
            headerMap.put("Accept", "text/html");
            headerMap.put("Connection", "close");
            headerMap.put("User-Agent", "how2j mini browser /java1.8");

            if (gzip){
                headerMap.put("Accept-Encoding", "gzip");
            }

            String path = requestUrl.getPath();
            if (path.length() == 0){
                path = "/";
            }

            StringBuilder httpRequest = new StringBuilder();
            httpRequest.append("GET " + path + " HTTP/1.1 \r\n");

            Set<String> keySet = headerMap.keySet();
            for (String key : keySet){
                httpRequest.append(key + ":" + headerMap.get(key) + "\r\n");
            }

            // request
            PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
            pw.println(httpRequest.toString());

            // response
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            InputStream inputStream = client.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while (true) {
                int length = inputStream.read(buffer);
                if (-1 == length) {
                    break;
                }
                baos.write(buffer, 0, length);
                if (length != bufferSize) {
                    break;
                }

            }

            // End
            client.close();
            return baos.toByteArray();


        }catch (Exception e){
            e.printStackTrace();

            try {
                e.toString().getBytes("utf-8");
            }catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }



        return null;
    }

}