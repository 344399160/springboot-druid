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
package com.scistor.queryrouter.server.impl;

import com.google.common.collect.Maps;
import com.scistor.queryrouter.server.JdbcHandler;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * 
 * 处理Jdbc sql query请求
 * 
 * @author luowenlei
 *
 */
@Service("jdbcHandlerImpl")
@Scope("prototype")
public class JdbcHandlerImpl implements JdbcHandler {
    
    /**
     * PROPERY_MAX_RESULT_SIZE
     */
    private static final String PROPERY_MAX_RESULT_SIZE = "queryrouter.result.memory.max.size";
    
    /**
     * PROPERY_SERVER_FILE_NAME
     */
    private static final String PROPERY_SERVER_FILE_NAME = "application";
    
    /**
     * meroryMaxSize,内存存放的最大的记录条数，默认50万
     */
    private static int memoryMaxSize = 500000;
    
    /**
     * Logger
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * jdbcTemplate
     */
    private JdbcTemplate jdbcTemplate = null;
    

    /**
     * dataSourceInfo
     */
    @Getter
    @Setter
    private DataSource dataSource;
    
    /**
     * 
     * initJdbcTemplate
     * 
     * @param dataSource
     *            dataSource
     */
    @Override
    public synchronized void initJdbcTemplate(DataSource dataSource) {
        long begin = System.currentTimeMillis();
        try {
            if (this.getJdbcTemplate() == null
                    || !this.getJdbcTemplate().getDataSource().equals(dataSource)) {
                this.setJdbcTemplate(new JdbcTemplate(dataSource));
                logger.info("queryId:{} initJdbcTemplate cost:"
                        + (System.currentTimeMillis() - begin) + "ms",
                        1);
            }
//            String maxSize = PropertiesFileUtils.getPropertiesKey(PROPERY_SERVER_FILE_NAME,
//                    PROPERY_MAX_RESULT_SIZE);
//            if (!StringUtils.isEmpty(maxSize)) {
//                memoryMaxSize = Integer.valueOf(maxSize).intValue();
//            }
            this.dataSource = dataSource;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("getDataSource error:" + e.getCause().getMessage());
        }
    }
    
