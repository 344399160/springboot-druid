package com.scistor.queryrouter.model;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * sql Select
 * 
 * @author luowenlei
 *
 */
public class GroupBy extends SqlSegment {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1938118460092726099L;

    /**
     * groupByList
     */
    @Getter
    @Setter
    private List<String> groupByList = Lists.newArrayList();


    /* (non-Javadoc)
     * @see com.baidu.rigel.biplatform.queryrouter.queryplugin.sql.model.SqlSegment#getSql()
     */
    @Override
    public String getSql() {
        if (groupByList.isEmpty()) {
            return "";
        }
        return this.getFormatSql(" group by " + this.generateGroupByCause());
    }

    public String generateGroupByCause() {
        if (groupByList.isEmpty()) {
            return "";
        }
        StringBuffer groupby = new StringBuffer();
//        for (SqlColumn colum : groupByList) {
//            if (ColumnType.JOIN == colum.getType()
//                    && !SqlColumnUtils.isFacttableColumn(colum)) {
//                // 如果为Join字段,join字段肯定需要hasalias
//                groupby.append(colum.getTableName() + SqlConstants.DOT
//                        + colum.getTableFieldName() + SqlConstants.COMMA);
//            } else {
//                // 如果为其他字段
//                if (this.isHasAlias()) {
//                    groupby.append(SqlConstants.SOURCE_TABLE_ALIAS_NAME + SqlConstants.DOT
//                            + colum.getFactTableFieldName() + SqlConstants.COMMA);
//                } else {
//                    groupby.append(colum.getFactTableFieldName() + SqlConstants.COMMA);
//                }
//            }
//        }
        return this.getFormatSql(groupby.toString().substring(0, groupby.toString().lastIndexOf(SqlConstants.COMMA))
                + SqlConstants.SPACE);
    }

    public GroupBy(boolean hasAlias) {
        super(hasAlias);
        // TODO Auto-generated constructor stub
    }
    
}
