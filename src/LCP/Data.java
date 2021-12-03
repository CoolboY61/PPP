package LCP;

import java.io.Serializable;
import java.util.Objects;

/**
 * LCP报文数据区
 * @Author: LiuYi
 * @Date: 2021/9/13 19:56
 */
public class Data implements Serializable {
    private String Type;
    private String Length;
    private String Data;

    public Data() {
    }

    public Data(String type, String length, String data) {
        Type = type;
        Length = length;
        Data = data;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getLength() {
        return Length;
    }

    public void setLength(String length) {
        Length = length;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return Objects.equals(Type, data.Type) &&
                Objects.equals(Length, data.Length) &&
                Objects.equals(Data, data.Data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Type, Length, Data);
    }

    @Override
    public String toString() {
        return "Data{" +
                "Type='" + Type + '\'' +
                ", Length='" + Length + '\'' +
                ", Data='" + Data + '\'' +
                '}';
    }
}
