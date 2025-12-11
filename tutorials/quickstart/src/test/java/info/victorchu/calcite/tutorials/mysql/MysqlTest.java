package info.victorchu.calcite.tutorials.mysql;

import info.victorchu.calcite.util.ResultSetFormatter;
import org.apache.calcite.adapter.jdbc.JdbcSchema;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaPlus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MysqlTest {
    public static Logger logger = LoggerFactory.getLogger(MysqlTest.class);
    private static CalciteConnection calciteConnection;

    @BeforeAll
    public static void setUp() throws SQLException {
        // 创建连接
        Properties info = new Properties();
        // lex 为 MYSQL 时，大小写敏感。默认是 Oracle，大小写不敏感。
        info.setProperty("lex", "MYSQL");
        Connection connection = DriverManager.getConnection("jdbc:calcite:", info);
        calciteConnection = connection.unwrap(CalciteConnection.class);

        // 注册schema
        final SchemaPlus rootSchema = calciteConnection.getRootSchema();
        Map<String, Object> operand = new HashMap<>();
        operand.put("jdbcDriver", "com.mysql.cj.jdbc.Driver");
        operand.put("jdbcUrl", "jdbc:mysql://localhost:3306/test?allowPublicKeyRetrieval=true");
        operand.put("jdbcUser", "calcite");
        operand.put("jdbcPassword", "apache#calcite");
        operand.put("jdbcSchema", "test");
        Schema schema = JdbcSchema.Factory.INSTANCE.create(rootSchema,"mysql",operand);
        rootSchema.add("mysql", schema);
    }

    @Test
    public void testQuery01() throws SQLException {
        // 创建语句
        Statement statement = calciteConnection.createStatement();
        // 执行语句
        ResultSet resultSet = statement.executeQuery("select * from mysql.customer limit 10");
        logger.info(new ResultSetFormatter().resultSet(resultSet).string());
        resultSet.close();
        statement.close();
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        calciteConnection.close();
    }
}
