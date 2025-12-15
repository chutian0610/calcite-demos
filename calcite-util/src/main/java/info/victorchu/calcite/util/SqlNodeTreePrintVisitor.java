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

    boolean withPosition = false;
    boolean withDetail = false;
    boolean ignoreEmptyNodeList = true;

    public SqlNodeTreePrintVisitor() {
        super();
    }

    public SqlNodeTreePrintVisitor(boolean withPosition, boolean withDetail,boolean ignoreEmptyNodeList) {
        super();
        this.withPosition = withPosition;
        this.withDetail = withDetail;
        this.ignoreEmptyNodeList = ignoreEmptyNodeList;
    }

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

    @Override
    public Object visit(SqlLiteral literal) {
        StringBuilder sb = new StringBuilder();
        sb.append(getLogPrefix()).append("SqlLiteral");
        if (withPosition) {
            sb.append("[").append(literal.getParserPosition().toString()).append("]");
        }
        sb.append(": type=").append(literal.getTypeName()).append(" value=").append(String.valueOf(literal.getValue()));
        log.info(sb.toString());
        depth++;
        super.visit(literal);
        depth--;
        return null;
    }

    @Override
    public Object visit(SqlCall call) {
        StringBuilder sb = new StringBuilder();
        sb.append(getLogPrefix()).append("SqlCall");
        if (withPosition) {
            sb.append("[").append(call.getParserPosition().toString()).append("]");
        }
        sb.append(": type=").append(call.getClass().getSimpleName());
        if (withDetail) {
            sb.append(", SqlOperator=").append(call.getOperator())
                    .append(", OperandList=[");
            if (!call.getOperandList().isEmpty()) {
                sb.append(call.getOperandList().stream().map(
                        x -> {
                            if (x == null) {
                                return "NULL";
                            }
                            return x.toString();
                        }
                ).collect(Collectors.joining(",")));
            }
            sb.append("]");
        }
        log.info(sb.toString());
        depth++;
        super.visit(call);
        depth--;
        return null;
    }

    @Override
    public Object visit(SqlNodeList nodeList) {
        if (ignoreEmptyNodeList && nodeList.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getLogPrefix()).append("SqlNodeList");
        if (withPosition) {
            sb.append("[").append(nodeList.getParserPosition().toString()).append("]");
        }
        sb.append(": nodeList=[");
        if (!nodeList.isEmpty()) {
            nodeList.forEach(x -> sb.append(x.toString()).append(","));
            sb.setLength(sb.length() - 1);
        }
        sb.append("]");
        log.info(sb.toString());
        depth++;
        super.visit(nodeList);
        depth--;
        return null;
    }

    @Override
    public Object visit(SqlIdentifier id) {
        StringBuilder sb = new StringBuilder();
        sb.append(getLogPrefix()).append("SqlIdentifier");
        if (withPosition) {
            sb.append("[").append(id.getParserPosition().toString()).append("]");
        }
        sb.append(": name=").append(id.toString());
        log.info(sb.toString());
        depth++;
        super.visit(id);
        depth--;
        return null;
    }

    @Override
    public Object visit(SqlDataTypeSpec type) {
        StringBuilder sb = new StringBuilder();
        sb.append(getLogPrefix()).append("SqlDataTypeSpec");
        if (withPosition) {
            sb.append("[").append(type.getParserPosition().toString()).append("]");
        }
        sb.append(": name=").append(type.toString());
        log.info(sb.toString());
        depth++;
        super.visit(type);
        depth--;
        return null;
    }

    @Override
    public Object visit(SqlDynamicParam param) {
        StringBuilder sb = new StringBuilder();
        sb.append(getLogPrefix()).append("SqlDynamicParam");
        if (withPosition) {
            sb.append("[").append(param.getParserPosition().toString()).append("]");
        }
        sb.append(": name=").append(param.toString());
        log.info(sb.toString());
        depth++;
        super.visit(param);
        depth--;
        return null;
    }

    @Override
    public Object visit(SqlIntervalQualifier intervalQualifier) {
        StringBuilder sb = new StringBuilder();
        sb.append(getLogPrefix()).append("SqlIntervalQualifier");
        if (withPosition) {
            sb.append("[").append(intervalQualifier.getParserPosition().toString()).append("]");
        }
        sb.append(": type=").append(intervalQualifier.typeName().toString());
        if (withDetail) {
            sb.append("timeFrameName=").append(intervalQualifier.timeFrameName)
                    .append(" timeUnitRange=").append(intervalQualifier.timeUnitRange)
                    .append(" startPrecision=").append(intervalQualifier.getStartPrecisionPreservingDefault())
                    .append(" fractionalSecondPrecision=").append(intervalQualifier.getFractionalSecondPrecisionPreservingDefault());
        }
        log.info(sb.toString());
        depth++;
        super.visit(intervalQualifier);
        depth--;
        return null;
    }
}
