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
package com.scistor.queryrouter.server;


import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 
 * 处理Jdbc sql query请求
 * 
 * @author luowenlei
 *
 */
public interface JdbcHandler {

    /**
     * initJdbcTemplate
     *
     * @param dataSource
     */
    public void initJdbcTemplate(DataSource dataSource);
    
    /**
     * getDataSourceInfo
     *
     * @return
     */
    public DataSource getDataSource();
    
    /**
     * 通过sql查询数据库中的数据
     * 
     * @return List<Map<String, Object>> formd tableresult data
     */
    public List<Map<String, Object>> queryForList(String sql, List<Object> whereValues);

    /**
     * 通过sql查询数据库中的数据
     *
     * @return List<Map<String, Object>> formd tableresult data
     */
    public List<Map<String, Object>> queryForList(String sql);

    /**
     * 通过sql查询数据库中的源数据
     * 
     * @return List<Map<String, Object>> formd tableresult data
     */
    public Map<String, String> queryForMeta(String tableName);
    
    /**
     * queryForInt
     * 
     * @param sql
     *            sql
     * @param whereValues
     *            whereValues
     *            dataSourceInfo
     * @return int count
     */
    public int queryForInt(String sql, List<Object> whereValues);
    


}
