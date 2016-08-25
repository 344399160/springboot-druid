package com.scistor;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.ValidConnectionChecker;
import com.scistor.queryrouter.server.JdbcHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BiApplicationTests {
	@Resource(name = "jdbcHandlerImpl")
	private JdbcHandler jdbcHandler;
	@Test
	public void contextLoads() {
		DruidDataSource dataSource = new DruidDataSource();
//		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//		dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test");
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setUrl("jdbc:oracle:thin:@127.0.0.1:1521:orcl");
		dataSource.setUsername("root");//用户名
		dataSource.setPassword("root");//密码
		dataSource.setInitialSize(2);
		dataSource.setMaxActive(20);

		jdbcHandler.initJdbcTemplate(dataSource);
		List<Object> list = new ArrayList<>();
		list.add("2");
//		List<Map<String, Object>> queryList = jdbcHandler.queryForList("select * from testtable where id = ?", list);
		List<Map<String, Object>> queryList = jdbcHandler.queryForList("select * from testtable");
		queryList.forEach(item -> {
			item.forEach((param1, param2) -> {
				System.out.println(param1 + "  " + param2);
			});
		});
		Map<String, String> map = jdbcHandler.queryForMeta("testtable");
		map.forEach((param1, param2) -> {
			System.out.println(param1 + "  " + param2);
		});
	}

}
