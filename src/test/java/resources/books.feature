Feature: Library with books

  Scenario: add book to library
    When 3 books are added to the library
    Then the library should have total 3 books

  Scenario: update book info in the library
    Given 2 books are added to the library
    When I update book with following info
      | Name      | War and Peace |
      | Author    | Tolstoy       |
      | Year      | 1901          |
      | Available | 100           |
    Then the book is updated with following info
      | Name      | War and Peace |
      | Author    | Tolstoy       |
      | Year      | 1901          |
      | Available | 100           |

#  Scenario: List all books in the library
#    Given the library has the following books
#      | Name          | Author      | Year | Available |
#      | War and Peace | Lev Tolstoy | 1901 | 2         |
#    When I request a list of all books
#    Then I should see the following books
#      | Name          | Author      | Year | Available |
#      | War and Peace | Lev Tolstoy | 1901 | 2         |


  Scenario : List a book by its id
    Given the library has the following books
      | Name             | Author              | Year | Available |
      | War and Peace    | Lev Tolstoy         | 1901 | 2         |
      | The Great Gatsby | F. Scott Fitzgerald | 1912 | 5         |
    When I request the book with name War and Peace
    Then I should see the book with name War and Peace and Author "Lev Tolstoy"


