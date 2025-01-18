import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

interface IEmployee {
    double calculateSalary();

    String getDetails();

    String getEmployeeId();

    String getPassword();

    String getEmployeeType();

    void updateSalaryDetails();
}

abstract class Employee implements IEmployee {
    protected String name;
    protected double grossPay;
    protected double netPay;
    protected double deductions;
    protected String employeeId;
    protected String password;

    public Employee(String name, String employeeId, String password) {
        this.name = name;
        this.employeeId = employeeId;
        this.password = password;
    }

    protected void calculateNetPay() {
        this.netPay = this.grossPay - this.deductions;
    }

    @Override
    public String getDetails() {
        return "Employee Name: " + name + " | Gross Pay: " + grossPay + " | Deductions: " + deductions + " | Net Pay: "
                + netPay;
    }

    @Override
    public String getEmployeeId() {
        return this.employeeId;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public abstract String getEmployeeType();

    @Override
    public abstract void updateSalaryDetails();
}

class PermanentEmployee extends Employee {
    private double basicPay;
    private double bonus;

    public PermanentEmployee(String name, String employeeId, String password, double basicPay, double bonus) {
        super(name, employeeId, password);
        this.basicPay = basicPay;
        this.bonus = bonus;
    }

    @Override
    public double calculateSalary() {
        this.grossPay = basicPay + bonus;
        this.deductions = calculateDeductions();
        calculateNetPay();
        return netPay;
    }

    private double calculateDeductions() {
        double tax = grossPay * 0.1; // 10% tax
        double insurance = grossPay * 0.03; // 3% employer tax
        double companyInsurance = grossPay * 0.05; // 5% company insurance
        double operationalExpenses = grossPay * 0.02; // 2% other expenses
        return tax + insurance + companyInsurance + operationalExpenses;
    }

    @Override
    public String getEmployeeType() {
        return "Permanent";
    }

    @Override
    public void updateSalaryDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new basic pay for " + name + ": ");
        this.basicPay = scanner.nextDouble();
        System.out.println("Enter new bonus for " + name + ": ");
        this.bonus = scanner.nextDouble();
    }
}

class ContractEmployee extends Employee {
    private double hourlyRate;
    private int hoursWorked;

    public ContractEmployee(String name, String employeeId, String password, double hourlyRate, int hoursWorked) {
        super(name, employeeId, password);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    @Override
    public double calculateSalary() {
        this.grossPay = hourlyRate * hoursWorked;
        this.deductions = calculateDeductions();
        calculateNetPay();
        return netPay;
    }

    private double calculateDeductions() {
        double tax = grossPay * 0.1; // 10% tax
        double insurance = grossPay * 0.03; // 3% employer tax
        return tax + insurance;
    }

    @Override
    public String getEmployeeType() {
        return "Contract";
    }

    @Override
    public void updateSalaryDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new hourly rate for " + name + ": ");
        this.hourlyRate = scanner.nextDouble();
        System.out.println("Enter new hours worked for " + name + ": ");
        this.hoursWorked = scanner.nextInt();
    }
}

class Intern extends Employee {
    private static final double FIXED_SALARY = 10000;

    public Intern(String name, String employeeId, String password) {
        super(name, employeeId, password);
    }

    @Override
    public double calculateSalary() {
        this.grossPay = FIXED_SALARY;
        this.deductions = calculateDeductions();
        calculateNetPay();
        return netPay;
    }

    private double calculateDeductions() {
        double tax = grossPay * 0.1; // 10% tax
        return tax;
    }

    @Override
    public String getEmployeeType() {
        return "Intern";
    }

    @Override
    public void updateSalaryDetails() {
        // Intern salary is fixed, so no update here
    }
}

class Freelancer extends Employee {
    private double projectRate;

    public Freelancer(String name, String employeeId, String password, double projectRate) {
        super(name, employeeId, password);
        this.projectRate = projectRate;
    }

    @Override
    public double calculateSalary() {
        this.grossPay = projectRate;
        this.deductions = calculateDeductions();
        calculateNetPay();
        return netPay;
    }

    private double calculateDeductions() {
        double tax = grossPay * 0.1; // 10% tax
        return tax;
    }

    @Override
    public String getEmployeeType() {
        return "Freelancer";
    }

    @Override
    public void updateSalaryDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new project rate for " + name + ": ");
        this.projectRate = scanner.nextDouble();
    }
}

class PayrollService {
    private static final Logger logger = Logger.getLogger(PayrollService.class.getName());

    public void generatePayroll(List<IEmployee> employees) {
        if (!employees.isEmpty()) {
            logger.info("----------------------------------------");
            for (IEmployee employee : employees) {
                logger.info(employee.getDetails());
                logger.info("Calculated Net Salary: " + employee.calculateSalary());
            }
            logger.info("----------------------------------------");
        } else {
            logger.warning("No employees to generate payroll for.");
        }
    }

