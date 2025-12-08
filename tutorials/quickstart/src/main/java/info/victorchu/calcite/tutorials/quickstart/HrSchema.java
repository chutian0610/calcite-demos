package info.victorchu.calcite.tutorials.quickstart;

import com.google.common.collect.ImmutableList;
import info.victorchu.calcite.tutorials.quickstart.bean.Department;
import info.victorchu.calcite.tutorials.quickstart.bean.Employee;
import info.victorchu.calcite.tutorials.quickstart.bean.Location;

import java.util.Arrays;
import java.util.Collections;

/**
 * 代码参考 apache calcite [org.apache.calcite.test.schemata.hr.HrSchema].
 * @date 2021/12/20 3:54 下午
 * @author victorchu
 */
public class HrSchema {
    @Override public String toString() {
        return "HrSchema";
    }

    public final Employee[] emps = {
            new Employee(100, 10, "Bill", 10000, 1000),
            new Employee(200, 20, "Eric", 8000, 500),
            new Employee(150, 10, "Sebastian", 7000, null),
            new Employee(110, 10, "Theodore", 11500, 250),
    };
    public final Department[] depts = {
            new Department(10, "Sales", Arrays.asList(emps[0], emps[2]),
                    new Location(-122, 38)),
            new Department(30, "Marketing", ImmutableList.<Employee>of(), new Location(0, 52)),
            new Department(40, "HR", Collections.singletonList(emps[1]), null),
    };

}
