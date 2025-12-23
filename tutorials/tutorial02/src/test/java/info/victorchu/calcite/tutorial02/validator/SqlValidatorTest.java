package info.victorchu.calcite.tutorial02.validator;

import org.apache.calcite.sql.validate.ExtendQueryCalciteSqlValidator;

import info.victorchu.calcite.tutorial02.BaseTest;
import info.victorchu.calcite.util.SqlNodeTreePrintVisitor;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.prepare.CalciteSqlValidator;
import org.apache.calcite.runtime.CalciteContextException;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.validate.SqlValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@Slf4j
@TestInstance(Lifecycle.PER_CLASS)
public class SqlValidatorTest extends BaseTest{
    @Test
    public void testQuery01() {
        String sql = "select * from mysql.customer limit 10";
        SqlParser parser = SqlParser.create(sql, parserConfig);
        SqlNode sqlNode;
        try {
            sqlNode = parser.parseStmt();
        } catch (SqlParseException e) {
            throw new RuntimeException(
                    "parse failed: " + e.getMessage(), e);
        }
        SqlNodeTreePrintVisitor visitor = new SqlNodeTreePrintVisitor();
        sqlNode.accept(visitor);
        SqlValidator sqlValidator = new CalciteSqlValidator(stdSqlOperatorTable, catalogReader, typeFactory,
                validatorConfig);
        sqlValidator.validate(sqlNode);
        SqlNodeTreePrintVisitor visitor2 = new SqlNodeTreePrintVisitor();
        sqlNode.accept(visitor2);
    }

    @Test
    public void testQuery02() {
        String sql = "select count_mock(1) from mysql.customer limit 10";
        SqlParser parser = SqlParser.create(sql, parserConfig);
        SqlNode sqlNode;
        try {
            sqlNode = parser.parseStmt();
        } catch (SqlParseException e) {
            throw new RuntimeException(
                    "parse failed: " + e.getMessage(), e);
        }
        // use ExtendCalciteSqlValidator to validate sqlNode
        SqlValidator sqlValidator = new ExtendQueryCalciteSqlValidator(stdSqlOperatorTable, catalogReader, typeFactory,
                validatorConfig);
        Assertions.assertThrows(CalciteContextException.class, () -> {
            sqlValidator.validate(sqlNode);
        });
    }

    @Test
    public void testQuery03() {
        String sql = "SELECT \n" + //
                        "    o.o_orderkey,\n" + //
                        "    o.o_orderdate,\n" + //
                        "    o.o_totalprice,\n" + //
                        "    c.c_name,\n" + //
                        "    c.c_address,\n" + //
                        "    c.c_phone\n" + //
                        "FROM mysql.orders o\n" + //
                        "JOIN mysql.customer c ON o.o_custkey = c.c_custkey\n" + //
                        "WHERE o.o_orderdate >= '1995-01-01'\n" + //
                        "  AND o.o_orderdate < '1996-01-01'\n" + //
                        "ORDER BY o.o_totalprice DESC\n" + //
                        "LIMIT 20";
        SqlParser parser = SqlParser.create(sql, parserConfig);
        SqlNode sqlNode;
        try {
            sqlNode = parser.parseStmt();
        } catch (SqlParseException e) {
            throw new RuntimeException(
                    "parse failed: " + e.getMessage(), e);
        }
        // use ExtendCalciteSqlValidator to validate sqlNode
        SqlValidator sqlValidator = new ExtendQueryCalciteSqlValidator(stdSqlOperatorTable, catalogReader, typeFactory,
                validatorConfig);
        sqlValidator.validate(sqlNode);
    }

    @Test
    public void testQuery04() {
        String sql = "SELECT `o_custkey`, GROUP_CONCAT(`o_orderpriority`)\n" + //
                "FROM mysql.`orders`\n" + //
                "GROUP BY `o_custkey`\n" + //
                "LIMIT 10";
        SqlParser parser = SqlParser.create(sql, parserConfig);
        SqlNode sqlNode;
        try {
            sqlNode = parser.parseStmt();
        } catch (SqlParseException e) {
            throw new RuntimeException(
                    "parse failed: " + e.getMessage(), e);
        }
        // use ExtendCalciteSqlValidator to validate sqlNode
        SqlValidator sqlValidator = new ExtendQueryCalciteSqlValidator(mySQLOperatorTable, catalogReader, typeFactory,
                validatorConfig);
        sqlValidator.validate(sqlNode);
    }
}
