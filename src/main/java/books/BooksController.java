package books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class BooksController {

    @Autowired
    private BooksRepository booksRepository;

    @GetMapping("/books")
    public String booksMain(Model model) {
        Iterable<Books> books = booksRepository.findAll();
        model.addAttribute("books", books);
        return "list-of-books";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("books", "Main page");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("books", "About");
        return "about";
    }

    @GetMapping("/books-add")
    public String booksAdd(Model model) {
        return "books-add";
    }

    @PostMapping("/books-add")
    public String bookPostadd(@RequestParam String name, @RequestParam String genre, @RequestParam String url, @RequestParam String description, Model model) {
        Books books = new Books(name, genre, url, description);
        booksRepository.save(books);
        return"redirect:/books";
    }

    @GetMapping("/books/{id}")
    public String bookInfo(@PathVariable(value = "id") Integer id, Model model) {
        if(!booksRepository.existsById(id)) {
            return"redirect:/books";
        }
        Optional<Books> book = booksRepository.findById(id);
        ArrayList<Books> res = new ArrayList<>();
        book.ifPresent(res::add);
        model.addAttribute("book", res);
        return "books-description";
    }

    @PostMapping("/books/{id}/delete")
    public String bookDelete(@PathVariable(value = "id") Integer id, Model model) {
        Books film = booksRepository.findById(id).orElseThrow(() -> new BookNotExistException(id));
        booksRepository.delete(film);
        return"redirect:/books";
    }

    @GetMapping("/books/{id}/edit")
    public String filmEdit(@PathVariable(value = "id") Integer id, Model model) {
        if(!booksRepository.existsById(id)) {
            return"redirect:/books";
        }

        Optional<Books> book = booksRepository.findById(id);
        ArrayList<Books> res = new ArrayList<>();
        book.ifPresent(res::add);
        model.addAttribute("book", res);
        return "books-edit";
    }

    @PostMapping("/books/{id}/edit")
    public String bookPostUpdate(@PathVariable(value = "id") Integer id, @RequestParam String name, @RequestParam String genre, @RequestParam String url, @RequestParam String description, Model model) {
        Books books = booksRepository.findById(id).orElseThrow(() -> new BookNotExistException(id));
        books.setName(name);
        books.setGenre(genre);
        books.setUrl(url);
        books.setDescription(description);
        booksRepository.save(books);
        return"redirect:/books";
    }
}