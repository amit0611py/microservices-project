package com.personal.report.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
public class AppConfig {
	
	@Bean(name = "reportProperties")
	@ConfigurationProperties("spring.report.datasource")
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}
	
	
	@Bean(name = "reportDataSource")
	@ConfigurationProperties("spring.report.hikari")
	public DataSource dataSorce(@Qualifier("reportProperties") DataSourceProperties properties) {
		return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}
	
	@Bean("report")
	public JdbcTemplate jdbcTemplate(@Qualifier("reportDataSource") DataSource ccbsDataSource) {
		return new JdbcTemplate(ccbsDataSource);
	}
	
	@Bean("reportTx")
	public PlatformTransactionManager platformTransactionManager(@Qualifier("reportDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
	
	

}
