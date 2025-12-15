package org.apache.calcite.sql.validate;

import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.prepare.CalciteCatalogReader;
import org.apache.calcite.prepare.CalciteSqlValidator;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperatorTable;

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

}
