package com.portfolio.apple;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.util.List;
//출처: https://mangkyu.tistory.com/264 [MangKyu's Diary:티스토리]
public class ResetTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void afterTestMethod(final TestContext testContext) throws Exception {
        final JdbcTemplate jdbcTemplate = getJdbcTemplate(testContext);
        final List<String> truncateQueries = getTruncateQueries(jdbcTemplate);
        truncateTables(jdbcTemplate, truncateQueries);
        resetSequence(jdbcTemplate);
    }

    private void resetSequence(final JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("ALTER SEQUENCE hibernate_sequence RESTART WITH 1");
    }

    private JdbcTemplate getJdbcTemplate(final TestContext testContext) {
        return testContext.getApplicationContext().getBean(JdbcTemplate.class);
    }

    private List<String> getTruncateQueries(final JdbcTemplate jdbcTemplate) {
          // 테이블 이름을 가져온다. H2
        return jdbcTemplate.queryForList("SELECT Concat('TRUNCATE TABLE ', TABLE_NAME, ';') AS q FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'", String.class);
        // 테이블 이름을 가져온다. MaridDB in AWS
//        return jdbcTemplate.queryForList("SELECT Concat('TRUNCATE TABLE ', TABLE_NAME, ';') AS q FROM freelec_springboot2_webservice.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'", String.class);
    }

    private void truncateTables(final JdbcTemplate jdbcTemplate, final List<String> truncateQueries) {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");  //h2 db
//        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0"); //mysql
        truncateQueries.forEach(jdbcTemplate::execute);
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");   //h2 db
//        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1"); //mysql
    }
}
