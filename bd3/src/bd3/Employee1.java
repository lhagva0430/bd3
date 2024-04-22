package bd3;
import java.io.*;
import java.util.*;
public class Employee1{
	private String name;
	private String id;
	private String work;
	private String date;
	public Employee1(String name, String id, String work, String date) {
		this.name = name;
		this.id = id;
		this.work = work;
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public String getId() {
		return id;
	}
	public String getWork() {
		return work;
		
	}
	public String getDate() {
		return date;
	}
	@Override
	public String toString() {
		return "Employee{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", work='" + work + '\'' +
                ", date='" + date + '\'' +
                '}';
	}
	public class EmployeeTransfer{
		public static void main(String[] args) {
			String filePath = "C:\\Users\\Dell\\Desktop\\hun ba com\\UBworkers.txt";
			String darhanFilePath = "C:\\Users\\Dell\\Desktop\\hun ba com\\DarhanWorkers.txt";
			List<Employee1> employees = parseEmployeeData(filePath); 
			
			Collections.sort(employees, Comparator.comparing(Employee1::getName));
			//tailbar
			List<Employee1> transferredEmployees = parseEmployeeData(darhanFilePath);
			Iterator<Employee1> iterator = employees.iterator();
			//tailbar
			while(iterator.hasNext()) {
				Employee1 employee1 = iterator.next();
				
				int yearsOfWork = calculateYearsOfWork(employee1.getDate());
				if(yearsOfWork > 1) {
					writeEmployeeToDarhanFile(employee1, darhanFilePath);
					
					iterator.remove();
				}
				
			}
			updateUBworkersFile(filePath, employees);
			
			System.out.println("Улаанбаатар серверт байгаа ажилчид: ");
			for(Employee1 employee1 : employees) {
				System.out.println(employee1);	
			}
			System.out.println("\nДархан серверт байгаа ажилчид: ");
			for (Employee1 employee1 : transferredEmployees) {
	            System.out.println(employee1);
	        }
			}
		
		
	
		private static List<Employee1> parseEmployeeData(String filePath) {
			List<Employee1> employees = new ArrayList<>();
			try (BufferedReader	br = new BufferedReader(new FileReader(filePath))){
				String line;
				while((line = br.readLine()) != null) {
					String[] parts = line.split("/");
					if(parts.length >= 4 ) {
						employees.add(new Employee1(parts[0], parts[1], parts[2], parts[3]));
					}
					else {
						System.err.println("Skipping line: " + line + " - Insufficient data");
					}
				}
				
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			return employees;
		}
		public static int calculateYearsOfWork(String startDate) {
			int startYear = Integer.parseInt(startDate.substring(0, 4));
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			return currentYear - startYear;
		}
		public static void writeEmployeeToDarhanFile(Employee1 employee1, String darhanFilePath) {
			// TODO Auto-generated method stub
			try(FileWriter writer = new FileWriter(darhanFilePath, true)){
				writer.write(employee1.getName() + "/" + employee1.getId() + "/" + employee1.getWork() + "/" + employee1.getDate() + "\n");
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		public static void updateUBworkersFile(String filePath, List<Employee1> employees) {
			// TODO Auto-generated method stub
			try (FileWriter writer = new FileWriter(filePath)) {
	            for (Employee1 employee1 : employees) {
	                writer.write(employee1.getName() + "/" + employee1.getId() + "/" + employee1.getWork() + "/" + employee1.getDate() + "\n");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
		
	
}
}