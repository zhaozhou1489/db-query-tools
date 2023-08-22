package com.marmot.db.tools.example.config;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @Author:zhaozhou
 * @Date: 2023/05/29
 * @Desc: todo
 */
@Slf4j
@Configuration
//basePackages 指定dao包  sqlSessionFactoryRef 要和SqlSessionFactory定义的Bean名称一样
@MapperScan(basePackages = { "com.marmot.db.tools.example.mapper" }, sqlSessionFactoryRef = "defaultSqlSessionFactory")
public class DefaultDataSourceConfig {

    //mapper扫描xml文件的路径
    static final String MAPPER_LOCATION = "classpath*:mapper/*.xml";


    public GlobalConfig globalConfig(){
        GlobalConfig globalConfig = new GlobalConfig();
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        globalConfig.setDbConfig(dbConfig);
        return globalConfig;
    }
    /**
     * 配置数据源
     */
    @Primary
    @Bean(name = "dataSourceDefault")
    //prefix的值要和yml文件中的一致
    @ConfigurationProperties(prefix = "spring.datasource.default")
    public DataSource dataSourceDefault(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "defaultTransactionManager")
    @Primary
    public PlatformTransactionManager dataSourceTransactionManager(@Qualifier("dataSourceDefault") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


    /**
     * 配置SqlSessionFactory
     */
    @Primary
    @Bean(name = "defaultSqlSessionFactory")
    public SqlSessionFactory defaultSqlSessionFactory(@Qualifier("dataSourceDefault") DataSource dataSource, @Qualifier("mybatisPlusInterceptor") MybatisPlusInterceptor mybatisPlusInterceptor) throws Exception{
        //final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        //用这个MybatisSqlSessionFactoryBean是因为我用的是mybatis普拉斯 用上面的会报错
        final MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setGlobalConfig(this.globalConfig());
        sqlSessionFactoryBean.setPlugins(mybatisPlusInterceptor);
        //这里注意getResources()方法，一定要加s因为有一个方法是getResource()
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        return sqlSessionFactoryBean.getObject();
    }

}
