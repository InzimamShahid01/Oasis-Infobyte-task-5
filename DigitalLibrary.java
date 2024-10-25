package digital_library_management;

import java.util.Random;
import java.util.Scanner;

class Auth{
	private int Id;
	private String username;
	private String password;
	Scanner sc = new Scanner(System.in);
	
	
	public int getId() {
		Random rand = new Random();
		Id = rand.nextInt(999)+100;
		return Id;
	}
	
	public String getUsername() {
		System.out.print("Enter your name: ");
		username = sc.nextLine();
		return username;
	}
	public String getPassword() {
		System.out.print("Enter your password: ");
		password = sc.nextLine();
		return password;
	}
}

class Books{
	private int Id;
	private String title;
	private String author;
	private String category;
	private boolean available=true;
	Scanner sc = new Scanner(System.in);
	
	public int getId() {
		Random rand = new Random();
		Id = rand.nextInt(999)+100;
		return Id;
	}
	
	public void setId(int id) {
		Id = id;
	}
	
	public String getTitle() {
		System.out.println("Enter Book Title: ");
		title = sc.nextLine();
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		System.out.println("Enter Book Author Name: ");
		author = sc.nextLine();
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getCategory() {
		System.out.println("Enter Book Category: ");
	    category = sc.nextLine();
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}

	@Override
	public String toString() {
		return  String.format("%-5d %-25s %-15s %-15s %-10s", 
                Id, title, author, category, available);
	}
	
	
}


public class DigitalLibrary {
	static boolean adminLoggedIn = false;
	static boolean userLoggedIn = false;
	
	
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		Scanner scString = new Scanner(System.in);
		System.out.println("\n***Welcome to Digital Library****");
		try {
			
		
		while(true) {
			Auth auth = new Auth();
			System.out.println("You want to register/login as:");
			System.out.println("1.Admin Login");
			System.out.println("2.New User? Register!");
			System.out.println("3.User Login");
			System.out.println("Press 9 to Logout");
			int ch = sc.nextInt();
			
			switch(ch) {
			case 1:
				String uname = auth.getUsername();
				String pass = auth.getPassword();
				
				boolean flag = DBConnection.login("admin",uname,pass);
				if(flag) {
					adminLoggedIn = true;
					System.out.println("***Login Successfully!***");
				}
				else System.out.println("***Login Failed!***");
				break;
				
			case 2:
				 int Id = auth.getId();
				 String unameR = auth.getUsername();
				 String passR = auth.getPassword();
				 DBConnection.register("users",unameR,passR,Id);
				break;
				
			case 3:
				uname = auth.getUsername();
			    pass = auth.getPassword();
				
				flag = DBConnection.login("users",uname,pass);
				if(flag) {
					userLoggedIn = true;
					System.out.println("***Login Successfully!***");
				}
				else {
					System.out.println("***Login Failed!***");
				}
				break;
				
			case 9:
				System.exit(0);
			default:
				System.exit(0);
			}
			
			
			
			if(adminLoggedIn) {
				while(true) {
					System.out.println("\n1.Add User");
					System.out.println("2.Delete User");
					System.out.println("3.Show All User");
					System.out.println("4.Add Book");
					System.out.println("5.Delete Book");
					System.out.println("6.Show All Books");
					System.out.println("7.View Issued Books");
					System.out.println("Press any key to Logout");
					
					int choice = sc.nextInt();
					switch(choice) {
					case 1:
						 int Id = auth.getId();
						 String unameR = auth.getUsername();
						 String passR = auth.getPassword();
						DBConnection.register("users",unameR,passR,Id);
						break;
					case 2:
						System.out.print("Enter User ID to Delete: ");
						int id = sc.nextInt();
						DBConnection.deleteInfo("users", id);
						break;
					case 3:
						DBConnection.showAll("users");
						break;
					case 4:
						DBConnection.addBook("books");
						break;
					case 5:
						System.out.print("Enter Book ID to Delete: ");
						int i_d = sc.nextInt();
						DBConnection.deleteInfo("books", i_d);
						break;
					case 6:
						DBConnection.showAll("books");
						break;
					case 7:
						DBConnection.issuedBook();
						break;
					default:
						System.exit(0);
					}
					
				}
				
			}
		
			   if(userLoggedIn) {
				  
				while(true) {
					
					System.out.println("\n1.View Books");
					System.out.println("2.Search Book By Category");
					System.out.println("3.Search Book By Title");
					System.out.println("4.Issue Book");
					System.out.println("5.Return Book");
					System.out.println("6.Any query");
					System.out.println("7.View Profile");
					System.out.println("Press 9 to Logout");
					int choice  = sc.nextInt();
					switch(choice){
					case 1:
						DBConnection.showAll("books");
						break;
					case 2:
						System.out.print("Enter a book Category: ");
						String ctgry = scString.nextLine();
						DBConnection.SearchBy("category",ctgry);
						
						break;
					case 3:
						System.out.print("Enter a book title: ");
						String tit = scString.nextLine();
						
						DBConnection.SearchBy("title",tit);
						break;
					case 4:
						System.out.println("\n-->For 5 Days there is no charge\n-->Charge after 5 Days are 5rs/Day\n");
						System.out.println("Enter your User ID: ");
						int uId = sc.nextInt();
						System.out.println("Enter Book ID: ");
						int bId = sc.nextInt();
						DBConnection.bookIssue(uId, bId);
						break;
					case 5:
						System.out.println("Enter your User ID: ");
						 uId = sc.nextInt();
						System.out.println("Enter Book ID: ");
						 bId = sc.nextInt();
						DBConnection.returnBook(uId, bId);
						break;
					
					case 6:
						System.out.println("Send your query at inzimamwrsi99@gmail.com");
						break;
					case 7:
						DBConnection.viewProfile();
						break;
				    case 9:
						System.exit(0);
					default:
						System.exit(0);
					}
					
				}
			}
			
		}// end of while 
	}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
