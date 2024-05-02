package bd3;

public class Employee{
	private String name;
	private String id;
	private String work;
	private String date;
	public Employee(String name, String id, String work, String date) {
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
		return  
                "Нэр='" + name + '\'' +
                ", Код='" + id + '\'' +
                ", Мэргэжил='" + work + '\'' +
                ", Ажилд орсон өдөр='" + date;
                
	}
}
