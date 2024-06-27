/**
 * 
 */
package com.studio.config;

import java.util.Properties;

import javax.naming.NamingException;
import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author ezhilraja_k
 *
 */

@Configuration
//@ComponentScan(basePackages={"org.studio.service","org.studio.dao"})
@ComponentScan(basePackages={"com.studio.dao","com.studio.service"})
@EnableTransactionManagement
public class DatabaseConfig {

	
	@Bean
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/studio");
		dataSource.setUsername("mystudio");
		dataSource.setPassword("studio@123");
		return dataSource;
	}
	
	@Bean
	@Autowired
	public PlatformTransactionManager getTransactionManager(EntityManagerFactory emf) throws NamingException{
      JpaTransactionManager jpaTransaction = new JpaTransactionManager();
      jpaTransaction.setEntityManagerFactory(emf);
      return jpaTransaction;
	}
	
	@Bean
	 public LocalContainerEntityManagerFactoryBean getEMF() {
	 
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
	    emf.setDataSource(getDataSource());
	    emf.setPersistenceUnitName("spring-jpa-unit");
	    emf.setJpaVendorAdapter(getHibernateAdapter());
	    Properties jpaProperties = new Properties();
	    	jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
//	        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
	    	
	    	jpaProperties.put("hibernate.hbm2ddl.auto", "validate");
	        jpaProperties.put("hibernate.show_sql", "false");
	        jpaProperties.put("hibernate.format_sql","false");
	    emf.setJpaProperties(jpaProperties);
	   return emf;
	 }
	
	 @Bean
	 public JpaVendorAdapter getHibernateAdapter() {
		 return new HibernateJpaVendorAdapter();
	 }
	
}
