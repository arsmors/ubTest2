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
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

public class MyStepdefs {
    BookController bookController;
    Response responsePost;
    Response responsePut;
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
    public void iUpdateBookWithFollowingInfo(DataTable dataTable) {
        List<Map<String, String>> books = dataTable.asMaps();

        Book book = new Book();
        book.setTitle(books.get(0).get("Name"));
        book.setAuthor(books.get(0).get("Author"));
        book.setBookYear(books.get(0).get("Year"));
        book.setAvailable(Integer.parseInt(books.get(0).get("Available")));

        String responseBodyGatsby = responsePost.getBody().asString();
        JsonPath jsonPathGatsby = new JsonPath(responseBodyGatsby);

        responsePut = given()
                .contentType(ContentType.JSON)
                .body(book)
                .when()
                .put("/books/" + jsonPathGatsby.get("id"));
    }

    @Then("I get the book with following info")
    public void theBookIsUpdatedWithFollowingInfo(DataTable dataTable) {
        List<Map<String, String>> books = dataTable.asMaps();
        JsonPath jsonPath;
        if (responsePut ==null) {
            jsonPath = new JsonPath(responsePost.body().asString());
        } else {
            jsonPath = new JsonPath(responsePut.body().asString());
        }
        assertEquals(jsonPath.get("title"), books.get(0).get("Name"));
        assertEquals(jsonPath.get("author"), books.get(0).get("Author"));
        assertEquals(jsonPath.get("bookYear"), books.get(0).get("Year"));
        assertEquals(jsonPath.get("available").toString(), books.get(0).get("Available"));
        assertEquals(jsonPath.get("id").toString(), books.get(0).get("ID"));
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
        responsePost =  RestAssured
                .given()
                .when()
                .get("/books/" + id);

        responsePost
                .then()
                .statusCode(200);
    }
}

