
Feature: 01
  Background: User is on the page
    Given user goes "https://restful-booker.herokuapp.com/booking/"

    Scenario: User puts
      When User put the "firstname" and "lastname" to the body
