package steps;

import java.util.List;
import java.util.Map;

import com.example.demo2.Book;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

public class MyStepdefs2 {
    Response response;

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

    @Then("The library should have total {int} books")
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

        given()
                .contentType(ContentType.JSON)
                .body(book)
                .when()
                .put("/books/" + jsonPathGatsby.get("id"));
    }

    @Then("I get the books with following info")
    public void theBookIsUpdatedWithFollowingInfo(DataTable dataTable) {
        List<Map<String, String>> books = dataTable.asMaps();
        JsonPath jsonPath = new JsonPath(response.body().asString());
        if (books.size() == 1) {
            assertEquals(jsonPath.get("title"), books.get(0).get("Name"));
            assertEquals(jsonPath.get("author"), books.get(0).get("Author"));
            assertEquals(jsonPath.get("bookYear"), books.get(0).get("Year"));
            assertEquals(jsonPath.get("available").toString(), books.get(0).get("Available"));
            assertEquals(jsonPath.get("id").toString(), books.get(0).get("ID"));
        } else {
            for (int i = 0; i < books.size(); i++) {
                assertEquals(jsonPath.getList("title").get(i), books.get(i).get("Name"));
                assertEquals(jsonPath.getList("author").get(i), books.get(i).get("Author"));
                assertEquals(jsonPath.getList("bookYear").get(i), books.get(i).get("Year"));
                assertEquals(jsonPath.getList("available").get(i).toString(), books.get(i).get("Available"));
                assertEquals(jsonPath.getList("id").get(i).toString(), books.get(i).get("ID"));
            }
        }
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

    @Test
    @Given("Books are added to the library with following data")
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
        response = RestAssured
                .given()
                .when()
                .get("/books/" + id);

        response
                .then()
                .statusCode(200);
    }

    @When("I delete all books in the library")
    public void iDeleteAllBooksInTheLibrary() {
        response = given()
                .when()
                .delete("/books/deleteAll");
    }

    @When("I delete book with id {int}")
    public void iDeleteBookWithId(int id) {
        response = given()
                .when()
                .delete("/books/" + id);

    }

    @And("I get the list of all available books in the library")
    public void listAllAvailableBooks() {
        response = given()
                .when()
                .get("/books/list");

        response
                .then()
                .statusCode(200);
    }
}

