/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package au.com.suttons.notification.mapper;

import au.com.suttons.notification.resource.bean.RequestBean;

public abstract class BaseMapper {
    protected RequestBean requestBean;
    protected int depth;

    protected final Integer MAX_DEPTH = 2;

    public BaseMapper(RequestBean requestBean) {
        this(requestBean, 0);
    }

    public BaseMapper(RequestBean requestBean, int depth) {
        this.requestBean = requestBean;
        this.depth = depth + 1;
    }

    public RequestBean getRequestBean() {
        return requestBean;
    }

    public int getDepth() {
        return depth;
    }
    
    public boolean isValidLevel() {
        return (this.getDepth() < MAX_DEPTH);
    }
}