package au.com.suttons.notification.resource.bean;

import java.util.List;

import au.com.suttons.notification.resource.page.Page;

public class BaseCollectionBean<T> {

    private int page;

    private int limit;

    private long count;

    private int totalPage;

    private List<T> content;

    public BaseCollectionBean(Page<T> page){
        this.page = page.getNumber() + 1;
        this.limit =page.getSize();
        this.count = page.getTotalElements();
        this.totalPage = page.getTotalPages();
    }

    public BaseCollectionBean(List<T> list){
        this.page = 1;
        this.limit =list.size();
        this.count = list.size();
        this.totalPage = 1;
        this.setContent(list);
    }

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    public long getCount() {
        return count;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getTotalPage() {
        return totalPage;
    }

}
