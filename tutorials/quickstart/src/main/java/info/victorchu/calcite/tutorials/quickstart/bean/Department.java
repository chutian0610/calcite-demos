package info.victorchu.calcite.tutorials.quickstart.bean;

import java.util.List;
import java.util.Objects;

/**
 * 公司部门
 * 代码参考 apache calcite [org.apache.calcite.test.schemata.hr.Department].
 * @date 2021/12/20 3:49 下午
 * @author victorchu
 */
public class Department {
    /**
     * 部门ID
     */
    public final int deptno;
    /**
     * 部门名
     */
    public final String name;

    /**
     * 雇员
     */
    @org.apache.calcite.adapter.java.Array(component = Employee.class)
    public final List<Employee> employees;
    /**
     * 位置
     */
    public final Location location;

    public Department(int deptno, String name, List<Employee> employees,
                      Location location) {
        this.deptno = deptno;
        this.name = name;
        this.employees = employees;
        this.location = location;
    }

    @Override public String toString() {
        return "Department [deptno: " + deptno + ", name: " + name
                + ", employees: " + employees + ", location: " + location + "]";
    }

    @Override public boolean equals(Object obj) {
        return obj == this
                || obj instanceof Department
                && deptno == ((Department) obj).deptno;
    }

    @Override public int hashCode() {
        return Objects.hash(deptno);
    }
}
