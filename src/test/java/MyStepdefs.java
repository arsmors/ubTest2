import java.util.Map;

import com.example.demo2.Book;
import com.example.demo2.BookController;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

public class MyStepdefs {
    BookController bookController;
    private int bookId = 0;

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @After
    public void cleanUp() {
        given()
                .when()
                .delete("/books/all")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @When("{int} books are added to the library")
    public void booksAreAddedToTheLibrary(int qty) {
        Book book = new Book();
            for (int i = 0; i < qty; i++) {
                Response response = given()
                        .contentType(ContentType.JSON)
                        .body(book)
                        .when()
                        .post("/books");

                response
                        .then()
                        .statusCode(200);

                bookId = response.then().extract().path("id");
            }
    }

    @Then("the library should have total {int} books")
    public void theLibraryShouldHaveTotalBooks(int qty) {
        RestAssured
                .given()
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .body("size()", is(qty));
    }

    @Given("the library has the following books")
    public void theLibraryHasTheFollowingBooks(Map<String, String> requestFields) {
        Book book = new Book();
        book.setTitle(requestFields.get("Title"));
        book.setAuthor(requestFields.get("Author"));
        book.setBookYear(requestFields.get("bookYear"));
        book.setAvailable(Integer.parseInt(requestFields.get("Available")));

        given()
                .contentType(ContentType.JSON)
                .body(book)
                .when()
                .put("/books/" + bookId);

        }

    @When("I update book with following info")
    public void iUpdateBookWithFollowingInfo(Map<String, String> requestFields) {
        Book book = new Book();
        book.setTitle(requestFields.get("Name"));
        book.setAuthor(requestFields.get("Author"));
        book.setBookYear(requestFields.get("Year"));
        book.setAvailable(Integer.parseInt(requestFields.get("Available")));

        given()
                .contentType(ContentType.JSON)
                .body(book)
                .when()
                .put("/books/" + bookId);
    }

    @Then("the book is updated with following info")
    public void theBookIsUpdatedWithFollowingInfo(Map<String, String> requestFields) {
        Book book = new Book();
        book.setTitle(requestFields.get("Name"));
        book.setAuthor(requestFields.get("Author"));
        book.setTitle(requestFields.get("Year"));
        book.setAvailable(Integer.parseInt(requestFields.get("Available")));
        Response response = RestAssured
                .given()
                .when()
                .get("/books/" + bookId);

        assertEquals(response.then().extract().path("name"), book.getTitle());
        assertEquals(response.then().extract().path("author"), book.getAuthor());
        assertEquals(response.then().extract().path("year"), book.getBookYear());
        assertEquals(response.then().extract().path("available").toString(), requestFields.get("Available"));

    }
}