package com.scistor.queryrouter.model;

import lombok.Getter;
import lombok.Setter;

/**
 * sql JoinOn
 * 
 * @author luowenlei
 *
 */
public class JoinOn extends SqlSegment {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6180677386102168725L;

    /**
     * joinTableName
     */
    @Getter
    @Setter
    private String joinTableName;

    /**
     * joinTableFieldName
     */
    @Getter
    @Setter
    private String joinTableFieldName;

    /**
     * facttableName
     */
    @Getter
    @Setter
    private String facttableName;
    
    /**
     * complexOperator
     */
    @Getter
    @Setter
    private String facttableColumnName;

    
    
    /* (non-Javadoc)
     * @see com.baidu.rigel.biplatform.queryrouter.queryplugin.sql.model.SqlSegment#getSql()
     */
    @Override
    public String getSql() {
        StringBuffer sql = new StringBuffer("");
        sql.append(SqlConstants.SPACE);
        sql.append(SqlConstants.AND);
        sql.append(SqlConstants.SPACE);
        sql.append(joinTableName);
        sql.append(SqlConstants.DOT);
        sql.append(joinTableFieldName);
        sql.append(SqlConstants.SPACE);
        sql.append(SqlConstants.EQUALS);
        sql.append(SqlConstants.SPACE);
        sql.append(facttableName);
        sql.append(SqlConstants.DOT);
        sql.append(facttableColumnName);
        sql.append(SqlConstants.SPACE);
        return this.getFormatSql(sql.toString());
    }

}
