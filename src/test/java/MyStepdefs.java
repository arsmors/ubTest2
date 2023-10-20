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

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

public class MyStepdefs {
    BookController bookController;
    Response response;
    Response responsePut;

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

    @When("I update book with following info")
    public void iUpdateBookWithFollowingInfo(DataTable dataTable) {
        List<Map<String, String>> books = dataTable.asMaps();

        Book book = new Book();
        book.setTitle(books.get(0).get("Name"));
        book.setAuthor(books.get(0).get("Author"));
        book.setBookYear(books.get(0).get("Year"));
        book.setAvailable(Integer.parseInt(books.get(0).get("Available")));

        String responseBodyGatsby = response.getBody().asString();
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
            jsonPath = new JsonPath(response.body().asString());
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

            response = given()
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
        response =  RestAssured
                .given()
                .when()
                .get("/books/" + id);

        response
                .then()
                .statusCode(200);
    }
}

