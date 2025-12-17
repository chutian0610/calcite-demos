package org.apache.calcite.sql.validate;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.prepare.CalciteCatalogReader;
import org.apache.calcite.prepare.CalciteSqlValidator;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperatorTable;
import org.checkerframework.checker.nullness.qual.PolyNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import info.victorchu.calcite.util.IdentityHashSet;

/**
 * Validator with extended log of inner logical.
 */
@Slf4j
public class ExtendQueryCalciteSqlValidator extends CalciteSqlValidator {
    protected final IdentityHashMap<SqlValidatorScope, IdentityHashSet<SqlValidatorScope>> scopeTree = new IdentityHashMap<>();
    private Optional<SqlValidatorScope> root = Optional.empty();

    @EqualsAndHashCode
    @Builder
    static class Wrapper<T>{
        private Integer id;
        private T data;
    }

    private IdentityHashMap<SqlValidatorScope,Wrapper<SqlValidatorScope>> scopeNameMap = new IdentityHashMap<>(); 
    
    private AtomicInteger scopeId = new AtomicInteger(0);

    public ExtendQueryCalciteSqlValidator(SqlOperatorTable opTab,
            CalciteCatalogReader catalogReader, JavaTypeFactory typeFactory,
            Config config) {
        super(opTab, catalogReader, typeFactory, config);
    }

    private void afterValidate() {
        buildScopeTree();
        printScopeRoot();
        printScopeInfo();
    }

    private void printScopeInfo(){
        scopeNameMap.entrySet().forEach(x->{
            Wrapper<SqlValidatorScope> wrapper = x.getValue();
            String nameSpace = null;
            if(wrapper.data instanceof ListScope){
                ListScope scope = (ListScope) wrapper.data;
                nameSpace = scope.getChildren().stream().map(y->y.getType().toString()).collect(Collectors.joining(","));
            }
            String scopeInfo = String.format("scope_%s = { sql=%s,\n nameSpace=%s }",
                    wrapper.id,
                    wrapper.data.getNode().toString().replaceAll("\n"," "),
                    nameSpace == null? "NULL": "["+nameSpace+"]"

            );
            log.info(scopeInfo);
        });
    }
    private Wrapper<SqlValidatorScope> registerScope(SqlValidatorScope scope){
        if(scopeNameMap.containsKey(scope)){
            return scopeNameMap.get(scope);
        }
        return scopeNameMap.computeIfAbsent(scope, x -> {
            return Wrapper.<SqlValidatorScope>builder()
                .data(scope)
                .id(scopeId.incrementAndGet())
                .build();
        });
    }

    private String scope2String(SqlValidatorScope scope){
        Wrapper<SqlValidatorScope> wrapper = registerScope(scope);
        return String.format("scope_%s[%s] ",wrapper.id ,scope.getClass().getSimpleName());
    }

    private String scopeTree2String(SqlValidatorScope parent,SqlValidatorScope scope){
        return String.format("%s --> %s",scope2String(parent),scope2String(scope));
    }

    private void printScopeRoot() {
        Preconditions.checkArgument(root.isPresent());
        log.info("====== print Scope Tree ======");
        log.info("catalogScope --> {}",scope2String(root.get()));

        List<SqlValidatorScope> cursor = Lists.newArrayList(root.get());
        printScopeTree(cursor); 
    }  

    private void printScopeTree(List<SqlValidatorScope> list){
        ListIterator<SqlValidatorScope> listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            SqlValidatorScope cursor = listIterator.next();
            listIterator.remove();

            if (scopeTree.containsKey(cursor) && !scopeTree.get(cursor).isEmpty()) {
                @SuppressWarnings("null")
                List<SqlValidatorScope> children = Lists.newArrayList(scopeTree.get(cursor).iterator());
                for (SqlValidatorScope sqlValidatorScope : children) {
                    log.info(scopeTree2String(cursor, sqlValidatorScope));
                    listIterator.add(sqlValidatorScope);
                }
            }
        }
    }

    private void buildScopeTree() {
        scopes.entrySet().forEach(item -> {
            DelegatingScope delegatingScope = (DelegatingScope) item.getValue();
            SqlValidatorScope parent = delegatingScope.getParent();
            if (parent instanceof EmptyScope) {
                // skip
            } else if (parent instanceof CatalogScope) {
                root = Optional.of(delegatingScope);
            } else {
                if (scopeTree.containsKey(parent)) {
                    scopeTree.get(parent).add(delegatingScope);
                } else {
                    scopeTree.computeIfAbsent(parent, x -> new IdentityHashSet<SqlValidatorScope>())
                            .add(delegatingScope);
                }
            }
        });
    }

    @Override
    public SqlNode validate(SqlNode topNode) {
        log.info("Enter Sql Validate[{}]: {}", topNode.getClass().getSimpleName(),
                topNode.toString().replace("\n", " "));
        SqlNode node = super.validate(topNode);
        log.info("End Sql Validate[{}]: {}", topNode.getClass().getSimpleName(),
                topNode.toString().replace("\n", " "));
        afterValidate();
        return node;
    }

    @Override
    protected @PolyNull SqlNode performUnconditionalRewrites(
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