    /**
     * 通过sql查询数据库中的数据
     * 
     * @return List<Map<String, Object>> formd tableresult data
     */
    public List<Map<String, Object>> queryForList(String sql, List<Object> whereValues) {
        long begin = System.currentTimeMillis();
        List<Map<String, Object>> result = null;
        try {
            logger.info("queryId:{} sql: {}", 1,
                    this.toPrintString(sql, whereValues));
            if (null == whereValues) {
                result = jdbcTemplate.queryForList(sql);
            } else {
                result = this.jdbcTemplate.queryForList(sql, whereValues.toArray());
            }
        } catch (Exception e) {
            logger.error("queryId:{} select sql error:{}", 1, e
                    .getCause().getMessage());
            throw e;
        } finally {
            logger.info("queryId:{} select sql cost:{} ms resultsize:{}",
                    1, System.currentTimeMillis() - begin,
                    result == null ? null : result.size());
        }
        return result;
    }
    /**
     * 通过sql查询数据库中的数据
     *
     * @return List<Map<String, Object>> formd tableresult data
     */
    public List<Map<String, Object>> queryForList(String sql) {
        long begin = System.currentTimeMillis();
        List<Map<String, Object>> result = null;
        try {
            result = jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            logger.error("queryId:{} select sql error:{}", 1, e
                    .getCause().getMessage());
            throw e;
        } finally {
            logger.info("queryId:{} select sql cost:{} ms resultsize:{}",
                    1, System.currentTimeMillis() - begin,
                    result == null ? null : result.size());
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * queryrouter.queryplugin.service.JdbcHandler
     * #queryForMeta(java.lang.String, java.util.List)
     */
    @Override
    public Map<String, String> queryForMeta(String tableName) {
        long begin = System.currentTimeMillis();
        Map<String, String> result = Maps.newConcurrentMap();
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = this.getJdbcTemplate().getDataSource().getConnection();
            DatabaseMetaData dbMetaData = conn.getMetaData();
            if (StringUtils.isNotEmpty(tableName)) {
                pst = conn.prepareStatement(String.format("select * from %s where 1=2", tableName));
                ResultSetMetaData rsd = pst.executeQuery().getMetaData();
                for(int i = 0; i < rsd.getColumnCount(); i++) {
                    result.put(rsd.getColumnName(i + 1), rsd.getColumnTypeName(i + 1));
                }
            }
        } catch (SQLException e1) {
            logger.error("queryId:{} select meta error:{}", 1, e1
                    .getCause().getMessage());
        } finally {
            JdbcUtils.closeConnection(conn);
            JdbcUtils.closeStatement(pst);
            logger.info("queryId:{} select meta cost:{} ms resultsize:{}",
                    1, System.currentTimeMillis() - begin, result.size());
        }
        return result;
    }
    
    /**
     * queryForInt
     * 
     * @param sql
     *            sql
     * @param whereValues
     *            whereValues
     * @return int count
     */
    public int queryForInt(String sql, List<Object> whereValues) {
        long begin = System.currentTimeMillis();
        Map<String, Object> result = null;
        int count = 0;
        try {
            logger.info("queryId:{} count sql: {}", 1,
                    this.toPrintString(sql, whereValues));
            result = this.jdbcTemplate.queryForMap(sql, whereValues.toArray());
            count = Integer.valueOf(result.values().toArray()[0].toString()).intValue();
        } catch (Exception e) {
            logger.error("queryId:{} select sql error:{}", 1, e
                    .getCause().getMessage());
            throw e;
        } finally {
            logger.info("queryId:{} select count sql cost:{} ms, result: {}",
                    1, System.currentTimeMillis() - begin, count);
        }
        return count;
    }
    
    /**
     * querySqlList
     *
     * @param sqlQuery
     * @param groupByList
     * @param dataSourceInfo
     * @return
     */
 /*   public SearchIndexResultSet querySqlList(SqlQuery sqlQuery, List<SqlColumn> groupByList) {
        // 此方法目前只能使用 preparesql = false
        sqlQuery.getWhere().setGeneratePrepareSql(false);
        long begin = System.currentTimeMillis();
        List<String> selectListOrder = Lists.newArrayList();
        for (SqlColumn sqlColumn : sqlQuery.getSelect().getSelectList()) {
            selectListOrder.add(sqlColumn.getName());
        }
        List<String> groupByListStr = Lists.newArrayList();
        for (SqlColumn sqlColumn : groupByList) {
            groupByListStr.add(sqlColumn.getName());
        }
        Meta meta = new Meta(selectListOrder.toArray(new String[0]));
        SearchIndexResultSet resultSet = new SearchIndexResultSet(meta, 1000000);
        
        final List<String> selectListOrderf = Lists.newArrayList(selectListOrder);
        jdbcTemplate.query(new PreparedStatementCreator() {
            
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                String sql = sqlQuery.toSql();
                logger.info("queryId:{} sql: {}", 1, sql);
                PreparedStatement pstmt = con.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                if (con.getMetaData().getDriverName().toLowerCase().contains("mysql")) {
                    pstmt.setFetchSize(Integer.MIN_VALUE);
                }
                return pstmt;
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                List<Object> fieldValues = new ArrayList<Object>();
                String groupBy = "";
                for (String select : selectListOrderf) {
                    fieldValues.add(rs.getObject(select));
                    if (groupByListStr != null && groupByListStr.contains(select)) {
                        groupBy += rs.getString(select) + ",";
                    }
                }
                
                SearchIndexResultRecord record = new SearchIndexResultRecord(fieldValues
                        .toArray(new Serializable[0]), groupBy);
                resultSet.addRecord(record);
            }
        });
        logger.info("queryId:{} select sql cost:{} ms resultsize:{}",
                1, System.currentTimeMillis() - begin,
                resultSet == null ? null : resultSet.size());
        ;
        return resultSet;
    }*/
    

    /**
     * convertListToString
     *
     * @param list
     * @return
     */
    private String convertListToString(List<Integer> list) {
        String r = "";
        for (Object o : list) {
            r = r + o.toString() + ",";
        }
        return  "[" + r + "]";
    }
    /**
     * toPrintString
     * 
     * @param sql
     *            sql
     * @param objects
     *            objects
     * @return sql String
     */
    public String toPrintString(String sql, List<Object> objects) {
        if (CollectionUtils.isEmpty(objects)) {
            return sql;
        }
        String printSql = new String(sql);
        int valuesCount = 0;
        if (!StringUtils.isEmpty(printSql)) {
            for (Object value : objects) {
                valuesCount++;
                if (value instanceof String) {
                    printSql = StringUtils.replaceOnce(printSql, "?", "'" + value.toString() + "'");
                } else {
                    printSql = StringUtils.replaceOnce(printSql, "?", value.toString());
                }
                if (valuesCount > 2000) {
                    return printSql;
                }
            }
            return printSql;
        } else {
            return "";
        }
    }
    

    /**
     * default generate get jdbcTemplate
     * 
     * @return the jdbcTemplate
     */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
    
    /**
     * default generate set jdbcTemplate
     * 
     * @param jdbcTemplate
     *            the jdbcTemplate to set
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
