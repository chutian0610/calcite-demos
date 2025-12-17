package info.victorchu.calcite.tutorial01.node2sql;

import info.victorchu.calcite.util.SqlNodeTreePrintVisitor;
import lombok.SneakyThrows;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.dialect.MysqlSqlDialect;
import org.apache.calcite.sql.parser.SqlParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalciteNode2SqlTest {
    @SneakyThrows
    @Test
    public void sqlToNode01(){
        String sql = "select name from users";
        SqlParser parser = SqlParser.create(sql,SqlParser.Config.DEFAULT);
        SqlNode node = parser.parseQuery();
        String formatSql = node.toSqlString(MysqlSqlDialect.DEFAULT).getSql();
        Assertions.assertEquals("SELECT `NAME`\n" +
                "FROM `USERS`",formatSql);
    }
    @SneakyThrows
    @Test
    public void sqlToNode02(){
        String sql = "select c1+c2+c3, count(1) from users";
        SqlParser parser = SqlParser.create(sql,SqlParser.Config.DEFAULT);
        SqlNode node = parser.parseQuery();
        SqlNodeTreePrintVisitor visitor = new SqlNodeTreePrintVisitor();
        node.accept(visitor);
        String formatSql = node.toSqlString(MysqlSqlDialect.DEFAULT).getSql();
        Assertions.assertEquals("SELECT `C1` + `C2` + `C3`, COUNT(1)\n" +
                "FROM `USERS`" ,formatSql);
    }
}
