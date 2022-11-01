package vn.com.irtech.core.common.page;

public class PageAble<T> extends PageDomain {
    private T data;

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}