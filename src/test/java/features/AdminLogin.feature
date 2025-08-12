Feature: Login Page Feature
  This Feature deals with all the login components in the application
  #screenshot for every test
  #follow template
  #create different project file

  #TC001
  @Try
  Scenario: Login with valid email and password
    Given user is on the login page
    And the email is "aininafisah@altecflex.com"
    And the password is "Tester@123"
    When user click login
    Then user can see the dashboard

  #TC002
  @TC002
  Scenario: Login with Remember Me checked
    Given user is on the login page
    And the email is "aininafisah@altecflex.com"
    And the password is "Tester@123"
    And user check Remember Me
    When user click login
    Then user can see the dashboard

  #TC003
  @Test
  Scenario: Pressing Enter after entering valid credentials logs in successfully.
    Given user is on the login page
    And the email is "aininafisah@altecflex.com"
    When user click Enter
    Then user can see the dashboard

  #TC004

  Scenario: Password is visible after clicking the eye icon
    Given user is on the login page
    And the email is "aininafisah@altecflex.com"
    And the password is "Tester@123"
    When user click the eye icon
    Then user can view the password

  #TC005
  Scenario: Click on Forgot Password
    Given user is on the login page
    When user click on Forgot Password button
    Then user is redirected to password recovery page

  #TC006
  Scenario: Remember me functionality retains credentials on next visit.
    Given user is on the login page
    And the email is "aininafisah@altecflex.com"
    And the password is "Tester@123"
    And user check Remember Me
    When user click login
    And user can see the dashboard
    Then user open new browser
    And user can see existing dashboard

  #TC007

  Scenario: All elements are aligned and visible on various screen sizes
    Given user is on the login page
    When minimise the page size
    And the email is "aininafisah@altecflex.com"
    And the password is "Tester@123"
    Then contents are visible


  #TC008
  Scenario: Placeholder texts are displayed correctly
    Given user is on the login page
    Then the placeholders for email and password should be "Email Address" and "Password"

  #TC009
  Scenario: Login with blank email and password
    Given user is on the login page
    When user click login
    Then page should show warning

  #TC010
  Scenario: Login with blank email and correct password
    Given user is on the login page
    And the password is "Tester@123"
    When user click login
    Then email input box should show warning

  #TC011
  @NewTC
  Scenario: Login with correct email and blank password
    Given user is on the login page
    And the email is "aininafisah@altecflex.com"
    When user click login
    Then password input box should show warning

  #TC012
  Scenario: Login with invalid email format
    Given user is on the login page
    And the email is "aininafisah@"
    And the password is "Tester@123"
    When user click login
    Then page should show email warning

    #TC013
  Scenario: Login with trailing whitespaces in email
    Given user is on the login page
    And the email is "aininafisah@ altecflex.com"
    And the password is "Tester@123"
    When user click login
    Then page should show email warning

    #TC014
  Scenario: Leading and trailing whitespaces in password
    Given user is on the login page
    And the email is "aininafisah@altecflex.com"
    And the password is "Tester@  123"
    And user click the eye icon
    And user can view the password
    When user click login
    Then page should show credential warning

    #TC015
  Scenario: Incorrect password with valid email
    Given user is on the login page
    And the email is "aininafisah@altecflex.com"
    And the password is "Tester@"
    And user click the eye icon
    And user can view the password
    When user click login
    Then page should show credential warning

    #TC016
  Scenario: Non-registered email
    Given user is on the login page
    And the email is "hello@outlook.com"
    And the password is "Tester@123"
    When user click login
    Then page should show credential warning

    #TC017
  Scenario: Login with long emails or passwords
    Given user is on the login page
    And the email is "awtestingbot123456789132456789@outlook.com"
    And the password is "Tester@123456789123456789"
    And user click the eye icon
    And user can view the password
    When user click login
    Then page should show credential warning

    #TC018
  Scenario: Pressing Login button repeatedly quickly
    Given user is on the login page
    And the email is "aininafisah@altecflex.com"
    And the password is "Tester@123"
    When user click login repeatedly
    Then user can see the dashboard


    #TC019
  Scenario: Login with all CAPITAL LETTER email and correct password
    Given user is on the login page
    And the email is "AININAFISAH@ALTECFLEX.COM"
    And the password is "Tester@123"
    And user click the eye icon
    And user can view the password
    When user click login
    Then page should show credential warning


    #TC020
  Scenario: Login with all CAPITAL LETTER email and wrong password
    Given user is on the login page
    And the email is "AININAFISAH@ALTECFLEX.COM"
    And the password is "Tester@456"
    And user click the eye icon
    And user can view the password
    When user click login
    Then page should show credential warning

    #TC021
  @NewTC
  Scenario: Login with all CAPITAL LETTER email and CAPITAL LETTER password
    Given user is on the login page
    And the email is "AININAFISAH@ALTECFLEX.COM"
    And the password is "TESTER@123"
    And user click the eye icon
    And user can view the password
    When user click login
    Then page should show credential warning



