    public void showEmployeeSalary(String employeeId, String password, List<IEmployee> employees) {
        boolean found = false;
        for (IEmployee employee : employees) {
            if (employee.getEmployeeId().equals(employeeId) && employee.getPassword().equals(password)) {
                logger.info("----------------------------------------");
                logger.info("Salary details for " + employeeId + ":");
                logger.info("Net Pay: " + employee.calculateSalary());
                logger.info("----------------------------------------");
                found = true;
                break;
            }
        }
        if (!found) {
            logger.warning("Invalid Employee ID or Password.");
        }
    }

    public void updateEmployeeSalary(String employeeId, List<IEmployee> employees) {
        for (IEmployee employee : employees) {
            if (employee.getEmployeeId().equals(employeeId)) {
                employee.updateSalaryDetails();
                logger.info("Salary details for " + employeeId + " updated.");
                return;
            }
        }
        logger.warning("Employee ID not found.");
    }

    public void showEmployeeList(List<IEmployee> employees) {
        logger.info("List of all employees:");
        for (IEmployee employee : employees) {
            logger.info("Name: " + employee.getDetails() + " | Type: " + employee.getEmployeeType());
        }
    }
}

public class EmployeesalarymanagementSystem {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(EmployeesalarymanagementSystem.class.getName());
        Scanner scanner = new Scanner(System.in);

        List<IEmployee> employees = new ArrayList<>();

        employees.add(new PermanentEmployee("divya", "emp001", "empPass1", 1000000, 30000));
        employees.add(new ContractEmployee("anushka", "emp002", "empPass2", 100, 8));

        PayrollService payrollService = new PayrollService();

        boolean continueRunning = true;

        while (continueRunning) {
            logger.info("Choose your role:");
            logger.info("1. Employee");
            logger.info("2. Manager");
            logger.info("3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    logger.info("Enter Employee ID: ");
                    String empId = scanner.nextLine();
                    logger.info("Enter Password: ");
                    String empPassword = scanner.nextLine();
                    payrollService.showEmployeeSalary(empId, empPassword, employees);
                    break;

                case 2:
                    logger.info("Enter Manager Password: ");
                    String managerPassword = scanner.nextLine();
                    if ("manager123".equals(managerPassword)) {
                        boolean managerOptions = true;
                        while (managerOptions) {
                            logger.info("Choose an option:");
                            logger.info("1. Add a new employee");
                            logger.info("2. Update employee salary settings");
                            logger.info("3. View employee details");
                            logger.info("4. Generate payroll");
                            logger.info("5. Exit");
                            int managerChoice = scanner.nextInt();
                            scanner.nextLine();

                            switch (managerChoice) {
                                case 1:
                                    logger.info(
                                            "Enter the type of employee to add (Permanent/Contract/Intern/Freelancer): ");
                                    String type = scanner.nextLine();
                                    logger.info("Enter Employee Name: ");
                                    String name = scanner.nextLine();
                                    logger.info("Enter Employee ID: ");
                                    String id = scanner.nextLine();
                                    logger.info("Enter Password: ");
                                    String password = scanner.nextLine();

                                    switch (type.toLowerCase()) {
                                        case "permanent":
                                            logger.info("Enter Basic Pay: ");
                                            double basicPay = scanner.nextDouble();
                                            logger.info("Enter Bonus: ");
                                            double bonus = scanner.nextDouble();
                                            employees.add(new PermanentEmployee(name, id, password, basicPay, bonus));
                                            break;

                                        case "contract":
                                            logger.info("Enter Hourly Rate: ");
                                            double hourlyRate = scanner.nextDouble();
                                            logger.info("Enter Hours Worked: ");
                                            int hoursWorked = scanner.nextInt();
                                            employees.add(
                                                    new ContractEmployee(name, id, password, hourlyRate, hoursWorked));
                                            break;

                                        case "intern":
                                            employees.add(new Intern(name, id, password));
                                            break;

                                        case "freelancer":
                                            logger.info("Enter Project Rate: ");
                                            double projectRate = scanner.nextDouble();
                                            employees.add(new Freelancer(name, id, password, projectRate));
                                            break;

                                        default:
                                            logger.warning("Invalid employee type.");
                                            break;
                                    }
                                    logger.info("Employee added successfully.");
                                    break;

                                case 2:
                                    logger.info("Enter Employee ID to update salary: ");
                                    String updateEmpId = scanner.nextLine();
                                    payrollService.updateEmployeeSalary(updateEmpId, employees);
                                    break;

                                case 3:
                                    payrollService.showEmployeeList(employees);
                                    break;

                                case 4:
                                    payrollService.generatePayroll(employees);
                                    break;

                                case 5:
                                    managerOptions = false;
                                    break;

                                default:
                                    logger.warning("Invalid choice. Please try again.");
                                    break;
                            }
                        }
                    } else {
                        logger.warning("Incorrect Manager Password.");
                    }
                    break;

                case 3:
                    logger.info("Exiting the program.");
                    continueRunning = false;
                    break;

                default:
                    logger.warning("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}