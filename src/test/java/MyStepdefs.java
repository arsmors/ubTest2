import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.demo2.Book;
import com.example.demo2.BookController;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;

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
                .delete("/books/deleteAll");

        given()
                .when()
                .get("/books/list")
                .then().body("size()", is(0));
    }

    @When("{int} books are added to the library")
    public void booksAreAddedToTheLibrary(int qty) {
        Book book = new Book();
        Random random = new Random();
        for (int i = 0; i < qty; i++) {
            book.setTitle("Name" + RandomStringUtils.randomNumeric(10));
            book.setAuthor("Author" + RandomStringUtils.randomNumeric(10));
            book.setBookYear("19" + RandomStringUtils.randomNumeric(2));
            book.setAvailable(random.nextInt(99));
            Response response = given()
                    .contentType(ContentType.JSON)
                    .body(book)
                    .when()
                    .post("/books/add");

            response
                    .then()
                    .statusCode(201);

            bookId = response.then().extract().path("id");
        }
    }


    @Then("the library should have total {int} books")
    public void theLibraryShouldHaveTotalBooks(int qty) {
        RestAssured
                .given()
                .when()
                .get("/books/list")
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
        book.setBookYear(requestFields.get("Year"));
        book.setAvailable(Integer.parseInt(requestFields.get("Available")));
        Response response = RestAssured
                .given()
                .when()
                .get("/books/" + bookId);

        assertEquals(response.then().extract().path("title"), book.getTitle());
        assertEquals(response.then().extract().path("author"), book.getAuthor());
        assertEquals(response.then().extract().path("bookYear"), book.getBookYear());
        assertEquals(response.then().extract().path("available").toString(), requestFields.get("Available"));
    }

    @Given("the library is empty")
    public void theLibraryIsEmpty() {
        RestAssured
                .given()
                .when()
                .get("/books/list")
                .then()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Given("books are added to the library with following data")
    public void booksAreAddedToTheLibraryWithFollowingData(DataTable dataTable) {
        List<Map<String, String>> books = dataTable.asMaps();

        for (Map<String, String> bookData : books) {
            Book book = new Book();
            book.setTitle(bookData.get("Name"));
            book.setAuthor(bookData.get("Author"));
            book.setBookYear(bookData.get("Year"));
            book.setAvailable(Integer.parseInt(bookData.get("Available")));

            Response response = given()
                    .contentType(ContentType.JSON)
                    .body(book)
                    .when()
                    .post("/books/add");

            response
                    .then()
                    .statusCode(201);
        }
    }

    @When("I request the book with id {int}")
    public void iRequestTheBookWithId(int id) {
        RestAssured
                .given()
                .when()
                .get("/books/" + id)
                .then()
                .statusCode(200);
    }
}

