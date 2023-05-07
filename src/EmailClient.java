/*
import java.io.*;
import java.net.Socket;
import java.util.Base64;
import java.util.HashMap;
import java.util.Hashtable;

public class EmailClient {



    private final HashMap<String,String> hashMap;

    public EmailClient(HashMap<String,String> hashMap){

        this.hashMap = hashMap;

    }

    public void sendEmails(){




    }




    public void sendEmail() throws IOException {
        System.out.println("进入sendEmail");



        int port = 25;

        //设置发件人的邮箱和授权码
        String from = hashMap.get("from");
        String password = hashMap.get("password");

        //获取所有的收件人地址
        String[] toEmails = EmailUtil.getEmaiAddress(hashMap.get("to"));



        for(String toEmail: toEmails ){

            //设置SMTP服务器地址和端口号
            String host = EmailUtil.getHost(toEmail);

            System.out.println(toEmail);
            System.out.println(host);

            //建立与SMTP服务器的连接
            Socket socket = new Socket(host, port);
            //获取连接的输入流和输出流
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
            //读取服务器的欢迎信息
            String welcomeMsg = in.readLine();
            System.out.println(welcomeMsg);
            //登录SMTP服务器
            out.write("EHLO " + host + "\r\n");
            out.flush();
            String response = in.readLine();
            while (response.startsWith("250-")) {
                response = in.readLine();
            }
            System.out.println(response);
            //登录邮箱
            out.write("AUTH LOGIN\r\n");
            out.flush();
            System.out.println(in.readLine());
            out.write(Base64.getEncoder().encodeToString(from.getBytes()) + "\r\n");
            out.flush();
            System.out.println(in.readLine());
            out.write(Base64.getEncoder().encodeToString(password.getBytes()) + "\r\n");
            out.flush();
            System.out.println(in.readLine());
            //设置邮件头信息
            out.write("MAIL FROM: <" + from + ">\r\n");
            out.flush();
            System.out.println(in.readLine());
            out.write("RCPT TO: <" + toEmail + ">\r\n");
            out.flush();
            System.out.println(in.readLine());
            out.write("DATA\r\n");
            out.flush();
            System.out.println(in.readLine());
            out.write("FROM: " + from + "\r\n");
            out.flush();
            out.write("TO: " + toEmail + "\r\n");
            out.flush();
            out.write("SUBJECT: " + hashMap.get("subject") + "\r\n");
            out.flush();
            out.write("\r\n");
            out.flush();
            //设置邮件内容
            out.write(hashMap.get("body") + "\r\n");
            out.flush();
            out.write(".\r\n");
            out.flush();
            System.out.println(in.readLine());
            //退出SMTP服务器
            out.write("QUIT\r\n");
            out.flush();
            System.out.println(in.readLine());
            //关闭连接
            socket.close();


        }






    }


}
*/



import javax.net.ssl.SSLSocketFactory;
import java.net.Socket;
import java.io.*;
import java.util.Base64;
import java.util.HashMap;

public class EmailClient {
    private final HashMap<String, String> hashMap;

    public EmailClient(HashMap<String, String> hashMap) {
        this.hashMap = hashMap;
    }


    public void sendEmail() throws IOException {
        System.out.println("进入sendEmail");

        String host = EmailUtil.getHost(hashMap.get("from"));
        System.out.println(host);

        int port = 465; // 注意端口号改为465
        String from = hashMap.get("from");
        String password = hashMap.get("password");
        String[] toEmails = EmailUtil.getEmaiAddress(hashMap.get("to"));

        for (String toEmail : toEmails) {


            System.out.println(toEmail);


            // 创建安全的SSL套接字
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            Socket socket = sslSocketFactory.createSocket(host, port);
            // 获取连接的输入流和输出流
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
            // 读取服务器的欢迎信息
            String welcomeMsg = in.readLine();
            System.out.println(welcomeMsg);
            // 登录SMTP服务器
            out.write("EHLO " + host + "\r\n");
            out.flush();
            String response = in.readLine();
            while (response.startsWith("250-")) {
                response = in.readLine();
            }
            System.out.println(response);
            // 登录邮箱
            out.write("AUTH LOGIN\r\n");
            out.flush();
            System.out.println(in.readLine());
            out.write(Base64.getEncoder().encodeToString(from.getBytes()) + "\r\n");
            out.flush();
            System.out.println(in.readLine());
            out.write(Base64.getEncoder().encodeToString(password.getBytes()) + "\r\n");
            out.flush();
            System.out.println(in.readLine());
            // 设置邮件头信息
            out.write("MAIL FROM: <" + from + ">\r\n");
            out.flush();
            System.out.println(in.readLine());
            out.write("RCPT TO: <" + toEmail + ">\r\n");
            out.flush();
            System.out.println(in.readLine());
            out.write("DATA\r\n");
            out.flush();
            System.out.println(in.readLine());
            out.write("FROM: " + from + "\r\n");
            out.flush();
            out.write("TO: " + toEmail + "\r\n");
            out.flush();
            out.write("SUBJECT: " + hashMap.get("subject") + "\r\n");
            out.flush();
            out.write("\r\n");
            out.flush();
            // 设置邮件内容
            out.write(hashMap.get("body") + "\r\n");
            out.flush();
            out.write(".\r\n");
            out.flush();
            System.out.println(in.readLine());
            // 退出SMTP服务器
            out.write("QUIT\r\n");
            out.flush();
            System.out.println(in.readLine());
            // 关闭连接
            socket.close();
        }
    }
}

