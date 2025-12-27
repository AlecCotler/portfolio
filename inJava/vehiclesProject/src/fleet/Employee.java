package fleet;
/**
 * This enum class represents employees by their last names.
 * Each employee also has a Department.
 *
 * @author Carly Chick
 */
public enum Employee {
    PATEL(Department.ComputerScience),
    LIM(Department.ElectricalEngineering),
    ZIMNES(Department.ComputerScience),
    HARPER(Department.ElectricalEngineering),
    KAUR(Department.InformationTechnologyAndInformatics),
    TAYLOR(Department.Math),
    RAMESH(Department.Math),
    CERAVOLO(Department.BusinessAnalyticsAndInformationTechnology);

    private Department dept;

    /**
     * Creates an employee constant with the given department.
     *
     * @param dept the department for the employee
     */
    Employee(Department dept) {
        this.dept = dept;
    }

    /**
     * Method that gets the department for the respective employee.
     *
     * @return the department
     */
    public Department getDept() {
        return dept;
    }

    /**
     * Turns a string into the matching employee constant.
     * The match is case-insensitive.
     *
     * @param employeeStr the string to match against employee names
     * @return the matching employee constant; null if no match is found
     */
    public static Employee parseEmployee(String employeeStr) {
        Employee[] allEmployees = Employee.values();   // get all enum constants
        for (int i = 0; i < allEmployees.length; i++) {
            if (allEmployees[i].name().equalsIgnoreCase(employeeStr)) {
                return allEmployees[i];
            }
        }
        return null;
    }
}
