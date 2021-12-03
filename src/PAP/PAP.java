package PAP;

import java.io.Serializable;
import java.util.Objects;

/**
 * PAP报文
 * @Author: LiuYi
 * @Date: 2021/9/13 20:02
 */
public class PAP implements Serializable {
    private String account;
    private String passwd;

    public PAP() {
    }

    public PAP(String account, String passwd) {
        this.account = account;
        this.passwd = passwd;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PAP pap = (PAP) o;
        return Objects.equals(account, pap.account) &&
                Objects.equals(passwd, pap.passwd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, passwd);
    }

    @Override
    public String toString() {
        return "PAP{" +
                "account='" + account + '\'' +
                ", passwd='" + passwd + '\'' +
                '}';
    }
}
