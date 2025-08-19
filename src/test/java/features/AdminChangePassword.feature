Feature: Change Password for Admin

      #IHP-61-001
  Scenario: Click change password button
    Given User logs in to the HR portal using "testnini@mailsac.com" and "Tester@123" credentials
    And User navigate to profile
    When User click change password
    Then Change password tab should be shown

      #IHP-61-002
  @IHP-61-002
  Scenario Outline: Successfully change password with valid current password and a new password meeting all rules.
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password <currentPassword>
    And User enter valid new password <newPassword>
    And User confirm new password <confirmNewPassword>
    When User click change password button
    Then OTP tab should display
    And User open email with <emailUsername> and <emailPassword>
    And User retrieve OTP
    And User filled the OTP
    And User successfully change password
    Examples:
      | email                  | password     | currentPassword | newPassword  | confirmNewPassword | emailUsername | emailPassword     |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@123"    | "Tester@456" | "Tester@456"       | "nini"        | "uzBcAvf292#z8Wc" |

      #IHP-61-003
  @IHP-61-003
  Scenario Outline: Confirm password matches the new password
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password <currentPassword>
    And User enter valid new password <newPassword>
    And User confirm new password <confirmNewPassword>
    When User click Eye Icon
    Then User click change password button

    Examples:
      | email                  | password     | currentPassword | newPassword  | confirmNewPassword |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@123"    | "Tester@456" | "Tester@456"       |

      #IHP-61-004
  @IHP-61-004
  Scenario Outline: Change password with the "eye icon" toggled to show/hide password fields.
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password <currentPassword>
    And User enter valid new password <newPassword>
    And User confirm new password <confirmNewPassword>
    When User click Eye Icon
    Then User click change password button

    Examples:
      | email                  | password     | currentPassword | newPassword  | confirmNewPassword |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@123"    | "Tester@456" | "Tester@456"       |

      #IHP-61-005
  @IHP-61-005
  Scenario Outline: Clicking Cancel closes the modal without saving changes
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password <currentPassword>
    And User enter valid new password <newPassword>
    And User confirm new password <confirmNewPassword>
    And User click Eye Icon
    When User click Cancel button
    Then Tab should be close without saving changes

    Examples:
      | email                  | password     | currentPassword | newPassword  | confirmNewPassword |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@123"    | "Tester@456" | "Tester@456"       |

      #IHP-61-006
  Scenario Outline: Validation messages change colour when the new password has at least 8 characters
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password <currentPassword>
    When User enter valid new password <newPassword>
    And User click Eye Icon
    Then The password rule "Use at least 8 characters" should be "active"

    Examples:
      | email                  | password     | currentPassword | newPassword  |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@123"    | "Tester@456" |

      #IHP-61-007
  @IHP-61-007
  Scenario Outline: Validation messages change colour when the new password has at least 1 uppercase
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password <currentPassword>
    When User enter valid new password <newPassword>
    And User click Eye Icon
    Then The password rule "Should contain at least 1 uppercase" should be "active"

    Examples:
      | email                  | password     | currentPassword | newPassword  |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@123"    | "Tester" |

      #IHP-61-008
  @IHP-61-008
  Scenario Outline: Validation messages change colour when the new password has at least 1 numeric
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password <currentPassword>
    And User enter valid new password <newPassword>
    And User confirm new password <confirmPassword>
    When User click Eye Icon
    Then The password rule "Should contain at least 1 numeric" should be "active"

    Examples:
      | email                  | password     | currentPassword | newPassword  | confirmPassword |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@123"    | "Tester456"  | "Tester456"      |

      #IHP-61-009
  @IHP-61-009
  Scenario Outline: Validation messages change colour when the new password has at least 1 special character
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password <currentPassword>
    When User enter valid new password <newPassword>
    And User confirm new password <confirmPassword>
    When User click Eye Icon
    Then The password rule "Should contain at least 1 special character" should be "active"

    Examples:
      | email                  | password     | currentPassword | newPassword  | confirmPassword |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@123"    | "Tester@456"  | "Tester@456"      |

      #IHP-61-010
  @IHP-61-010
  Scenario Outline: Error is shown if new password is the same as current password
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password <currentPassword>
    But New password equal to current password
    When User click Eye Icon
    Then The password rule "Cannot be same as previous password" should be "inactive"

    Examples:
      | email                  | password     | currentPassword | newPassword  | confirmNewPassword |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@123"    | "Tester@456" | "Tester@456"       |

      #IHP-61-011
  Scenario Outline: Validation messages change colour when the new password is different from current password
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password <currentPassword>
    When User enter valid new password <newPassword>
    Then The password rule "Cannot be same as previous password" should be "active"

    Examples:
      | email                  | password     | currentPassword | newPassword  |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@123"    | "Tester@456" |

      #IHP-61-012
  @IHP-61-012
  Scenario Outline: All password rules are validated in real-time while typing the new password
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User click Eye Icon
    And User enter current password <currentPassword>
    And User enter valid new password <newPassword>
    And All password rules should be:
      | Rule Text                                    | Expected State |
      | Use at least 8 characters                    | active         |
      | Should contain at least 1 uppercase          | active         |
      | Should contain at least 1 numeric            | active         |
      | Should contain at least 1 special character  | active         |
      | Cannot be same as previous password          | active         |
    When User confirm new password <confirmNewPassword>
    Then User click change password button
    And OTP tab should display

    Examples:
      | email                | password   | currentPassword | newPassword | confirmNewPassword |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@123"      | "Tester@456"  | "Tester@456"         |

      #IHP-61-013
  @IHP-61-013
  Scenario Outline:Change password button is enabled only when all conditions are met
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password <currentPassword>
    And User enter valid new password <newPassword>
    And User confirm new password <confirmPassword>
    When All password rules should be:
      | Rule Text                                    | Expected State |
      | Use at least 8 characters                    | active         |
      | Should contain at least 1 uppercase          | active         |
      | Should contain at least 1 numeric            | active         |
      | Should contain at least 1 special character  | active         |
      | Cannot be same as previous password          | active         |
    Then User click change password button
    And OTP tab should display

    Examples:
      | email                  | password     | currentPassword | newPassword  | confirmPassword |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@123"    | "Tester@456" | "Tester@456"     |

      #IHP-61-014
  @IHP-61-014
  Scenario Outline: Leave all fields blank should show validation errors
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password "<currentPassword>"
    And User enter valid new password "<newPassword>"
    When User confirm new password "<confirmNewPassword>"
    Then The Change Password button should be disabled

    Examples:
      | email                  | password     | currentPassword | newPassword  |  confirmNewPassword |
      | "testnini@mailsac.com" | "Tester@123" |                 |              |                     |

      #IHP-61-015
  @IHP-61-015
  Scenario Outline: Enter incorrect current password
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password <currentPassword>
    And User enter valid new password <newPassword>
    And User confirm new password <confirmNewPassword>
    And User click Eye Icon
    When User click change password button
    Then OTP tab should display
    And User open email with <emailUsername> and <emailPassword>
    And User retrieve OTP
    And User filled the OTP
    And User failed to change password

    Examples:
      | email                  | password     | currentPassword | newPassword  | confirmNewPassword | emailUsername | emailPassword     |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@1"      | "Tester@456" | "Tester@456"       | "nini"        | "uzBcAvf292#z8Wc" |

      #IHP-61-016
  @IHP-61-016
  Scenario Outline: New password has at less than 8 characters
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password <currentPassword>
    But <newPassword> does not meet the rules
    When User click Eye Icon
    Then The password rule "Use at least 8 characters" should be "inactive"

    Examples:
      | email                  | password     | currentPassword | newPassword  | confirmNewPassword |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@123"    | "Test" | "Tester@456"       |

      #IHP-61-017
  @IHP-61-017
  Scenario Outline: New password missing uppercase letter
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password <currentPassword>
    But <newPassword> does not meet the rules
    When User click Eye Icon
    Then The password rule "Should contain at least 1 uppercase" should be "inactive"

    Examples:
      | email                  | password     | currentPassword | newPassword  | confirmNewPassword |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@123"    | "test@123" | "Tester@456"       |

      #IHP-61-018
  @IHP-61-018
  Scenario Outline: New password missing numeric character
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password <currentPassword>
    But <newPassword> does not meet the rules
    When User click Eye Icon
    Then The password rule "Should contain at least 1 numeric" should be "inactive"

    Examples:
      | email                  | password     | currentPassword | newPassword  | confirmNewPassword |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@123"    | "tester@" | "Tester@456"       |

      #IHP-61-019
  @IHP-61-019
  Scenario Outline: New password missing special character
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password <currentPassword>
    But <newPassword> does not meet the rules
    When User click Eye Icon
    Then The password rule "Should contain at least 1 special character" should be "inactive"

    Examples:
      | email                  | password     | currentPassword | newPassword  | confirmNewPassword |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@123"    | "Tester123" | "Tester@456"       |

      #IHP-61-020
  @IHP-61-020
  Scenario Outline: Confirm password does not match new password
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password <currentPassword>
    And User enter valid new password <newPassword>
    And User confirm new password <confirmNewPassword>
    When User click Eye Icon
    Then The Change Password button should be disabled

    Examples:
      | email                  | password     | currentPassword | newPassword  | confirmNewPassword |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@123"    | "Tester@456" | "Tester@789"       |

      #IHP-61-021
  @IHP-61-021
  Scenario Outline: Try submitting form using Enter key
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password <currentPassword>
    And User enter valid new password <newPassword>
    And User confirm new password <confirmNewPassword>
    When User click Eye Icon
    Then User press Enter key
    And OTP tab should display

    Examples:
      | email                  | password     | currentPassword | newPassword  | confirmNewPassword |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@123"    | "Tester@456" | "Tester@456"       |

      #IHP-61-022
  @IHP-61-022
  Scenario Outline: User cannot enter a password longer than 20 characters
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password <currentPassword>
    When User enter valid new password <newPassword>
    And User click Eye Icon
    Then The new password field should contain only the first twenty characters
    And The Change Password button should be disabled

    Examples:
      | email                  | password     | currentPassword | newPassword                  |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@123"    | "ThisIsMoreThan20Characters" |

      #IHP-61-023
  @IHP-61-024
  Scenario Outline: Include spaces in passwords
    Given User logs in to the HR portal using <email> and <password> credentials
    And User navigate to profile
    And User click change password
    And User enter current password <currentPassword>
    And User enter valid new password <newPassword>
    And User confirm new password <confirmNewPassword>
    And User click Eye Icon
    When User click change password button
    Then The form should display warning

    Examples:
      | email                  | password     | currentPassword | newPassword    | confirmNewPassword |
      | "testnini@mailsac.com" | "Tester@123" | "Tester@123"    | "Tester @ 456" | "Tester @ 456"     |






