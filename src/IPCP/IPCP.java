package IPCP;

import java.io.Serializable;
import java.util.Objects;

/**
 * IPCP报文
 * @Author: LiuYi
 * @Date: 2021/9/13 20:02
 */
public class IPCP implements Serializable {
    private String reply;
    private String IP;

    public IPCP(String reply) {
        this.reply = reply;
    }

    public IPCP(String reply, String IP) {
        this.reply = reply;
        this.IP = IP;
    }

    public IPCP() {
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IPCP ipcp = (IPCP) o;
        return Objects.equals(reply, ipcp.reply) &&
                Objects.equals(IP, ipcp.IP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reply, IP);
    }

    @Override
    public String toString() {
        return "IPCP{" +
                "reply='" + reply + '\'' +
                ", IP='" + IP + '\'' +
                '}';
    }
}
