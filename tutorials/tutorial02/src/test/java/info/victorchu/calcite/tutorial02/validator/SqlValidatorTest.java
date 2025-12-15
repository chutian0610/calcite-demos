package info.victorchu.calcite.tutorial02.validator;

import com.google.common.collect.ImmutableList;
import org.apache.calcite.sql.validate.ExtendCalciteSqlValidator;
import info.victorchu.calcite.util.SqlNodeTreePrintVisitor;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.adapter.jdbc.JdbcSchema;
import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.config.CalciteConnectionConfigImpl;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.jdbc.JavaTypeFactoryImpl;
import org.apache.calcite.prepare.CalciteCatalogReader;
import org.apache.calcite.prepare.CalciteSqlValidator;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperatorTable;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.SqlParserImplFactory;
import org.apache.calcite.sql.util.SqlOperatorTables;
import org.apache.calcite.sql.validate.SqlValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.UnaryOperator;

@Slf4j
public class SqlValidatorTest {

    static CalciteSchema rootSchema;
    static JavaTypeFactoryImpl typeFactory;
    static CalciteConnectionConfig config;
    static CalciteCatalogReader catalogReader;
    static SqlParser.Config  parserConfig;
    static SqlValidator.Config validatorConfig;
    static SqlOperatorTable sqlOperatorTable;

    @BeforeAll
    public static void setUp() {
        Properties properties = new Properties();
        properties.setProperty("lex", "MYSQL");
        initConfig(properties);
        initSchema();
        initCatalogReader();
        initSqlOperatorTable();
    }
    private static void initConfig(Properties properties){
        config = new CalciteConnectionConfigImpl(properties);
        typeFactory = new JavaTypeFactoryImpl();
        parserConfig = SqlParser.config()
                .withQuotedCasing(config.quotedCasing())
                .withUnquotedCasing(config.unquotedCasing())
                .withQuoting(config.quoting())
                .withConformance(config.conformance())
                .withCaseSensitive(config.caseSensitive());
        SqlParserImplFactory parserFactory =
                config.parserFactory(SqlParserImplFactory.class, null);
        if (parserFactory != null) {
            parserConfig = parserConfig.withParserFactory(parserFactory);
        }
        validatorConfig = UnaryOperator.<SqlValidator.Config>identity().apply(
                SqlValidator.Config.DEFAULT
                        .withLenientOperatorLookup(config.lenientOperatorLookup())
                        .withConformance(config.conformance())
                        .withDefaultNullCollation(config.defaultNullCollation())
                        .withIdentifierExpansion(true));
    }
    private static void initCatalogReader(){
        catalogReader = new CalciteCatalogReader(
                rootSchema,
                rootSchema.getName() == null? ImmutableList.of()
                        : ImmutableList.of(rootSchema.getName()),
                typeFactory,
                config);
    }
    private static void initSqlOperatorTable(){
        final SqlOperatorTable opTab0 =
                config.fun(SqlOperatorTable.class,
                        SqlStdOperatorTable.instance());
        final List<SqlOperatorTable> list = new ArrayList<>();
        list.add(opTab0);
        list.add(catalogReader);
        sqlOperatorTable = SqlOperatorTables.chain(list);
    }
    private static void initSchema() {
        rootSchema =  CalciteSchema.createRootSchema(false);
        Map<String, Object> operand = new HashMap<>();
        operand.put("jdbcDriver", "com.mysql.cj.jdbc.Driver");
        operand.put("jdbcUrl", "jdbc:mysql://localhost:3306/test?allowPublicKeyRetrieval=true");
        operand.put("jdbcUser", "calcite");
        operand.put("jdbcPassword", "apache#calcite");
        operand.put("jdbcSchema", "test");
        Schema schema = JdbcSchema.Factory.INSTANCE.create(rootSchema.plus(),"mysql",operand);
        rootSchema.add("mysql", schema);
    }

    @Test
    public void testQuery01(){
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
        SqlValidator sqlValidator =  new CalciteSqlValidator(sqlOperatorTable, catalogReader, typeFactory,
                validatorConfig);
        sqlValidator.validate(sqlNode);
        SqlNodeTreePrintVisitor visitor2 = new SqlNodeTreePrintVisitor();
        sqlNode.accept(visitor2);
    }
    @Test
    public void testQuery02(){
        String sql = "select * from mysql.customer limit 10";
        SqlParser parser = SqlParser.create(sql, parserConfig);
        SqlNode sqlNode;
        try {
            sqlNode = parser.parseStmt();
        } catch (SqlParseException e) {
            throw new RuntimeException(
                    "parse failed: " + e.getMessage(), e);
        }
        // use ExtendCalciteSqlValidator to validate sqlNode
        SqlValidator sqlValidator =  new ExtendCalciteSqlValidator(sqlOperatorTable, catalogReader, typeFactory,
                validatorConfig);
        sqlValidator.validate(sqlNode);
    }
}
