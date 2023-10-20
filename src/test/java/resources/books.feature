Feature: Library with books

  Scenario: add book to library
    Given the library is empty
    Given books are added to the library with following data
      | Name             | Author              | Year | Available |
      | War and Peace    | Lev Tolstoy         | 1901 | 2         |
      | The Great Gatsby | F. Scott Fitzgerald | 1912 | 5         |
      | Dracula          | Bram Stoker         | 1897 | 1         |
    Then the library should have total 3 books

  Scenario: Get a book by ID
    Given books are added to the library with following data
      | Name             | Author              | Year | Available |
      | War and Peace    | Lev Tolstoy         | 1901 | 2         |
      | The Great Gatsby | F. Scott Fitzgerald | 1912 | 5         |
      | Dracula          | Bram Stoker         | 1897 | 1         |
    When I request the book with id 2
    Then I get the book with following info
      | Name             | Author              | Year | Available | ID |
      | The Great Gatsby | F. Scott Fitzgerald | 1912 | 5         | 2  |

  Scenario: update book info in the library
    Given books are added to the library with following data
      | Name             | Author              | Year | Available |
      | War and Peace    | Lev Tolstoy         | 1901 | 2         |
      | The Great Gatsby | F. Scott Fitzgerald | 1912 | 5         |
      | Dracula          | Bram Stoker         | 1897 | 1         |
    When I request the book with id 2
    And I update book with following info
      | Name                   | Author         | Year | Available |
      | The Catcher in the Rye | J. D. Salinger | 1951 | 100       |
    Then I get the book with following info
      | Name                   | Author         | Year | Available | ID |
      | The Catcher in the Rye | J. D. Salinger | 1951 | 100       | 2  |


#    When I request the book with id 1
#    Then I should see the book with id 1 and name War and Peace and Author "Lev Tolstoy"

