package com.example.jobMaintenance.component;

//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;

//@Component
public class ShedLockTableInitializer {
//    private final JdbcTemplate jdbcTemplate;
//
//    public ShedLockTableInitializer(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @EventListener(ContextRefreshedEvent.class)
//    public void createShedLockTable() {
//        String sql = """
//            CREATE TABLE IF NOT EXISTS shedlock (
//                name VARCHAR(64) NOT NULL,
//                lock_until TIMESTAMP NOT NULL,
//                locked_at TIMESTAMP NOT NULL,
//                locked_by VARCHAR(255) NOT NULL,
//                PRIMARY KEY (name)
//            )
//            """;
//
//        jdbcTemplate.execute(sql);
//    }
}
