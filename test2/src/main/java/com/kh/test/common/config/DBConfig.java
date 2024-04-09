package com.kh.test.common.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


/*
 * @Configuration
 * - 설정용 클래스임을 명시 + 객체로 생성해서 내부 코드를 서버실행시 모두 수행
 * 
 * @PropertySource("경로")
 * - 지정된 경로의 properties 파일 내용을 읽어와서 사용
 * - 사용할 properties 파일이 다수일 경우 연속해서 작성하면 됨(Bean으로 등록함)
 * 
 * @Bean
 * - 개발자가 수동으로 생성한 객체의 관리를 Spring에게 넘기는 어노테이션
 * 
 * @ConfigurationProperties(prefix = "spring.datasource.hikari")
 * - @PropertySource로 읽어온 properties 파일의 내용 중 접두사(앞부분, prefix)가 
 *   일치하는 값만 읽어옴
 * - 읽어온 내용을 생성하려는 Bean에 자동으로 세팅
 * 
 * @Autowired 
 * - 등록된 Bean 중에서 타입이 일치하거나,
 *   상속 관계에 있는 Bean을 지정된 필드에 주입
 *   == 의존성 주입(DI, Dependency Injection)
 * 
 * DataSource : Connection 생성 + Connection Pool 지원 하는 객체를
 * 				참조하기 위한 Java 인터페이스
 * 				(DriverManager 대안, Java JNDI 기술 이용)
 * 
 * 
 * 
 */



@Configuration
@PropertySource("classpath:/config.properties")
public class DBConfig {
	
	// 필드 생성
	// import org.springframework.context.ApplicationContext;
	@Autowired  // DI, 의존성 주입
	private ApplicationContext applicationContext; //현재 프로젝트
	
	
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		// HikariConfig 설정 객체 생성
		
		/* -> config.properties 파일에서 읽어온 spring.datasource.hikari로 시작하는 
		 * 모든 값이 알맞은 필드에 세팅된 상태*/
		return new HikariConfig();
	}
	
	
	@Bean 
	public DataSource dataSource(HikariConfig config) {
		// 매개 변수 HikariConfig config
		// -> 등록된 Bean 중 HikariConfig 타입의 Bean이 자동으로 주입
		DataSource dataSource = new HikariDataSource(config);
		return dataSource;
	}
	
	
	// ------------------------------------ HikariCP 설정 -------------------------------------------
	
	@Bean
	public SqlSessionFactory sessionFactory(DataSource dataSource) throws Exception {
		
	
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		
		
		sessionFactoryBean.setDataSource(dataSource);
		
		
		// mapper.xml 파일이 모이는 경로를 지정
		// -> Mybatis 코드 수행시 mapper.xml을 읽을 수 있음
		sessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mappers/**.xml"));
		
		
		// 해당 패키지 내 모든 클래스의 별칭을 등록
		// -> Mybatis는 특정 클래스 지정 시 패키지명.클래스명을 모두 작성해야하므로 긴 이름을 짧게 부를 수 있게 별칭 설정 가능
		// ex) edu.kh.todo.model.dto.Todo (원본) -> Project(별칭으로 등록)
		sessionFactoryBean.setTypeAliasesPackage("com.kh.test");
		
		
		// mybatis 설정 파일 경로 지정
		sessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
		
		
		// 설정 내용이 모두 적용된 객체 하나 반환
		return sessionFactoryBean.getObject();
	}
	
	//sqlSessionTemplate : Connection + DBCP(DB Connection Pool) + Mybatis + 트랜잭션 제어 처리
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory factory) {
		
		return new SqlSessionTemplate(factory);
	}
	
	
	//DataSourceTransactionManager : 트랜잭션 매니저(제어처리)
	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
		
		return new DataSourceTransactionManager(dataSource);
	}
	

}
