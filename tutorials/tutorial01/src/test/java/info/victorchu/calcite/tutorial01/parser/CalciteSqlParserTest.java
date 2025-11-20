package info.victorchu.calcite.tutorial01.parser;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.babel.SqlBabelParserImpl;
import org.apache.calcite.sql.parser.ddl.SqlDdlParserImpl;
import org.junit.jupiter.api.Test;

/**
 * @author victorchu
 * @date 2022/7/9 9:20 下午
 */
@Slf4j
class CalciteSqlParserTest {

    @SneakyThrows
    @Test
    public void defaultParse(){
        String sql = "select name from users";
        SqlParser sqlParser = SqlParser.create(sql, SqlParser.Config.DEFAULT);
        SqlNode sqlNode = sqlParser.parseQuery();
        log.info(sqlNode.toString());
    }
    @SneakyThrows
    @Test
    public void babelParse(){
        String sql = "select name from users";
        SqlParser sqlParser = SqlParser.create(sql, SqlParser.Config.DEFAULT.withParserFactory(SqlBabelParserImpl.FACTORY));
        SqlNode sqlNode = sqlParser.parseQuery();
        log.info(sqlNode.toString());
    }
    @SneakyThrows
    @Test
    public void ddlParse(){
        String sql = "create table tdef (i int not null, j int default 100)";
        SqlParser sqlParser = SqlParser.create(sql, SqlParser.Config.DEFAULT.withParserFactory(SqlDdlParserImpl.FACTORY));
        SqlNode sqlNode = sqlParser.parseQuery();
        log.info(sqlNode.toString());
    }

}