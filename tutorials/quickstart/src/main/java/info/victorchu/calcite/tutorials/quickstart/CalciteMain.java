package info.victorchu.calcite.tutorials.quickstart;

import info.victorchu.calcite.util.ResultSetFormatter;
import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.SchemaPlus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * calcite quickstart.
 * <a href="https://calcite.apache.org/docs/index.html"> calcite background 中的例子
 * </a>
 * 
 * @author victorchu
 */

public class CalciteMain {
    public static Logger logger = LoggerFactory.getLogger(CalciteMain.class);

    public static void main(String[] args) throws SQLException {
        // 创建连接
        Properties info = new Properties();
        info.setProperty("lex", "JAVA");
        Connection connection = DriverManager.getConnection("jdbc:calcite:", info);
        CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);
        // 注册schema
        final SchemaPlus rootSchema = calciteConnection.getRootSchema();
        rootSchema.add("hr", new ReflectiveSchema(new HrSchema()));

        // 创建语句
        Statement statement = calciteConnection.createStatement();
        // 执行语句
        ResultSet resultSet = statement.executeQuery("select d.deptno, min(e.empid) as min_empid \n"
                + "from hr.emps as e\n"
                + "join hr.depts as d\n"
                + "  on e.deptno = d.deptno\n"
                + "group by d.deptno\n"
                + "having count(*) > 1");
        logger.info(new ResultSetFormatter().resultSet(resultSet).string());
        resultSet.close();
        statement.close();
        connection.close();
    }
}
