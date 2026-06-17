import java.util.ArrayList;

class Library extends FacilityUnit implements Reportable {

    private ArrayList<LibraryBooks> books;
    private int visitorsPerDay;
    private static int borrowedbooks;
    private static int returnedbooks;


    public Library() {
        super();
        visitorsPerDay = 0;
        books = new ArrayList<>();
    }

    public Library(String entityID, String name, String location,
                   double maintenanceCost, int usageFrequency,
                   int visitorsPerDay) {

        super(entityID, name, location, maintenanceCost, usageFrequency);

        this.visitorsPerDay = visitorsPerDay;
        books = new ArrayList<>();
    }

    public int getVisitorsPerDay() {
        return visitorsPerDay;
    }

    public void setVisitorsPerDay(int visitorsPerDay) {
        this.visitorsPerDay = visitorsPerDay;
    }

    public boolean addBook(LibraryBooks book) {
        if (book == null || book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            System.out.println("Invalid book cannot be added.");
            return false;
        }
        for (LibraryBooks existing : books) {
            if (existing.getTitle().equalsIgnoreCase(book.getTitle())) {
                System.out.println(book.getTitle() + " already exists in the library.");
                return false;
            }
        }
        books.add(book);
        return true;
    }

    public void removeBook(LibraryBooks book) {
        books.remove(book);
    }

    public int getTotalBooks() {
        return books.size();
    }
    public static int getBorrowedBooks() {
        return borrowedbooks;
    }
    public static int getReturnedBooks() {
        return returnedbooks;
    }

    public LibraryBooks  searchBook(String title) {

        for (LibraryBooks b : books) {

            if (b.getTitle().equalsIgnoreCase(title)) {
                return b;
            }
        }

        return null;
    }

    public boolean borrowBook(String title, String borrowerName) {
        for (LibraryBooks book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                if (!book.isBorrowed()) {
                    book.setBorrowed(true);
                    book.setBorrowedby(borrowerName);
                    borrowedbooks++;
                    System.out.println(borrowerName + " borrowed \"" + title + "\"");
                    return true;
                } else {
                    System.out.println("Book is already borrowed by " + book.getBorrowedby());
                    return false;
                }
            }
        }
        System.out.println("Book not found: " + title);
        return false;
    }

    public boolean returnBook(String title, String returnerName) {
        for (LibraryBooks book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                if (!book.isBorrowed()) {
                    System.out.println("Book was not borrowed.");
                    return false;
                } else if (!book.getBorrowedby().equalsIgnoreCase(returnerName)) {
                    System.out.println("Return failed: " + returnerName + " did not borrow this book.");
                    return false;
                } else {
                    book.setBorrowed(false);
                    book.setBorrowedby(null);
                    returnedbooks++;
                    borrowedbooks--;
                    System.out.println(returnerName + " returned \"" + title + "\"");
                    return true;
                }
            }
        }
        System.out.println("Book not found: " + title);
        return false;
    }
    public void displayBooks() {

        if (books.isEmpty()) {
            System.out.println("No books in library");
        }

        else {

            for (LibraryBooks b : books) {
                System.out.println(b);
            }
        }
    }

    public void availableBooks() {

        for (LibraryBooks b : books) {

            if (!b.isBorrowed()) {
                System.out.println(b);
            }
        }
    }

    public void borrowedBooks() {

        for (LibraryBooks b : books) {

            if (b.isBorrowed()) {
                System.out.println(b);
            }
        }
    }
    public ArrayList<LibraryBooks> getBooks() {
        return books;
    }

    @Override
    public double calculateOperationalCost() {

        return maintenanceCost +
                (usageFrequency * 20) +
                (books.size() * 2) +
                (visitorsPerDay * 5);
    }

    @Override
    public String toString() {

        return (super.toString()
                + "\nTotal Books: " + books.size()
                + "\nVisitors Per Day: " + visitorsPerDay
                + "\nOperational Cost: " + calculateOperationalCost());
    }

public void generateReport() {
    System.out.println("Total Books:"+books.size()+"\n"+"Borrowed Books:"+borrowedbooks+"\n"+"Returned Books:"+returnedbooks);
    System.out.println("Visitors Per Day:"+ visitorsPerDay);
    System.out.println("Total Operational Cost:"+ calculateOperationalCost());
    System.out.println("=== Books Details ===");
    for (LibraryBooks b : books) {
    System.out.println(b.toString());
    System.out.println();
    }
}
}

