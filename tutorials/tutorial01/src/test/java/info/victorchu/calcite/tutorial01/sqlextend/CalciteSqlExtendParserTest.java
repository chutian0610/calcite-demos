package info.victorchu.calcite.tutorial01.sqlextend;

import info.victorchu.calcite.util.SqlNodeTreePrintVisitor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.extend.impl.SqlExtendParserImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class CalciteSqlExtendParserTest {
    @SneakyThrows
    @Test
    public void defaultParseError(){
        String sql = "select name from ${users}";
        SqlParser sqlParser = SqlParser.create(sql, SqlParser.Config.DEFAULT);
        Assertions.assertThrows(SqlParseException.class,() ->{
            SqlNode node = sqlParser.parseQuery();
            SqlNodeTreePrintVisitor visitor = new SqlNodeTreePrintVisitor();
            node.accept(visitor);
        });

    }
    @SneakyThrows
    @Test
    public void extendParse(){
        String sql = "select name from ${users}";
        SqlParser sqlParser = SqlParser.create(sql, SqlParser.Config.DEFAULT.withParserFactory(SqlExtendParserImpl.FACTORY));
        SqlNode node = sqlParser.parseQuery();
        SqlNodeTreePrintVisitor visitor = new SqlNodeTreePrintVisitor();
        node.accept(visitor);
    }
    @SneakyThrows
    @Test
    public void extendParseError(){
        String sql = "select name from ${sys.users}";
        SqlParser sqlParser = SqlParser.create(sql, SqlParser.Config.DEFAULT.withParserFactory(SqlExtendParserImpl.FACTORY));
        Assertions.assertThrows(SqlParseException.class,() ->{
            SqlNode node = sqlParser.parseQuery();
            SqlNodeTreePrintVisitor visitor = new SqlNodeTreePrintVisitor();
            node.accept(visitor);
        });
    }
}
