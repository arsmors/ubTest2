-- Define the schema for the 'book' table
CREATE TABLE book (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    available INT NOT NULL,
    bookYear VARCHAR(4) -- Assuming the 'book_year' is a 4-character string
);
