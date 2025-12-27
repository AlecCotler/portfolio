package fleet;
/**
 * This enum class represents the different departments
 * that employees can belong to.
 *
 * @author Carly Chick
 */
public enum Department {
    ComputerScience,
    ElectricalEngineering,
    InformationTechnologyAndInformatics,
    Math,
    BusinessAnalyticsAndInformationTechnology;

    /**
     * Constructor for Department
     */
    Department() {
    }

    /**
     * Returns an array of Departments
     *
     * @return array of Departments
     */
    public static Department[] toArray() {
        return new Department[]{
                BusinessAnalyticsAndInformationTechnology,
                ComputerScience,
                ElectricalEngineering,
                InformationTechnologyAndInformatics,
                Math
        };
    }

    /**
     * This method converts a department constant into it's respective grammatically correct String.
     *
     * @return a grammatically correct string that corresponds with a Department enum
     */
    public String convertToWords(){
        switch(this.name()){
            case "ComputerScience":
                return "Computer Science";
            case "ElectricalEngineering":
                return "Electrical Engineering";
            case "InformationTechnologyAndInformatics":
                return "Information Technology and Informatics";
            case "Math":
                return "Mathematics";
            case "BusinessAnalyticsAndInformationTechnology":
                return "Business Analytics and Information Technology";
            default:
                return "Failed to find department.";
        }
    }
}
