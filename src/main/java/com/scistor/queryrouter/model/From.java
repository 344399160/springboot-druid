package com.scistor.queryrouter.model;

import lombok.Getter;
import lombok.Setter;

/**
 * sql From
 * 
 * @author luowenlei
 *
 */
public class From extends SqlSegment {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 8882672890101351785L;
    
    /**
     * tableName
     */
    @Getter
    @Setter
    private String tableName;


    public From(boolean hasAlias) {
        super(hasAlias);
    }

}
