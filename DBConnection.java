package digital_library_management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {
		private static final String uname = "root";
		private static final String pass = "12345";
		private static final String url = "jdbc:mysql://localhost:3306/library";
		
	public static Connection getConnection() throws SQLException {
		
		return DriverManager.getConnection(url,uname,pass);
	}
	static int Login_Id ;
	
	public static void register(String tableName,String username,String pass,int Id) throws SQLException {
		Connection con = DBConnection.getConnection();
		String registerQuery="insert into "+ tableName +" values(?,?,?)";
		PreparedStatement ps = con.prepareStatement(registerQuery);
		ps.setInt(1, Id);
		ps.setString(2,username);
		ps.setString(3, pass);
		
		int rowsAffected = ps.executeUpdate();
		
		if(rowsAffected > 0) {
			System.out.println("***Registered Successfully!***");
		}
		else {
			System.out.println("***Registration Failed!***");
		}
		
	}
	
	public static boolean login(String tableName,String username,String pass) throws SQLException {
		Connection con = DBConnection.getConnection();
		String loginQuery = "Select * from "+ tableName + " where username = ? and pass = ?";
		PreparedStatement ps = con.prepareStatement(loginQuery);
		ps.setString(1,username);
		ps.setString(2, pass);
		
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()) {
			Login_Id = rs.getInt("Id");
			return true;
		}
		else {
			return false;
		}
	}
	
	public static void showAll(String tableName) throws SQLException {
		String query = "Select * from "+tableName;
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		
		if(tableName == "users") {
			
		System.out.printf("%-10s %10s","Id","Username","Password\n");
		 System.out.println("-----------------------------------");
	    
	    while(rs.next()) {
	    	int id = rs.getInt("Id");
	    	String username = rs.getString("username");
	    	String password = rs.getString("pass");
	    	
	    	System.out.printf("%-10s%-10s%10s\n",id,username,password);
	    }
	    
		}
		else {
			List<Books> books = new ArrayList<>();
			while(rs.next()) {
				Books b = new Books();
				b.setId(rs.getInt("Id"));
				b.setTitle(rs.getString("title"));
				b.setAuthor(rs.getString("author"));
				b.setCategory(rs.getString("category"));
				b.setAvailable(rs.getBoolean("available"));
				books.add(b);
			}
			System.out.printf("%-5s %-25s %-15s %-15s %-10s%n", "ID", "Title", "Author", "Category", "Available");
            System.out.println("---------------------------------------------------------------");
			for(Books book : books) {
				System.out.println(book);
			}
		}
	}
	
	public static void addBook(String tableName) throws SQLException {
		String insertQuery = "insert into "+tableName+" values(?,?,?,?,?)";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(insertQuery);
		Books book = new Books();
		
		int id = book.getId();
		String title = book.getTitle();
		String author = book.getAuthor();
		String category = book.getCategory();
		boolean available = book.isAvailable();
		
		ps.setInt(1, id);
		ps.setString(2,title);
		ps.setString(3,author);
		ps.setString(4,category);
		ps.setBoolean(5, available);
		
		int rowsAffected = ps.executeUpdate();
		
		if(rowsAffected > 0) {
			System.out.println("***Book Added Successfully!***");
		}
		else {
			System.out.println("***Invalid Info!!***");
		}
	}
	
	public static void deleteInfo(String tableName,int id) throws SQLException {
		String deleteQuery = "delete from "+ tableName +" where Id =?";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(deleteQuery);
		ps.setInt(1, id);
		
		int rowsAffected = ps.executeUpdate();
		
		if(rowsAffected > 0) {
			System.out.println("***Deleted Successfully!***");
		}
		else {
			System.out.println("***Invalid Info!!***");
		}
	}
	
	public static void SearchBy(String Column,String value) throws SQLException {
		String SQuery = "Select * from books where "+Column+" =?";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(SQuery);
		ps.setString(1,value);
		ResultSet rs = ps.executeQuery();
	
		List<Books> books = new ArrayList<>();
		while(rs.next()) {
			Books b = new Books();
			b.setId(rs.getInt("Id"));
			b.setTitle(rs.getString("title"));
			b.setAuthor(rs.getString("author"));
			b.setCategory(rs.getString("category"));
			b.setAvailable(rs.getBoolean("available"));
			books.add(b);
		}
		System.out.printf("%-5s %-25s %-15s %-15s %-10s%n", "ID", "Title", "Author", "Category", "Available");
        System.out.println("---------------------------------------------------------------");
		for(Books book : books) {
			System.out.println(book);
		}
		
	}
	
	public static void bookIssue(int uId,int bId) throws SQLException {
		boolean isAvail= false;
		String availQuery = "select available from books where Id =?";
		String isseueQuery = "insert into book_trans values(?,?,?)";
		String updateAvail = "UPDATE books SET available = 0 where Id = ?";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(availQuery);
		ps.setInt(1, bId);
		ResultSet rs = ps.executeQuery();
		if(rs.next())
		{
			isAvail = rs.getBoolean("available");	
		}
		
		if(isAvail) {
			LocalDate date = LocalDate.now();
			ps= con.prepareStatement(isseueQuery);
			ps.setInt(1,uId);
			ps.setInt(2, bId);
			ps.setObject(3, date);
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				ps = con.prepareStatement(updateAvail);
				ps.setInt(1, bId);
				int rowAffected = ps.executeUpdate();	
				System.out.println("***Book Issued Successfully!***");
				System.out.println("***Happy reading!***");
			}
			else {
				System.out.println("***Invalid Info!!***");
			}
		}
		else {
			System.out.println("***Book is not available***\nOR\n***Invalid Credentials***");
		}
		
	}
	public static void viewProfile() throws SQLException {
		String query = "Select * from users where Id=?";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(query);
		ps.setInt(1,Login_Id);
		ResultSet rs = ps.executeQuery();
			
		System.out.printf("%-10s %-10s %-10s","Id","Username","Password\n");
		 System.out.println("-----------------------------------");
	    
	    if(rs.next()) {
	    	int id = rs.getInt("Id");
	    	String username = rs.getString("username");
	    	String password = rs.getString("pass");
	    	
	    	System.out.printf("%-10s%-10s %-10s\n",id,username,password);
	    }
	}
	
	public static void returnBook(int uId,int BId) throws SQLException {
		String updateAvail = "UPDATE books SET available = 1 where Id = ?";
		String deleteQuery = "Delete from book_trans where user_Id = ? and book_Id = ? ";
		String calDaysQyery = "Select issue_Date from book_trans where user_Id=?";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(updateAvail);
		ps.setInt(1, BId);
		ps.executeUpdate();
		ps = con.prepareStatement(deleteQuery);
		ps.setInt(1, uId);
		ps.setInt(2, BId);
		int rowsAffected = ps.executeUpdate();
		if(rowsAffected>0) {
			ps = con.prepareStatement(calDaysQyery);
			ps.setInt(1, uId);
			ResultSet rs = ps.executeQuery();
			Object startDate = rs.getObject("issue_Date");
	    	Temporal Date =  LocalDate.now();
	    	
	    	long diffDate = ChronoUnit.DAYS.between((Temporal)startDate,Date);
	    	if(diffDate >= 5) {
	    		diffDate = (diffDate - 5)*5;
	    		System.out.println("Your Fine is"+ diffDate +"Rs");
	    	}
	    	else {
	    		System.out.println("Happy Reading");
	    	}
			
			System.out.println("***Book Returned***");
		}
		else {
			System.out.println("***Invalid Credentials***");
		}
	}
	
	public static void issuedBook() throws SQLException {
		String viewQuery = "Select * from book_trans";
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(viewQuery);
		ResultSet rs = ps.executeQuery();
		System.out.printf("%-10s %-10s %-10s","User Id","Book Id","Issue Date\n");
		 System.out.println("-----------------------------------");
	    
		 
	    while(rs.next()) {
	    	int uid = rs.getInt("user_Id");
	    	int bid = rs.getInt("book_Id");
	    	String iDate = rs.getString("issue_Date");
	    	
	    	System.out.printf("%-10s %-10s %-10s\n",uid,bid,iDate);
	    
	}
	    
	}
}


