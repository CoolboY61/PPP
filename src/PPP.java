import IPCP.IPCP;
import LCP.LCP;
import PAP.PAP;

import java.io.Serializable;
import java.util.Objects;

/**
 * PPP数据报格式定义
 * @Author: LiuYi
 * @Date: 2021/9/13 18:05
 */
public class PPP implements Serializable {
    private String Flag;
    private String Address;
    private String Control;
    private String Protocol;
    Object Data;
    private String FCS;

    public PPP() {
        Flag = "0x7E";
        Address = "0xFF";
        Control = "0x03";
        Protocol = "null";
        Data = new String();
        FCS = "0x7bc7";
    }

    public PPP(String protocol) {
        Flag = "0x7E";
        Address = "0xFF";
        Control = "0x03";
        Protocol = protocol;
        if ("0xc021".equals(protocol)) {
            Data = new LCP();
        } else if ("0xc023".equals(protocol)) {
            Data = new PAP();
        } else if ("0x8021".equals(protocol)) {
            Data = new IPCP();
        }
        FCS = "0x7bc7";
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getControl() {
        return Control;
    }

    public void setControl(String control) {
        Control = control;
    }

    public String getProtocol() {
        return Protocol;
    }

    public void setProtocol(String protocol) {
        Protocol = protocol;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }

    public String getFCS() {
        return FCS;
    }

    public void setFCS(String FCS) {
        this.FCS = FCS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PPP ppp = (PPP) o;
        return Objects.equals(Flag, ppp.Flag) &&
                Objects.equals(Address, ppp.Address) &&
                Objects.equals(Control, ppp.Control) &&
                Objects.equals(Protocol, ppp.Protocol) &&
                Objects.equals(Data, ppp.Data) &&
                Objects.equals(FCS, ppp.FCS);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Flag, Address, Control, Protocol, Data, FCS);
    }

    @Override
    public String toString() {
        return "PPP{" +
                "Flag='" + Flag + '\'' +
                ", Address='" + Address + '\'' +
                ", Control='" + Control + '\'' +
                ", Protocol='" + Protocol + '\'' +
                ", Data=" + Data +
                ", FCS='" + FCS + '\'' +
                '}';
    }
}
