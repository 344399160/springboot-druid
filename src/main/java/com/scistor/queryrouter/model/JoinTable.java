package com.scistor.queryrouter.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * sql Join
 * 
 * @author luowenlei
 *
 */
public class JoinTable extends SqlSegment {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -85610952681471460L;
    
    /**
     * tableName
     */
    @Getter
    @Setter
    private String tableName;
    
    /**
     * joinOnList
     */
    @Getter
    @Setter
    private List<JoinOn> joinOnList = new ArrayList<JoinOn>();

    /* (non-Javadoc)
     * @see com.baidu.rigel.biplatform.queryrouter.queryplugin.plugins.model.SqlSegment#getSql()
     */
    @Override
    public String getSql() {
        if (StringUtils.isEmpty(this.tableName)) {
            return "";
        }
        StringBuffer sql = new StringBuffer(" left outer join ");
        sql.append(this.tableName);
        sql.append(SqlConstants.SPACE);
        sql.append(this.tableName);
        sql.append(SqlConstants.SPACE);
        sql.append(SqlConstants.JOIN_ON);
        sql.append(SqlConstants.SQL_TRUE);
        if (joinOnList.isEmpty()) {
            return sql.toString();
        }
        for (JoinOn joinOn : joinOnList) {
            if (StringUtils.isEmpty(joinOn.getFacttableColumnName())) {
                continue;
            }
            sql.append(joinOn.getSql());
        }
        return this.getFormatSql(sql.toString());
    }


}
