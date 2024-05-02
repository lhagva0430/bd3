package bd3;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class EmployeeTransfer{
		public static void main(String[] args) {
			System.out.println("Вариантын дугаар:");
			Scanner sc=new Scanner(System.in);
			int a=sc.nextInt();
			while(a!=1&&a!=2) {
				a=sc.nextInt();
			}
			switch (a) {
			case 1:
				variant1();
				break;
			case 2:
				variant2();
				break;
			}
		}
		
		public static void variant2() {
			/*Улаанбаатарын ажилчдын мэдээллийг дарханы файлд хадгалж
			 *  ангалан Улаанбаатарын файлд хадгална
			*/
			String filePath = "C:\\Users\\Dell\\Desktop\\hun ba com\\UBworkers.txt";
			String darhanFilePath = "C:\\Users\\Dell\\Desktop\\hun ba com\\DarhanWorkers.txt";
			List<Employee1> employees = parseEmployeeData(filePath); 
			
			Collections.sort(employees, Comparator.comparing(Employee1::getName));
			
			Iterator<Employee1> iterator = employees.iterator();
			
			while(iterator.hasNext()) {
				Employee1 employee1 = iterator.next();

					writeEmployeeToDarhanFile(employee1, darhanFilePath);
					
					iterator.remove();
				
			}
			updateUBworkersFile(filePath, employees);
			List<Employee1> transferredEmployees = parseEmployeeData(darhanFilePath);
			Collections.sort(transferredEmployees, Comparator.comparing(Employee1::getName));
			Iterator<Employee1> daiterator = transferredEmployees.iterator();
			while(daiterator.hasNext()) {
				Employee1 employee1 = daiterator.next();
				int yearsOfWork = calculateYearsOfWork(employee1.getDate());
				if(yearsOfWork > 1) {
					writeEmployeeToDarhanFile(employee1, filePath);
					daiterator.remove();
				}
			}
			updateUBworkersFile(darhanFilePath,transferredEmployees );
			
			System.out.println("амжилттай");
			
		}
		public static void variant1() {
			/*Улаанбаатарын файлаас ангилсан ажилчдын мэдээллийг
			 *  дарханы файлд хадгалах
			 */
			String filePath = "C:\\Users\\Dell\\Desktop\\hun ba com\\UBworkers1.txt";
			String darhanFilePath = "C:\\Users\\Dell\\Desktop\\hun ba com\\DarhanWorkers1.txt";
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
			System.out.println("амжилттай");
			
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
