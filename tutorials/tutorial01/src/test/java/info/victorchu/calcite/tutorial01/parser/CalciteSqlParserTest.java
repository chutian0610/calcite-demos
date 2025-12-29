package info.victorchu.calcite.tutorial01.parser;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.calcite.config.Lex;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.babel.SqlBabelParserImpl;
import org.apache.calcite.sql.parser.ddl.SqlDdlParserImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author victorchu
 * @date 2022/7/9 9:20 下午
 */
@Slf4j
class CalciteSqlParserTest {

    @SneakyThrows
    @Test
    public void defaultParse() {
        String sql = "select name from users";
        SqlParser sqlParser = SqlParser.create(sql, SqlParser.Config.DEFAULT);
        SqlNode sqlNode = sqlParser.parseQuery();
        Assertions.assertEquals("SELECT `NAME`\n" +
                "FROM `USERS`", sqlNode.toString());
    }

    @SneakyThrows
    @Test
    public void babelParse() {
        String sql = "select name from users";
        SqlParser sqlParser = SqlParser.create(sql,
                SqlParser.Config.DEFAULT.withParserFactory(SqlBabelParserImpl.FACTORY));
        SqlNode sqlNode = sqlParser.parseQuery();
        Assertions.assertEquals("SELECT `NAME`\n" +
                "FROM `USERS`", sqlNode.toString());
    }

    @SneakyThrows
    @Test
    public void ddlParse() {
        String sql = "create table tdef (i int not null, j int default 100)";
        SqlParser sqlParser = SqlParser.create(sql,
                SqlParser.Config.DEFAULT.withParserFactory(SqlDdlParserImpl.FACTORY));
        SqlNode sqlNode = sqlParser.parseQuery();
        Assertions.assertEquals("CREATE TABLE `TDEF` (`I` INTEGER NOT NULL, `J` INTEGER DEFAULT (100))",
                sqlNode.toString());
    }

    @SneakyThrows
    @Test
    public void defaultParseSimpleFunction() {
        String sql = "select upper(name) from users";
        SqlParser sqlParser = SqlParser.create(sql, SqlParser.Config.DEFAULT);
        SqlNode sqlNode = sqlParser.parseQuery();
        Assertions.assertEquals("SELECT UPPER(`NAME`)\n" +
                "FROM `USERS`", sqlNode.toString());
    }
    @SneakyThrows
    @Test
    public void defaultParseMemberFunction01() {
        String sql = "select myColumn.func(a, b) from users";
        SqlParser sqlParser = SqlParser.create(sql, SqlParser.Config.DEFAULT);
        SqlNode sqlNode = sqlParser.parseQuery();
        Assertions.assertEquals("SELECT `MYCOLUMN`.`FUNC`(`A`, `B`)\n" +
                "FROM `USERS`", sqlNode.toString());
    }
    @SneakyThrows
    @Test
    public void defaultKeyWord01() {
        String sql = "SELECT o_orderdate, YEAR(o_orderdate) AS `year` FROM orders";
        SqlParser sqlParser = SqlParser.create(sql, SqlParser.Config.DEFAULT.withLex(Lex.MYSQL));
        SqlNode sqlNode = sqlParser.parseQuery();
        Assertions.assertEquals("SELECT `o_orderdate`, YEAR(`o_orderdate`) AS `year`\n" +
                                "FROM `orders`", sqlNode.toString());
    }
}