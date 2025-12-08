package info.victorchu.calcite.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.util.SqlBasicVisitor;

import java.util.stream.Collectors;

/**
 * @author victorchu
 * @date 2022/7/14 11:32
 */
@Slf4j
public class SqlNodeTreePrintVisitor extends SqlBasicVisitor<Object> {

    private int depth = 0;

    private String getLogPrefix() {
        StringBuffer sb = new StringBuffer();
        sb.append("|");
        int tmp = depth;
        while (tmp > 0) {
            sb.append("  ");
            tmp--;
        }
        sb.append("#");
        return sb.toString();
    }


    public SqlNodeTreePrintVisitor() {
        super();
    }

    @Override
    public Object visit(SqlLiteral literal) {
        log.info("{}SqlLiteral[{}]: type={} value={}",getLogPrefix(),
                literal.getParserPosition().toString(),
                literal.getTypeName(),String.valueOf(literal.getValue()));
        depth++;
        super.visit(literal);
        depth--;
        return null;
    }

    @Override
    public Object visit(SqlCall call) {
        log.info("{}SqlCall[{}]: type={} SqlOperator={},OperandList=[{}]",getLogPrefix(),
                call.getParserPosition().toString(),
                call.getClass().getSimpleName(),
                call.getOperator(),
                call.getOperandList().stream().map(
                        x->{
                            if(x == null){
                                return "NULL";
                            }
                            if (x instanceof SqlNodeList && ((SqlNodeList) x).isEmpty()){
                                return "[]";
                            }
                            return x.toString();
                        }
                ).collect(Collectors.joining(",")));
        depth++;
        super.visit(call);
        depth--;
        return null;
    }

    @Override
    public Object visit(SqlNodeList nodeList) {
        if(nodeList.isEmpty()){
            return null;
        }else {
            log.info("{}SqlNodeList[{}]: nodeList={}", getLogPrefix(),nodeList.getParserPosition().toString(), nodeList.toString());
        }
        depth++;
        super.visit(nodeList);
        depth--;
        return null;
    }

    @Override
    public Object visit(SqlIdentifier id) {
        log.info("{}SqlIdentifier[{}]: name={}",getLogPrefix(),id.getParserPosition().toString(),id.toString());
        depth++;
        super.visit(id);
        depth--;
        return null;
    }

    @Override
    public Object visit(SqlDataTypeSpec type) {
        log.info("{}SqlDataTypeSpec[{}]: name={}",getLogPrefix(),type.getParserPosition().toString(),type.toString());
        depth++;
        super.visit(type);
        depth--;
        return null;
    }

    @Override
    public Object visit(SqlDynamicParam param) {
        log.info("{}SqlDynamicParam[{}]: name={}",getLogPrefix(),param.getParserPosition().toString(),param.toString());
        depth++;
        super.visit(param);
        depth--;
        return null;
    }

    @Override
    public Object visit(SqlIntervalQualifier intervalQualifier) {
        log.info("{}SqlIntervalQualifier[{}]: timeFrameName={} timeUnitRange={} startPrecision={} fractionalSecondPrecision={}",getLogPrefix(),intervalQualifier.getParserPosition().toString()
                ,intervalQualifier.timeFrameName,intervalQualifier.timeUnitRange,intervalQualifier.getStartPrecisionPreservingDefault(),
                intervalQualifier.getFractionalSecondPrecisionPreservingDefault());
        depth++;
        super.visit(intervalQualifier);
        depth--;
        return null;
    }
}
