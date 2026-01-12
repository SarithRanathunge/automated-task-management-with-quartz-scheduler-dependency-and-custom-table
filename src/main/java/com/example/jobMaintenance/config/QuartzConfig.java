package com.example.jobMaintenance.config;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.time.ZoneId;
import java.util.TimeZone;

@Configuration
public class QuartzConfig {

    private static final ZoneId ZONE = ZoneId.of("Asia/Colombo");
    private final HistoricalJobProperties jobProperties;

    public QuartzConfig(HistoricalJobProperties jobProperties) {
        this.jobProperties = jobProperties;
    }

    @Bean
    public JobDetailFactoryBean historicalMarketPricesJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(com.example.jobMaintenance.component.HistoricalMarketPricesJob.class);
        factoryBean.setDurability(true);
        factoryBean.setName("historicalMarketPricesJob");
        factoryBean.setDescription("Daily job to archive current market prices to historical");
        return factoryBean;
    }

    @Bean
    public CronTriggerFactoryBean historicalMarketPricesTrigger(JobDetail historicalMarketPricesJobDetail) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(historicalMarketPricesJobDetail);
        factoryBean.setName("dailyTrigger");

        // Build cron from the HH:mm property
        String hhmm = jobProperties.getScheduledTime(); // "22:00"
        String[] parts = hhmm.split(":");
        String cron = String.format("0 %s %s * * ?", parts[1], parts[0]); // seconds minutes hours ...

        factoryBean.setCronExpression(cron);
        factoryBean.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Colombo")));
        return factoryBean;
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        return new SpringBeanJobFactory(); // Enables autowiring in Quartz jobs
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(
            Trigger historicalMarketPricesTrigger,
            SpringBeanJobFactory springBeanJobFactory) {

        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        factory.setTriggers(historicalMarketPricesTrigger); // varargs method

        // This is the key: tell Quartz to use Spring's job factory for autowiring
        factory.setJobFactory(springBeanJobFactory);

        factory.setWaitForJobsToCompleteOnShutdown(true);

        return factory;
    }
}