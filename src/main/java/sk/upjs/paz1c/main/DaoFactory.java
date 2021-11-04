package sk.upjs.paz1c.main;


import org.springframework.jdbc.core.JdbcTemplate;

import com.mysql.cj.jdbc.MysqlDataSource;

public enum DaoFactory {
	
	INSTANCE;
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getjdJdbcTemplate() {
		if (jdbcTemplate == null) {
			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setUser("canteen");
			dataSource.setPassword("canteenApp2021");
			dataSource.setUrl("jdbc:mysql://localhost:3306/projekt_jedalen?"
					+ "serverTimezone=Europe/Bratislava");
			
			jdbcTemplate = new JdbcTemplate(dataSource);
		}
		return jdbcTemplate;
	}

}
