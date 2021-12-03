import IPCP.IPCP;
import LCP.Data;
import LCP.LCP;
import PAP.PAP;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * 服务器端程序
 *
 * @Author: LiuYi
 * @Date: 2021/9/13 19:46
 */
public class PPPServer {
    private final static Logger logger = Logger.getLogger(PPPServer.class.getName());

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(10000);
        while (true) {
            Socket socket = server.accept();
            invoke(socket);
        }
    }

    private static void invoke(final Socket socket) throws IOException {
        new Thread(new Runnable() {
            // 状态
            String status = "Establish";
            // PPP数据报
            PPP ppp = null;

            // 输入 输出流
            ObjectInputStream is = null;
            ObjectOutputStream os = null;

            @Override
            public void run() {
                try {
                    Scanner sc = new Scanner(System.in);
                    is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                    os = new ObjectOutputStream(socket.getOutputStream());
                    while (true) {
                        switch (status) {
                            case "Establish":
                                int i = 0;
                                System.out.println("--------- LCP阶段 链路配置 ---------");
                                Object obj1 = is.readObject();
                                ppp = (PPP) obj1;

                                LCP lcp = lcp = (LCP) ppp.getData();
                                Data[] data = lcp.getData();

                                if (!"1500".equals(data[0].getData())) {
                                    i++;
                                } else if (!"C023".equals(data[1].getData())) {
                                    i++;
                                } else if (!"C025".equals(data[2].getData())) {
                                    i++;
                                }

                                if (i == 0) {
                                    ppp = new PPP();
                                    ppp.setData("Configure-Ack");
                                    // 协商通过继续执行caseAuthenticate
                                    System.out.println(ppp.toString());
                                    System.out.println("LCP配置协商通过！！！\n\n");
                                    status = "Authenticate";

                                    os.writeObject(ppp);
                                    os.flush();
                                } else if (i == 1) {
                                    ppp = new PPP();
                                    ppp.setData("Configure-Reject");
                                    System.out.println(ppp.toString());
                                    System.out.println("LCP配置协商未通过！！！\n\n");
                                    status = "Establish";

                                    os.writeObject(ppp);
                                    os.flush();
                                    break;
                                } else if (i == 2) {
                                    ppp = new PPP();
                                    ppp.setData("Configure-Nak");
                                    System.out.println(ppp.toString());
                                    System.out.println("LCP配置协商未通过！！！\n\n");
                                    status = "Establish";

                                    os.writeObject(ppp);
                                    os.flush();
                                    break;
                                }
                            case "Authenticate":
                                System.out.println("--------- PAP阶段 身份认证 ---------");
                                Object obj2 = is.readObject();
                                ppp = (PPP) obj2;

                                PAP pap = (PAP) ppp.getData();
                                if ("admin".equals(pap.getAccount()) && "12345".equals(pap.getPasswd())) {
                                    ppp = new PPP();
                                    ppp.setData("Authenticate-Ack");
                                    System.out.println(ppp.toString());
                                    System.out.println("PAP身份认证通过！！！\n\n");
                                    status = "NetWork";

                                    os.writeObject(ppp);
                                    os.flush();
                                    // 协商通过该继续执行caseNetWork
                                } else {
                                    ppp = new PPP();
                                    ppp.setData("Authenticate-Nak");
                                    System.out.println(ppp.toString());
                                    System.out.println("PAP身份验证未通过！！！\n" +
                                            "即将断开连接\n");
                                    status = "Terminate";

                                    os.writeObject(ppp);
                                    os.flush();
                                    break;
                                }


                            case "NetWork":
                                System.out.println("----- IPCP阶段 网络配置 -------");
                                Object obj3 = is.readObject();
                                ppp = (PPP) obj3;
                                IPCP ipcp = (IPCP) ppp.getData();

                                if ("Configure-Request".equals(ipcp.getReply())) {
                                    ipcp = new IPCP("Configure-Ack","127.0.0.1");
                                    status = "Talking";
                                    ppp = new PPP();
                                    ppp.setData(ipcp);
                                    System.out.println(ppp.toString());
                                    System.out.println("IPCP请求成功！！！\n\n");

                                    os.writeObject(ppp);
                                    os.flush();
                                    // 请求成功，继续执行caseTalking
                                } else {
                                    ipcp = new IPCP("Configure-Nak");
                                    status = "Terminate";
                                    ppp = new PPP();
                                    ppp.setData(ipcp);
                                    System.out.println(ppp.toString());
                                    System.out.println("IPCP请求失败！！！\n\n");

                                    os.writeObject(ppp);
                                    os.flush();
                                    break;
                                }

                                break;



                            case "Talking":
                                System.out.println("网络协议层配置成功，开始与客户端通话：");
                                String content;

                                while(true) {
                                    // 接收数据
                                    Object obj4 = is.readObject();
                                    if (obj4 != null) {
                                        ppp = (PPP) obj4;
                                    }
                                    System.out.println("他说：" + ppp.getData().toString());
                                    if ("bye".equals(ppp.getData().toString())) {
                                        status = "Terminate";
                                        break;
                                    }
                                    System.out.print("我说：");
                                    content = sc.nextLine();
                                    ppp = new PPP();
                                    ppp.setData(content);

                                    // 发送数据
                                    os.writeObject(ppp);
                                    os.flush();
                                    if ("bye".equals(content)) {
                                        status = "Terminate";
                                        break;
                                    }
                                }
                                break;

                            case "Terminate":
                                System.out.println("-----    与该客户端连接中断     -----");
                                try {
                                    is.close();
                                } catch (Exception ex) {
                                }
                                try {
                                    os.close();
                                } catch (Exception ex) {
                                }
                                try {
                                    socket.close();
                                } catch (Exception ex) {
                                }
                                return;
                            default:
                                break;
                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {

                }
            }
        }).start();
    }
}