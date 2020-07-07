package cn.cnyaoshun.form.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

@Data
@AllArgsConstructor
public class DynamicTemplate {
    private JdbcTemplate jdbcTemplate;
    private TransactionTemplate transactionTemplate;
}
