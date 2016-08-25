/**
 * Copyright (c) 2014 Baidu, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 */
package com.scistor.queryrouter.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 分页信息
 * @author xiaoming.chen
 *
 */
public class PageInfo implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7767822498708728360L;
    
    /**
     * DEFAULT_PAGE_SIZE 默认每页记录数
     */
    public static final int DEFAULT_PAGE_SIZE = 500;
    
    /**
     * totalRecordCount 总记录数
     */
    @Getter
    @Setter
    private int totalRecordCount;
    
    /**
     * currentPage 当前第几页
     */
    @Getter
    @Setter
    private int currentPage;
    
    /**
     * totalPage 总页数
     */
    @Getter
    @Setter
    private int totalPage;
    
    /**
     * pageSize 每页的记录数
     */
    @Getter
    @Setter
    private int pageSize = PageInfo.DEFAULT_PAGE_SIZE;


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PageInfo [totalRecordCount=" + totalRecordCount + ", currentPage=" + currentPage + ", totalPage="
            + totalPage + ", pageSize=" + pageSize + "]";
    }
    
}
