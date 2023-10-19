Feature: Library with books

  Scenario: add book to library
    When 3 books are added to the library
    Then the library should have total 3 books

  Scenario: update book info in the library
    Given 1 books are added to the library
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

#  Scenario : List a book by its id
#    Given 1 books are added to the library
#      | Name      | War and Peace |
#      | Author    | Tolstoy       |
#      | Year      | 1901          |
#      | Available | 100           |
#    When I request the book with name War and Peace
#    Then I should see the book with name War and Peace and Author "Lev Tolstoy"

