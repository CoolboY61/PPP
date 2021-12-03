import IPCP.IPCP;
import LCP.Data;
import LCP.LCP;
import PAP.PAP;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * 服务端程序
 *
 * @Author: LiuYi
 * @Date: 2021/9/13 19:46
 */
public class PPPClient {
    private final static Logger logger = Logger.getLogger(PPPClient.class.getName());

    public static void main(String[] args) throws Exception {
        // 状态
        String status = "Establish";
        // PPP数据报
        PPP ppp = null;
        int i = 0;
        Socket socket = null;
        ObjectOutputStream os = null;
        ObjectInputStream is = null;

        Scanner sc = new Scanner(System.in);
        socket = new Socket("localhost", 10000);
        os = new ObjectOutputStream(socket.getOutputStream());
        is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        while (true) {
            switch (status) {
                case "Establish":
                    i++;
                    System.out.println("--------- LCP阶段 链路配置 ---------");
                    ppp = new PPP("0xc021");
                    LCP lcp = new LCP();
                    Data[] data = new Data[3];
                    data[0] = new Data("1", "4", "1500");
                    data[1] = new Data("1", "4", "C023");
                    data[2] = new Data("1", "4", "C025");
                    lcp.setID(Integer.toString(i));
                    lcp.setLength("26");
                    lcp.setData(data);
                    ppp.setData(lcp);

                    //             发送    LCP请求数据包
                    System.out.println("向服务器发送LCP配置请求........");
                    System.out.println(ppp.toString());
                    os.writeObject(ppp);
                    os.flush();

                    //             接收    LCP响应数据包
                    Object obj1 = is.readObject();
                    if (obj1 != null) {
                        ppp = (PPP) obj1;
                    }
                    System.out.println("服务器回复：" + ppp.getData().toString());
                    String reply1 = (String) ppp.getData();

                    if ("Configure-Ack".equals(reply1)) {
                        // 协商通过继续执行caseAuthenticate
                        System.out.println("链路建立成功！！！\n\n");
                        status = "Authenticate";
                    } else {
                        System.out.println("链路建立失败...\n\n");
                        status = "Establish";
                        break;
                    }


                case "Authenticate":
                    System.out.println("--------- PAP阶段 身份认证 ---------");
                    PAP pap = new PAP("admin", "12345");
                    ppp = new PPP("0xc023");
                    ppp.setData(pap);

                    //             发送    PAP请求数据包
                    System.out.println("向服务器发送PAP配置请求........");
                    System.out.println(ppp.toString());
                    os.writeObject(ppp);
                    os.flush();

                    //             接收    PAP响应数据包
                    Object obj2 = is.readObject();
                    if (obj2 != null) {
                        ppp = (PPP) obj2;
                    }
                    System.out.println("服务器回复：" + ppp.getData().toString());
                    String reply2 = (String) ppp.getData();

                    if ("Authenticate-Ack".equals(reply2)) {
                        // 协商通过继续执行caseNetWork
                        System.out.println("PAP用户信息协商通过！！！\n\n");
                        status = "NetWork";
                    } else {
                        System.out.println("用户信息协商失败...\n\n");
                        status = "Terminate";
                        break;
                    }


                case "NetWork":
                    System.out.println("----- IPCP阶段 网络配置 -------");
                    IPCP ipcp = new IPCP("Configure-Request");
                    ppp = new PPP();
                    ppp.setData(ipcp);

                    //             发送    IPCP请求数据包
                    System.out.println("向服务器发送IPCP配置请求........");
                    System.out.println(ppp.toString());
                    os.writeObject(ppp);
                    os.flush();

                    //             接收    IPCP响应数据包
                    Object obj3 = is.readObject();
                    if (obj3 != null) {
                        ppp = (PPP) obj3;
                    }
                    ipcp = (IPCP) ppp.getData();
                    System.out.println("服务器回复：" + ipcp.getReply());
                    String reply3 = ipcp.getReply();
                    if ("Configure-Ack".equals(reply3)) {
                        // 协商通过继续执行caseTalking
                        System.out.println("IPCP请求成功！！！");
                        System.out.println("服务器分配的IP地址：" + ipcp.getIP() + "\n\n") ;
                        status = "Taking";
                    } else {
                        System.out.println("IPCP请求失败...\n\n");
                        status = "Terminate";
                        break;
                    }



                case "Talking":
                    System.out.println("网络协议层配置成功，开始与服务器通话：");
                    String content;

                    while(true) {
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
                        // 接收数据
                        Object obj4 = is.readObject();
                        if (obj4 != null) {
                            ppp = (PPP) obj4;
                        }
                        System.out.println("她说：" + ppp.getData().toString());
                        if ("bye".equals(ppp.getData().toString())) {
                            status = "Terminate";
                            break;
                        }
                    }
                    break;

                case "Terminate":
                    i++;
                    System.out.println("---------    链路终止      ---------\n\n");
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
    }
}