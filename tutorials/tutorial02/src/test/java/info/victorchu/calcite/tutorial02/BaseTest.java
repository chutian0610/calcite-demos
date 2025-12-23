package info.victorchu.calcite.tutorial02;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.UnaryOperator;

import org.apache.calcite.adapter.jdbc.JdbcSchema;
import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.config.CalciteConnectionConfigImpl;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.jdbc.JavaTypeFactoryImpl;
import org.apache.calcite.prepare.CalciteCatalogReader;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.sql.SqlOperatorTable;
import org.apache.calcite.sql.fun.SqlLibrary;
import org.apache.calcite.sql.fun.SqlLibraryOperatorTableFactory;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.SqlParserImplFactory;
import org.apache.calcite.sql.util.SqlOperatorTables;
import org.apache.calcite.sql.validate.SqlValidator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import com.google.common.collect.ImmutableList;

public abstract class BaseTest {
    protected CalciteSchema rootSchema;
    protected JavaTypeFactoryImpl typeFactory;
    protected CalciteConnectionConfig config;
    protected CalciteCatalogReader catalogReader;
    protected SqlParser.Config parserConfig;
    protected SqlValidator.Config validatorConfig;
    protected SqlOperatorTable stdSqlOperatorTable;
    protected SqlOperatorTable mySQLOperatorTable;

    @BeforeAll
    public void setUp() {
        Properties properties = new Properties();
        properties.setProperty("lex", "MYSQL");
        initConfig(properties);
        initSchema();
        initCatalogReader();
        initSqlOperatorTable();
    }

    private void initConfig(Properties properties) {
        config = new CalciteConnectionConfigImpl(properties);
        typeFactory = new JavaTypeFactoryImpl();
        parserConfig = SqlParser.config()
                .withQuotedCasing(config.quotedCasing())
                .withUnquotedCasing(config.unquotedCasing())
                .withQuoting(config.quoting())
                .withConformance(config.conformance())
                .withCaseSensitive(config.caseSensitive());
        SqlParserImplFactory parserFactory = config.parserFactory(SqlParserImplFactory.class, null);
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

    private void initCatalogReader() {
        catalogReader = new CalciteCatalogReader(
                rootSchema,
                rootSchema.getName() == null ? ImmutableList.of()
                        : ImmutableList.of(rootSchema.getName()),
                typeFactory,
                config);
    }

    private void initSqlOperatorTable() {
        final SqlOperatorTable opTab0 = SqlLibraryOperatorTableFactory.INSTANCE
                .getOperatorTable(SqlLibrary.STANDARD);
        final List<SqlOperatorTable> list0 = new ArrayList<>();
        list0.add(opTab0);
        list0.add(catalogReader);
        stdSqlOperatorTable = SqlOperatorTables.chain(list0);

        final SqlOperatorTable opTab1 = SqlLibraryOperatorTableFactory.INSTANCE
                .getOperatorTable(SqlLibrary.STANDARD, SqlLibrary.MYSQL);
        final List<SqlOperatorTable> list1 = new ArrayList<>();
        list1.add(opTab1);
        list1.add(catalogReader);
        mySQLOperatorTable = SqlOperatorTables.chain(list1);
    }

    private void initSchema() {
        rootSchema = CalciteSchema.createRootSchema(false);
        Map<String, Object> operand = new HashMap<>();
        operand.put("jdbcDriver", "com.mysql.cj.jdbc.Driver");
        operand.put("jdbcUrl", "jdbc:mysql://localhost:3306/test?allowPublicKeyRetrieval=true");
        operand.put("jdbcUser", "calcite");
        operand.put("jdbcPassword", "apache#calcite");
        operand.put("jdbcSchema", "test");
        Schema schema = JdbcSchema.Factory.INSTANCE.create(rootSchema.plus(), "mysql", operand);
        rootSchema.add("mysql", schema);
    }
    @AfterAll
    public void tearDown() {
        
    }
}
