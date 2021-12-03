package LCP;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * LCP报文数据格式
 * @Author: LiuYi
 * @Date: 2021/9/13 19:16
 */
public class LCP implements Serializable {
    private String ID;
    private String Length;
    Data []data;

    public LCP() {
    }

    public LCP(String ID, String length, Data[] data) {
        this.ID = ID;
        Length = length;
        this.data = data;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLength() {
        return Length;
    }

    public void setLength(String length) {
        Length = length;
    }

    public Data[] getData() {
        return data;
    }

    public void setData(Data[] data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LCP lcp = (LCP) o;
        return Objects.equals(ID, lcp.ID) &&
                Objects.equals(Length, lcp.Length) &&
                Arrays.equals(data, lcp.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(ID, Length);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return "LCP数据报：{" +
                "ID='" + ID + '\'' +
                ", Length='" + Length + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
