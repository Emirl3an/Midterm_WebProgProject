package books;

public class BookNotExistException extends RuntimeException {
    public BookNotExistException(Integer id) {
        super("No such id exists " + id);
    }
}
