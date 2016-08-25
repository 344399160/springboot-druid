package com.scistor.queryrouter.model;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * sql Join
 * 
 * @author luowenlei
 *
 */
public class Join extends SqlSegment {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -85610952681471460L;
    
    /**
     * joinTables,多个表的join关联
     */
    private List<JoinTable> joinTables = new ArrayList<>();

    
    /* (non-Javadoc)
     * @see com.baidu.rigel.biplatform.queryrouter.queryplugin.plugins.model.SqlSegment#getSql()
     */
    @Override
    public String getSql() {
        if (CollectionUtils.isEmpty(joinTables)) {
            return "";
        }
        StringBuffer sql = new StringBuffer();
        joinTables.forEach(item -> sql.append(item.getSql()));
        return this.getFormatSql(sql.toString());
    }

    /**
     * default generate get joinTables
     * @return the joinTables
     */
    public List<JoinTable> getJoinTables() {
        return joinTables;
    }

    /**
     * default generate set joinTables
     * @param joinTables the joinTables to set
     */
    public void setJoinTables(List<JoinTable> joinTables) {
        this.joinTables = joinTables;
    }
}
