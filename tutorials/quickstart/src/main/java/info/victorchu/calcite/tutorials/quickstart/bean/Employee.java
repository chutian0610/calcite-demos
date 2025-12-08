package info.victorchu.calcite.tutorials.quickstart.bean;

import java.util.Objects;

/**
 * 公司雇员.
 * 代码参考 apache.calcite [org.apache.calcite.test.schemata.hr.Employee].
 * @date 2021/12/20 3:49 下午
 * @author victorchutian
 */
public class Employee {
    /**
     * 雇员ID
     */
    public final int empid;
    /**
     * 所属部门
     */
    public final int deptno;
    /**
     * 雇员名称
     */
    public final String name;
    /**
     * 工资
     */
    public final float salary;
    /**
     * 佣金
     */
    public final Integer commission;

    public Employee(int empid, int deptno, String name, float salary,
                    Integer commission) {
        this.empid = empid;
        this.deptno = deptno;
        this.name = name;
        this.salary = salary;
        this.commission = commission;
    }

    @Override public String toString() {
        return "Employee [empid: " + empid + ", deptno: " + deptno
                + ", name: " + name + "]";
    }

    @Override public boolean equals(Object obj) {
        return obj == this
                || obj instanceof Employee
                && empid == ((Employee) obj).empid;
    }

    @Override public int hashCode() {
        return Objects.hash(empid);
    }
}