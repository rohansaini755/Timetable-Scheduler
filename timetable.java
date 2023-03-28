package timetable;

import java.sql.*;
import java.util.*;
interface TEch
{
	ArrayList <String> subjects = new ArrayList<>();
	ArrayList <ArrayList<String>> teachers = new ArrayList<>();
	ArrayList <ArrayList<String>> allocatedTeacher = new ArrayList<>();
	void getSubjects() throws SQLException;
	void getTeacher() throws SQLException;
	void allocationOfTeacher();
}
class CST implements TEch 
{
	Statement st;
	ResultSet rs;
	int numberOfSubjects;
	int numberOfSections;
	ArrayList<Integer> numberOfSubjectTeacher = new ArrayList<>();
	List<String> totalSubject = new ArrayList<>();
	Scanner ob = new Scanner(System.in);
	String course;
	String query;
	
	public CST(Connection con1,String course) throws SQLException
	{
		st = con1.createStatement(); 
		this.course = course;
		getSubjects();
		getTeacher();
		
	}	
	
	public void getSubjects() throws SQLException
	{
		System.out.println("timetable is being set for " + course);
		query = "select distinct subject from " + course;
		try {
			rs = st.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("no couses avilable");
			return;
		}
		while(rs.next())
		{
			totalSubject.add(rs.getString(1));
		}
		System.out.println(totalSubject);
		System.out.println("how many subjects do u wanna set");
		numberOfSubjects = ob.nextInt();
		ob.nextLine();
		System.out.println("enter subjects index number");
		int i =0;
		while(i < numberOfSubjects)
		{
			subjects.add(totalSubject.get(ob.nextInt() - 1));
			i++;
		}
//		System.out.println("subjects are sucessfully recorded");
	}
	
	public void getTeacher() throws SQLException
	{
//		System.out.println("ready to strat get teacher");
		int flag = 0;
		for(int i = 0 ; i < numberOfSubjects ; i++)
		{
			query = "select name from Btech where subject = " + "'" + subjects.get(i) + "'";
			rs = st.executeQuery(query);

			teachers.add(new ArrayList<>());
			while(rs.next())
			{
				teachers.get(i).add(rs.getString(1));
			}
			if(teachers.get(i).size() == 0)
			{
				System.out.println("there is no teacher in database for " + subjects.get(i));
				flag = 1;
				break;
			}
			numberOfSubjectTeacher.add(teachers.get(i).size());
	    }
		if(flag == 0)
			allocationOfTeacher();
		else
			System.out.println("add teacher for that subject");
     }
	void printNumberOfTeacher()
	{
		System.out.println(numberOfSubjectTeacher);
	}
	
	void printSubjects()
	{
		int i = 0;
		while(i < numberOfSubjects)
		{
			System.out.println(subjects.get(i));
			i++;
		}
	}
	
	public void allocationOfTeacher()
	{
		System.out.println("enter number of sections");
		numberOfSections = ob.nextInt();
		ob.nextLine();
		int [] indexOfSubjects = new int [numberOfSubjects];
		int j ,i = 0;
		try {
		while(i < numberOfSections)
		{	
			allocatedTeacher.add(new ArrayList<>());
			j = 0;
			while(j < numberOfSubjects)
			{
				allocatedTeacher.get(i).add(teachers.get(j).get(indexOfSubjects[j]));
				indexOfSubjects[j]++;
				indexOfSubjects[j] = indexOfSubjects[j] % (teachers.get(j).size());
				j++;
			}
			i++;
		}
		
		System.out.println("allocation has successfully completed");
		System.out.println(allocatedTeacher);
     }catch(ArrayIndexOutOfBoundsException e)
	{
    	 System.out.println("no teacher is there in database for some subjects");
	}
	}
	
}
public class timetable5{
	public static void main(String [] args) throws SQLException, ClassNotFoundException
	{
		String url = "jdbc:mysql://localhost:3306/studentTimetable";
		String user ="root";
		String password = "@Rony147896";
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con1 = DriverManager.getConnection(url, user, password);
		Statement st1 = con1.createStatement();
		ResultSet rs;
		List<String> courses  = new ArrayList<>();
		Scanner ob = new Scanner(System.in);
		
		
		String query = "select course from course";
		rs = st1.executeQuery(query);
		
		//get courses from database
		
		
		while(rs.next())
		{
			courses.add(rs.getString(1));
		}
		

		TEch o ;
		int ch = 0;
		do
		{
			System.out.println("Available courses are:" + courses);
			System.out.println("enter your choice");
			ch = ob.nextInt();
			ob.nextLine();
			System.out.println(courses.get(ch-1));
			o = new CST(con1,courses.get(ch-1));
		}while(ch!=0);

con1.close();
	}
}