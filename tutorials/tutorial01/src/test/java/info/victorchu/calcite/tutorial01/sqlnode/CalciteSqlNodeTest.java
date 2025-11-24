package info.victorchu.calcite.tutorial01.sqlnode;

import info.victorchu.calcite.util.SqlNodeTreePrintVisitor;
import lombok.SneakyThrows;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalciteSqlNodeTest {

    @SneakyThrows
    @Test
    public void sqlToNode01(){
        String sql = "select name from users";
        SqlParser parser = SqlParser.create(sql,SqlParser.Config.DEFAULT);
        SqlNode node = parser.parseQuery();
        SqlNodeTreePrintVisitor visitor = new SqlNodeTreePrintVisitor();
        node.accept(visitor);
    }

    @SneakyThrows
    @Test
    public void sqlToNode02(){
        String sql = "SELECT NOW() + INTERVAL (quantity * 2) HOUR FROM orders";
        SqlParser sqlParser = SqlParser.create(sql, SqlParser.Config.DEFAULT);
        SqlNode node = sqlParser.parseQuery();
        SqlNodeTreePrintVisitor visitor = new SqlNodeTreePrintVisitor();
        node.accept(visitor);
    }

    @SneakyThrows
    @Test
    public void sqlToNode03(){
        String sql = "SELECT NOW(), INTERVAL 2 DAY FROM orders";
        SqlParser sqlParser = SqlParser.create(sql, SqlParser.Config.DEFAULT);
        SqlNode node = sqlParser.parseQuery();
        SqlNodeTreePrintVisitor visitor = new SqlNodeTreePrintVisitor();
        node.accept(visitor);
    }
}
