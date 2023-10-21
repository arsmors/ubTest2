Feature: Library with books

  Scenario: Add books to library
    Given Books are added to the library with following data
      | Name             | Author              | Year | Available |
      | War and Peace    | Lev Tolstoy         | 1901 | 2         |
      | The Great Gatsby | F. Scott Fitzgerald | 1912 | 5         |
      | Dracula          | Bram Stoker         | 1897 | 1         |
    When I get the list of all available books in the library
    Then I get the books with following info
      | Name             | Author              | Year | Available | ID |
      | War and Peace    | Lev Tolstoy         | 1901 | 2         | 1  |
      | The Great Gatsby | F. Scott Fitzgerald | 1912 | 5         | 2  |
      | Dracula          | Bram Stoker         | 1897 | 1         | 3  |
    And The library should have total 3 books

  Scenario: Get a book by ID
    Given Books are added to the library with following data
      | Name             | Author              | Year | Available |
      | War and Peace    | Lev Tolstoy         | 1901 | 2         |
      | The Great Gatsby | F. Scott Fitzgerald | 1912 | 5         |
      | Dracula          | Bram Stoker         | 1897 | 1         |
    When I request the book with id 2
    Then I get the books with following info
      | Name             | Author              | Year | Available | ID |
      | The Great Gatsby | F. Scott Fitzgerald | 1912 | 5         | 2  |

  Scenario: Update book info in the library
    Given Books are added to the library with following data
      | Name             | Author              | Year | Available |
      | War and Peace    | Lev Tolstoy         | 1901 | 2         |
      | The Great Gatsby | F. Scott Fitzgerald | 1912 | 5         |
      | Dracula          | Bram Stoker         | 1897 | 1         |
    When I request the book with id 2
    And I update book with following info
      | Name                   | Author         | Year | Available |
      | The Catcher in the Rye | J. D. Salinger | 1951 | 100       |
    And I get the list of all available books in the library
    Then I get the books with following info
      | Name                   | Author         | Year | Available | ID |
      | War and Peace          | Lev Tolstoy    | 1901 | 2         | 1  |
      | The Catcher in the Rye | J. D. Salinger | 1951 | 100       | 2  |
      | Dracula                | Bram Stoker    | 1897 | 1         | 3  |

  Scenario: Delete all books from the library
    Given Books are added to the library with following data
      | Name             | Author              | Year | Available |
      | War and Peace    | Lev Tolstoy         | 1901 | 2         |
      | The Great Gatsby | F. Scott Fitzgerald | 1912 | 5         |
      | Dracula          | Bram Stoker         | 1897 | 1         |
    When I delete all books in the library
    Then The library should have total 0 books

  Scenario: Delete books by id
    Given Books are added to the library with following data
      | Name             | Author              | Year | Available |
      | War and Peace    | Lev Tolstoy         | 1901 | 2         |
      | The Great Gatsby | F. Scott Fitzgerald | 1912 | 5         |
      | Dracula          | Bram Stoker         | 1897 | 1         |
    When I delete book with id 2
    And I get the list of all available books in the library
    Then I get the books with following info
      | Name          | Author      | Year | Available | ID |
      | War and Peace | Lev Tolstoy | 1901 | 2         | 1  |
      | Dracula       | Bram Stoker | 1897 | 1         | 3  |


