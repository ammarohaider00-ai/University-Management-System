import java.io.Serializable;

class LibraryBooks implements Serializable {

    private String title;
    private String author;
    private boolean borrowed;
    private String borrowedby;

    public LibraryBooks() {
        title = null;
        author = null;
        borrowed = false;
        borrowedby = null;
    }

    public LibraryBooks(String title, String author, boolean borrowed) {

        this.title = title;
        this.author = author;
        this.borrowed = borrowed;
        this.borrowedby = null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isBorrowed() {
        return borrowed;
    }
    public void setBorrowedby(String borrowedby) {
        this.borrowedby = borrowedby;
    }
    public String getBorrowedby() {
        return borrowedby;
    }

    @Override
    public String toString() {

        return ("Title: " + title + "\n" + "Author: " + author + "\n" + "Borrowed by: " + borrowedby + "\n");
    }
}