package org.apache.calcite.sql.validate;

import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.prepare.CalciteCatalogReader;
import org.apache.calcite.prepare.CalciteSqlValidator;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperatorTable;
import org.checkerframework.checker.nullness.qual.PolyNull;

/** Validator with extended log of inner logical.
 */
@Slf4j
public class ExtendCalciteSqlValidator extends CalciteSqlValidator {

    public ExtendCalciteSqlValidator(SqlOperatorTable opTab,
                               CalciteCatalogReader catalogReader, JavaTypeFactory typeFactory,
                               Config config) {
        super(opTab, catalogReader, typeFactory, config);
    }
    @Override public SqlNode validate(SqlNode topNode) {
       log.info("【EntryPoint】Enter Sql Validate: {}", topNode.toString().replace("\n", " "));
       return super.validate(topNode);
    }

    @Override protected @PolyNull SqlNode performUnconditionalRewrites(
            @PolyNull SqlNode node,
            boolean underFrom) {
        if (node != null) {
            log.info("performUnconditionalRewrites(underFrom={}) node[{}]: {}",
                    underFrom,
                    node.getClass().getSimpleName(),
                    node.toString().replace("\n", " "));
        }

        return super.performUnconditionalRewrites(node, underFrom);
    }

}
