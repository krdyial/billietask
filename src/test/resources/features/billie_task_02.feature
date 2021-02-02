@smoke
  Feature: Billie_Task_02
    Scenario: Creation of a new booking
      Given user creates a request body
      When user posts the request body to the endpoint
      And user validates status code returns 200
      And user validates the data in response and request body
      Then user validates bookingid field with non-null value


    Scenario Outline: Update created booking
      Given user creates an Auth token
      And user creates a new  body and updates  "<firstname>","<lastname>" and "<additionalneeds>"
      When user puts the updated body
      And user validates the status code 200
      Then  user validates the data in response and updated body

      Examples: Update response body
        |firstname       |lastname   | additionalneeds |
        |Lebron          |James      | Ball            |

    Scenario: Get the created booking
      Given user sends a get request with created id
      And user validates the status code is 200
      Then user validates the request body and the updated data


    Scenario: Delete the created booking
      Given user sends a delete request to the endpoint
      When user validates the status code returns 201
      And user sends a get request
      Then user validates the last status code returns 404


