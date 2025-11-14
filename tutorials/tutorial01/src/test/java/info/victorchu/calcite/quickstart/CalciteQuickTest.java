package info.victorchu.calcite.quickstart;

import info.victorchu.calcite.util.SqlNodeTreePrintVisitor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParser;
import org.junit.jupiter.api.Test;

/**
 * @author victorchu
 * @date 2022/7/9 9:20 下午
 */
@Slf4j
class CalciteQuickTest {

    @SneakyThrows
    @Test
    public void convertQueryToSqlNode(){
        // Convert query to SqlNode
        String sql = "select price from transactions";
        // use default config
        SqlParser.Config config = SqlParser.config();

        SqlParser parser = SqlParser.create(sql, config);
        //构建parse tree
        SqlNode node = parser.parseQuery();
        log.info(node.toString());
        SqlNodeTreePrintVisitor visitor = new SqlNodeTreePrintVisitor();
        node.accept(visitor);
    }

}