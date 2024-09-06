package library;
import java.sql.*;
import java.util.Scanner;

public class library {
	
	String DB_URL ="jdbc:mysql://localhost:3306/library";
	String USER ="root";
	String PASSWORD ="Shahira@01";
	

    private Connection connection;
    private Scanner scanner;

    public library() {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected to the database.");
            scanner = new Scanner(System.in);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayMenu() {
        System.out.println("\nLibrary Management System Menu:");
        System.out.println("0. Exit Application");
        System.out.println("1. Add New Book");
        System.out.println("2. Search Book");
        System.out.println("3. Show All Books");
        System.out.println("4. Register Student");
        System.out.println("5. Check Out Book");
        System.out.print("Enter your choice: ");
    }

    public void addBook() {
        try {
            System.out.print("Enter book name: ");
            String bookName = scanner.nextLine();
            System.out.print("Enter author name: ");
            String authorName = scanner.nextLine();
            System.out.print("Enter price of book: ");
            int price = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
            System.out.print("Enter ISBN: ");
            String isbn = scanner.nextLine();
            
            String query = "INSERT INTO books1_detail (book_isbn, book_name, book_publisher, book_price) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, isbn);
            statement.setString(2, bookName);
            statement.setString(3, authorName);
            statement.setInt(4, price);
            statement.executeUpdate();
            System.out.println("Book added successfully.");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void searchBook() {
    	
    	    try {
    	        
    	        System.out.print("Enter author name: ");
    	        String authorName = scanner.nextLine();

    	        
    	        String query = "SELECT * FROM books1_detail WHERE book_publisher = ?";
    	        PreparedStatement statement = connection.prepareStatement(query);
    	        statement.setString(1, authorName);
    	        ResultSet resultSet = statement.executeQuery();

    	        
    	        System.out.println("Books by " + authorName + ":");
    	        System.out.println("ID\tISBN\tName\t\tAuthor\t\tQuantity\t\tPrice");
    	        while (resultSet.next()) {
    	            System.out.println(resultSet.getInt("bid") + "\t" +
    	                               resultSet.getString("book_isbn") + "\t" +
    	                               resultSet.getString("book_name") + "\t\t" +
    	                               resultSet.getString("book_publisher") + "\t\t" +
    	                               resultSet.getInt("book_price"));
    	                               
    	        }
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }
    	}
    public void showAllBooks() {
    	
    	
    	    try {
    	        
    	        String query = "SELECT * FROM books1_detail";
    	        PreparedStatement statement = connection.prepareStatement(query);
    	        ResultSet resultSet = statement.executeQuery();

    	        
    	        System.out.println("All Added Books:");
    	        System.out.println("ID\tISBN\t\tName\t\tAuthor\t\tPrice");
    	        while (resultSet.next()) {
    	            System.out.println(resultSet.getInt("bid") + "\t" +
    	                               resultSet.getString("book_isbn") + "\t" +
    	                               resultSet.getString("book_name") + "\t\t" +
    	                               resultSet.getString("book_publisher") + "\t\t" +
    	                               
    	                               resultSet.getInt("book_price"));
    	                               
    	        }
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }
    	}
    public void registerStudent() {
    	
    	    try {
    	        
    	        System.out.print("Enter register number: ");
    	        int registerNumber = scanner.nextInt();
    	        scanner.nextLine(); // Consume newline character
    	        System.out.print("Enter username: ");
    	        String username = scanner.nextLine();

    	        
    	        String query = "INSERT INTO users_details1 (USERNAME, PASSWORD, USER_TYPE) VALUES (?, ?, ?)";
    	        PreparedStatement statement = connection.prepareStatement(query);
    	        statement.setInt(1, registerNumber);
    	        statement.setString(2, username);
    	        statement.setInt(3, 1); 
    	        statement.executeUpdate();
    	        System.out.println("Student registered successfully.");
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }
    	}
    public void checkOutBook() {
        
    	
    
    	    try {
    	        
    	        System.out.print("Enter register number: ");
    	        int registerNumber = scanner.nextInt();
    	        scanner.nextLine(); 

    	        
    	        System.out.print("Enter book ID to check out: ");
    	        int bookId = scanner.nextInt();
    	        scanner.nextLine(); 

    	    
    	        String query = "SELECT * FROM books1_detail WHERE bid = ? AND book_price > 0";
    	        PreparedStatement statement = connection.prepareStatement(query);
    	        statement.setInt(1, bookId);
    	        ResultSet resultSet = statement.executeQuery();

    	        if (resultSet.next()) {
    	            
    	            String updateQuery = "UPDATE books1_detail SET book_price = book_price - 1 WHERE bid = ?";
    	            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
    	            updateStatement.setInt(1, bookId);
    	            updateStatement.executeUpdate();

    	            
    	            String insertQuery = "INSERT INTO issued_books1 (UID, BID, ISSUED_DATE, PERIOD) VALUES (?, ?, NOW(), ?)";
    	            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
    	            insertStatement.setInt(1, registerNumber);
    	            insertStatement.setInt(2, bookId);
    	            insertStatement.setInt(3, 14); // Assuming 14 days period
    	            insertStatement.executeUpdate();

    	            
    	            String bookDetailsQuery = "SELECT * FROM books1_detail WHERE bid = ?";
    	            PreparedStatement bookDetailsStatement = connection.prepareStatement(bookDetailsQuery);
    	            bookDetailsStatement.setInt(1, bookId);
    	            ResultSet bookDetailsResultSet = bookDetailsStatement.executeQuery();
    	            if (bookDetailsResultSet.next()) {
    	                System.out.println("Book successfully checked out:");
    	                System.out.println("Book ID: " + bookId);
    	                System.out.println("Book Name: " + bookDetailsResultSet.getString("book_name"));
    	                System.out.println("Author: " + bookDetailsResultSet.getString("book_publisher"));
    	                // You can print other book details as needed
    	            }
    	        } else {
    	            System.out.println("The book is not available for checkout.");
    	        }
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }
    	}

    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed.");
            }
            scanner.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        library librarySystem = new library();
        Scanner scanner = new Scanner(System.in);

        int choice = -1;

        while (choice != 0) {
            librarySystem.displayMenu();
            choice = scanner.nextInt();

            switch (choice) {
                case 0:
                    System.out.println("Exiting Application...");
                    break;
                case 1:
                    librarySystem.addBook();
                    break;
                case 2:
                    librarySystem.searchBook();
                    break;
                case 3:
                    librarySystem.showAllBooks();
                    break;
                case 4:
                    librarySystem.registerStudent();
                    break;
                case 5:
                    librarySystem.checkOutBook();
                    break;
                
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }

        librarySystem.closeConnection();
        scanner.close();
    }
}