package au.com.suttons.notification.resource.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ResponseCollectionBean implements APIResponse, Serializable
{
    private final static String PAGE_PARAM = "page=";

    private String href;
    private long count;
    private int page;
    private int limit;
    private String first;
    private String previous;
    private String next;
    private String last;
    private List<RootResourceBean> items;

    private transient int totalPage;

    public ResponseCollectionBean() {
        this.items = new ArrayList<RootResourceBean>();
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;

        this.first = this.getFirst();
        this.previous = this.getPrevious();
        this.next = this.getNext();
        this.last = this.getLast();
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getFirst() {
        if (this.href != null) {
            return this.hrefReplace(href, page, 1);
        }

        return null;
    }

    public String getPrevious() {
        if (this.href != null && page > 1) {
            return this.hrefReplace(href, page, page - 1);
        }
        return null;
    }

    public String getNext() {
        if (this.href != null && page < this.totalPage) {
            return this.hrefReplace(href, page, page + 1);
        }
        return null;
    }

    public String getLast() {
        if (this.href != null) {
            if(totalPage < 1){
                totalPage = 1;
            }
            return this.hrefReplace(href, page, totalPage);
        }
        return null;
    }

    /**
     * Getter for the {@code items} field.
     * <p/>
     * Note: This method returns a read-only version of {@code items} to prevent the {@code count} field becoming out of sync
     * with the number of items. {@code GSON} does not use getters to access field values, therefor the {@code count} value
     * must be set in the setter of the {@code items} field.
     *
     * @return {@code items}
     */
    public List<RootResourceBean> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(BaseResourceBean item) {
        if (item != null) {
            this.addItems(Arrays.asList(item));
        }
    }

    //TODO: remove this function when finish the pagination implementation for Customer and Vehicle
    public void addItems(List<? extends RootResourceBean> items) {
        if (items != null) {

            //Remove all null value item
            items.removeAll(Collections.singletonList(null));

            this.items.addAll(items);

            this.count = items.size();
        }

    }

    /**
     * Setter for the page information
     * <p/>
     * This method will set the count, current page number, limit and the collection content for the response.
     *
     * @param baseCollectionBean
     */
    public void setPageInfo(BaseCollectionBean baseCollectionBean) {
        if (baseCollectionBean != null) {
            this.count = baseCollectionBean.getCount();
            this.page = baseCollectionBean.getPage();
            this.limit = baseCollectionBean.getLimit();
            this.totalPage = baseCollectionBean.getTotalPage();
            List<BaseResourceBean> content = baseCollectionBean.getContent();

            if (content != null) {
                //Remove all null value item
                content.removeAll(Collections.singletonList(null));
                this.items.addAll(content);
            }
        }
    }

    private String hrefReplace(String href, int targetPage, int replacementPage) {

        if (href == null) {
            return null;
        }

        String targetWithOutPage = PAGE_PARAM;
        String targetWithPage = PAGE_PARAM + targetPage;
        String replacement = PAGE_PARAM + replacementPage;
        String target = null;

        //Find the target String to replace.
        if (href.lastIndexOf(targetWithPage) >= 0) {
            target = targetWithPage;
        } else if (href.lastIndexOf(targetWithOutPage) >= 0) {
            target = targetWithOutPage;
        }

        //If the href does not contain any Page Param String, then append the param at the end of url
        if(target == null){
            String q = "?";
            if (href.lastIndexOf(q) >= 0) {
                href = href + "&" + replacement;
            } else {
                href = href + q + replacement;
            }
        }else {
            //Replace the target with th replacement String
            href = href.replaceAll(target, replacement);
        }

        return href;
    }
}
