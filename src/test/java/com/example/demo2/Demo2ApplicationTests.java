package com.example.demo2;

import java.util.Random;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;


@SpringBootTest
public class Demo2ApplicationTests {

	@BeforeEach
	public void setUp() {
		// Set the base URL for your application
		RestAssured.baseURI = "http://localhost:8080"; // Update with your application's base URL
	}

	@Test
	public void testAddBook() {
		// Create a JSON request body for adding a book
		String requestBody = "{"
				+ "\"title\": \"Sample Book\","
				+ "\"author\": \"Sample Author\","
				+ "\"available\": 1,"
				+ "\"year\": \"2023\""
				+ "}";

		given()
				.contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post("/books/add");
	}

	@Test
	public void addBook() {
		Book book = new Book();
		Random random = new Random();
		book.setTitle("Name"+ RandomStringUtils.randomNumeric(10));
		book.setAuthor("Author"+RandomStringUtils.randomNumeric(10));
		book.setBookYear("19"+RandomStringUtils.randomNumeric(2));
		book.setAvailable(random.nextInt(99));
		given()
				.contentType(ContentType.JSON)
				.body(book)
				.when()
				.post("/books/add");
	}
}