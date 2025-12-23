/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.victorchu.calcite.tutorial02.converter;

import org.apache.calcite.plan.Contexts;
import org.apache.calcite.plan.ConventionTraitDef;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptCostImpl;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.plan.volcano.VolcanoPlanner;
import org.apache.calcite.prepare.CalciteSqlValidator;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.rex.RexBuilder;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql2rel.SqlToRelConverter;
import org.apache.calcite.sql2rel.StandardConvertletTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import info.victorchu.calcite.tutorial02.BaseTest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@TestInstance(Lifecycle.PER_CLASS)
public class RelNodeConverterTest extends BaseTest {
    @Test
    public void testQuery01() {
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
        log.info(sql);
        SqlParser parser = SqlParser.create(sql, parserConfig);
        SqlNode sqlNode;
        try {
            sqlNode = parser.parseStmt();
        } catch (SqlParseException e) {
            throw new RuntimeException(
                    "parse failed: " + e.getMessage(), e);
        }
        SqlValidator sqlValidator = new CalciteSqlValidator(stdSqlOperatorTable, catalogReader, typeFactory,
                validatorConfig);

        VolcanoPlanner planner = new VolcanoPlanner(RelOptCostImpl.FACTORY, Contexts.of(config));
        planner.addRelTraitDef(ConventionTraitDef.INSTANCE);

        RelOptCluster cluster = RelOptCluster.create(planner, new RexBuilder(typeFactory));
        SqlToRelConverter.Config converterConfig = SqlToRelConverter.config()
                .withTrimUnusedFields(true)
                .withExpand(false)
                .withInSubQueryThreshold(0) // not convert in subquery to join
                ;
        SqlToRelConverter converter = new SqlToRelConverter(null,
                sqlValidator,
                catalogReader,
                cluster,
                StandardConvertletTable.INSTANCE,
                converterConfig);
        RelRoot root = converter.convertQuery(sqlNode, true, true);
        log.info(RelOptUtil.toString(root.rel));  
    }
}
