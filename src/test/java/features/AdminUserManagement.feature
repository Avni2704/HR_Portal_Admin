Feature: Admin User Management (Create(Onboard new employee) Read(Filter and Export) Update(Edit Profile) Delete(Remove admin access)

  Background:
    Given User logs in using 'awtestingbot@outlook.com' and 'Perfume@123' credentials
    And User click on User Management
    And User can see Employee header

  Scenario Outline: Verify that Admin able to onboard a new employee and reflect on the new employee page
    When admin click Invite button
    And admin generates a new dummy email
    And admin fill personal info section <fullName>, <idType>, <idInfo>, <mobileNo>, <dobYear>, <dobMonth>, <dobDay>, <gender>, <maritalStatus>
    And admin fill employee info section <joinDateYear>, <joinDateMonth>, <joinDateDay>, <probEndDateYear>, <probEndDateMonth>, <probEndDateDay>, <position>, <level>, <contractType>, <reportingManager>
    And admin fill bank details section <bankName>, <bankAccNo>, <swiftCodeBranchName>
    And admin fill contributions section <epfNo>, <incomeTaxNo>, <socsoNo>
    And admin click add button
    And a successful toast message should display
    And new employee check email
    And employee click on the attached link in the email
    And employee enter <password> and <confirmPassword>
    And employee click on Create Password button
    And new employee check and retrieve OTP
    Then User should redirect to hr portal
    And employee should be able to view dashboard
    And employee input onboarding <nickname>, <address1>, <address2>, <state1>, <region1>, <postcode1>, <emergencyName>, <emergencyRelationship>, <emergencyMobile>
    And employee fill health declaration and consent
    And employee click submit button


    Examples:
      | fullName     | idType     | idInfo    | mobileNo     | dobYear | dobMonth   | dobDay | gender | maritalStatus | joinDateYear | joinDateMonth | joinDateDay | probEndDateYear | probEndDateMonth | probEndDateDay | position | level     | contractType   | reportingManager | bankName  | bankAccNo    | swiftCodeBranchName | epfNo       | incomeTaxNo | socsoNo     | password | confirmPassword | nickname | address1 | address2 | state1 | region1 | postcode1 | emergencyName | emergencyRelationship | emergencyMobile |
      | "Automation Test [RandomUUID]" | "Passport" | "PASS[MathRand]" | "0123456798" | "2000"  | "January"  | "19"   | "Male" | "Single"      | "2024"       | "February"    | "1"         | "2024"          | "May"            |  "30"          | "Admin"  | "Manager" | "Intern"       | "ANIS"           | "MAYBANK" | "1234567890" | "MBBEMYKL"          | "145236987" | "785412369" | "125478963" | "Auto@123" | "Auto@123" | "Automation Nickname" | "Address 1" | "Address 2" | "Selangor" | "Shah Alam" | "40582" | "Automation Emergency" | "Spouse" | "1137384373" |

  Scenario Outline: Verify that Admin able to filter and export
    When User click on Filter button
    And User should see filter prompt
    And User selects level <level> in user management
    #And User sets Date From <dateFrom> and Date To <dateTo> in Allowance Management
    #And User enters <value> in <field> in Allowance Management
    And User clicks Search
    Then admin click export button
    And exported file should contain only filtered data and visible columns in User Management

    Examples:
      | value   | field  | dateFrom            | dateTo              | level      |
      | "nisa"  | "Name" | "20 November 2025"  | "05 March 2026"     | "Director"  |

  Scenario Outline: Verify that Admin able to edit employee personal profile and update the page in real time
    When user click on action button on Employee table
    And admin edit <fullName>, <nickname>, <residentialAddress1>, <residentialAddress2>, <state>, <region>, <postcode>, <emergencyFullName>, <emergencyRelationship>, <emergencyMobile>
    Then admin click Save button
    And update successful toast message display
    And page should update to the latest input

    Examples:
      | fullName | nickname | residentialAddress1 | residentialAddress2 | state | region | postcode | emergencyFullName | emergencyRelationship | emergencyMobile |
      | "Automate Edit Full Name [RandomUUID]" | "Automate Edit Nickname [RandomUUID]" | "Automate Edit Address 1 [RandomUUID]" | "Automate Edit Address 2 [RandomUUID]" | "Selangor" | "Shah Alam" | "40542" | "Automate Edit Emergency Name [RandomUUID]" | "Others" | "1137384373" |

  Scenario Outline: Verify that Admin able to edit employee info and update the page in real time
    When user click on action button on Employee table
    And admin click on the employee info tab
    And admin edit <position> and <level>
    Then admin click Save button
    And update successful toast message display
    And page should update the position and level

    Examples:
      | position| level     |
      | "Admin" | "Others"  |

  Scenario Outline: Verify that Admin able to upload, download and remove document and reflect on the table
    When user click on action button on Employee table
    And admin click on the document tab
    Then admin upload document <filePath>
    And admin download document
    And admin delete document

    Examples:
    | filePath |
    | "C:\Users\ainin\OneDrive\Documents\test-data\test-png.png" |

  Scenario Outline: Verify that Master Admin able to add and remove new admin
    When admin click on Admin List
    And admin click on Add New button
    And admin select <admin> from the dropdown
    Then click Confirm button
    And success toast message shall display
    And admin click on the action button at the end of a row for <admin>
    And display success toast message with <admin> name

    Examples:
    | admin                       |
    | "AUTOMATION TEST ECEBABAE"  |


