Feature: Library with books

  Scenario: add book to library
    Given the library is empty
    When 3 books are added to the library
    Then the library should have total 3 books

#    Examples:
#      | name1         | author1     | year1 | available1 | name2            | author2             | year2 | available2 |
#      | War and Peace | Lev Tolstoy | 1901  | 2          | The Great Gatsby | F. Scott Fitzgerald | 1912  | 5          |

  Scenario: Get a book by ID
    Given books are added to the library with following data
      | Name             | Author              | Year | Available |
      | War and Peace    | Lev Tolstoy         | 1901 | 2         |
      | The Great Gatsby | F. Scott Fitzgerald | 1912 | 5         |
    When I request the book with id 1
    Then the book is updated with following info
      | Name             | Author              | Year | Available |
      | War and Peace    | Lev Tolstoy         | 1901 | 2         |
      | The Great Gatsby | F. Scott Fitzgerald | 1912 | 5         |


  Scenario: update book info in the library
    Given the library is empty
    When 1 books are added to the library
    And I update book with following info
      | Name      | War and Peace |
      | Author    | Tolstoy       |
      | Year      | 1901          |
      | Available | 100           |
    Then the book is updated with following info
      | Name      | War and Peace |
      | Author    | Tolstoy       |
      | Year      | 1901          |
      | Available | 100           |


#    When I request the book with id 1
#    Then I should see the book with id 1 and name War and Peace and Author "Lev Tolstoy"

