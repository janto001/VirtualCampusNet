/**
 * Filename: DataSource2Config.java
 *
 * © Copyright 2023 Quasarix. ALL RIGHTS RESERVED.

 * All rights, title and interest (including all intellectual property rights) in this software and any derivative works based upon or derived from
 * this software belongs exclusively to Quasarix.

 * Access to this software is forbidden to anyone except current employees of Quasarix and its affiliated companies who have executed non-disclosure
 * agreements explicitly covering such access. While in the employment of Quasarix or its affiliate companies as the case may be, employees may use
 * this software internally, solely in the course of employment, for the sole purpose of developing new functionalities, features, procedures,
 * routines, customizations or derivative works, or for the purpose of providing maintenance or support for the software. Save as expressly permitted
 * above, no license or right thereto is hereby granted to anyone, either directly, by implication or otherwise. On the termination of employment,
 * the license granted to employee to access the software shall terminate and the software should be returned to the employer, without retaining any
 * copies.

 * This software is (i) proprietary to Quasarix; (ii) is of significant value to it; (iii) contains trade secrets of Quasarix; (iv) is not publicly
 * available; and (v) constitutes the confidential information of Quasarix.

 * Any use, reproduction, modification, distribution, public performance or display of this software or through the use of this software without the
 * prior, express written consent of Quasarix is strictly prohibited and may be in violation of applicable laws.
 *
 */
package com.quasarix.virtual_campus.dao.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

/**
 * @author anto.jayaraj
 */
@Configuration
@EnableJpaRepositories( 
		  basePackages = "com.quasarix.virtual_campus.dao.ds2.repository", 
		  entityManagerFactoryRef = "ds2ManagerFactory",
		  transactionManagerRef = "ds2TransactionManager")
public class DataSource2Config {
	@Bean(name = "ds2DataSourceProperties")
	@ConfigurationProperties(prefix = "spring.datasource.ds2")
	public DataSourceProperties ruleDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "ds2DataSource")
	public DataSource secondaryDataSource(@Qualifier("ds2DataSourceProperties")
	DataSourceProperties dataSourceProperties) {
		return dataSourceProperties.initializeDataSourceBuilder().build();
	}

	@Bean(name = "ds2EntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("ds2DataSource")
	DataSource dataSource) {
		Map<String, Object> properties = new HashMap<>();
		return builder.dataSource(dataSource)
				.packages("com.quasarix.virtual_campus.dao.ds2.model")
				.persistenceUnit("ds2")
				.properties(properties)
				.build();
	}

	@Bean(name = "ds2TransactionManager")
	public PlatformTransactionManager transactionManager(@Qualifier("ds2EntityManagerFactory")
	EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}

