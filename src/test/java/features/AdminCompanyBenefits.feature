Feature: Admin Company Benefit To ADD and UPDATE Job Position and To ADD and UPDATE Benefit Package


  Scenario Outline: Verify that user able to add a new job position
    Given User logs in using <email> and <password> credentials
    And User click on Company Benefit
    And User should see Job Position header
    When User click on Add New Position
    And User enter Position Name <positionName>
    And User enter Description <jobDesc>
    Then User click on Create button
    And User should see created Job Position on the page

    Examples:
      | email                  | password    | positionName                  | jobDesc               |
      | "testnini@yopmail.com" | "Tester@456"| "Automation Test [MathRand]"  | "Automation Testing"  |

  Scenario Outline: Verify that no Job Position created if user click on Cancel button
    Given User logs in using <email> and <password> credentials
    And User click on Company Benefit
    And User should see Job Position header
    When User click on Add New Position
    And User enter Position Name <positionName>
    And User enter Description <jobDesc>
    Then User click on Cancel button
    And Form modal should close and no Job Position should be created

    Examples:
      | email                  | password    | positionName                  | jobDesc               |
      | "testnini@yopmail.com" | "Tester@456"| "Automation Test [MathRand]"  | "Automation Testing"  |


  Scenario Outline: Verify user is able to edit an existing Job Position successfully
    Given User logs in using <email> and <password> credentials
    And User click on Company Benefit
    And User should see Job Position header
    And An existing Job Position is available for editing

    When User clicks on the Edit button for the Job Position
    And User clears the Position Name field
    And User enters a new valid Position Name <newPosition>
    And User clears the Description field
    And User enters a new valid Description <newDesc>
    And User clicks the Save button

    Then The edit Job Position modal should close
    And User should see a success message for Job Position update
    And The Job Position should be updated with the new details in the listing

    Examples:
      | email                     | password      | newPosition          | newDesc             |
      | "testnini@yopmail.com"    | "Tester@456"  | "Automation Update" | "Automation Update" |

  Scenario Outline: Verify user cannot update Job Position with empty mandatory fields
    Given User logs in using <email> and <password> credentials
    And User click on Company Benefit
    And An existing Job Position is available for editing

    When User clicks on the Edit button for the Job Position
    And User clears the Position Name field
    And User clicks the Save button

    Then User should see a validation error message for Position Name
    And The Job Position should not be updated

    Examples:
      | email                     | password      |
      | "testnini@yopmail.com"    | "Tester@456"  |

  Scenario Outline: Verify creating Job Position with same existing values
    Given User logs in using <email> and <password> credentials
    And User click on Company Benefit
    And An existing Job Position is available for editing

    When User click on Add New Position
    And User enters an existed Position Name <existPost>
    And User enter Description <jobDesc>
    And User click on Create button

    Then User should see a message indicating duplicate entry
    And User click on Cancel button
    And Form modal should close and no Job Position should be created

    Examples:
      | email                     | password      | jobDesc               |  existPost       |
      | "testnini@yopmail.com"    | "Tester@456"  | "Automation Testing"  |  "123456@#$%^&*" |


  Scenario Outline: Verify editing Job Position with same existing values
    Given User logs in using <email> and <password> credentials
    And User click on Company Benefit
    And An existing Job Position is available for editing

    When User clicks on the Edit button for the Job Position
    And User clears the Position Name field
    And User enters an existed Position Name <existPost>
    And User clicks the Save button

    Then User should see a message indicating duplicate entry
    And User click on Cancel button
    And The Job Position should not be updated

    Examples:
      | email                     | password      | existPost       |
      | "testnini@yopmail.com"    | "Tester@456"  | "123456@#$%^&*" |


    Scenario Outline: Verify that user able to create a new Benefit Package
      Given User logs in using <email> and <password> credentials
      And User click on Company Benefit
      And User click on Benefit Package
      And User should see Benefit Package header
      When User click on Add Benefit button
      And User should see New Package page
      And User input <data> into respecting fields <fieldsName>
      Then User click on update button
      And User should see New Package created on the Benefit Package page with correct data

      Examples:
        | email                  | password    | data                                                                                    | fieldsName             |
        | "testnini@yopmail.com" | "Tester@456"| "Automation Test Package,Automation Package Description,50,49,48,47,46,1000,1010,1020"  | "name,description,Annual Leave,Sick/Medical,Hospitalization,Maternity,Paternity,Medical Claim,Optical/ Dental,Others"|


    Scenario Outline: Verify that user able to add New Leave Type and New Claim Type and create a new Benefit Package
      Given User logs in using <email> and <password> credentials
      And User click on Company Benefit
      And User click on Benefit Package
      And User should see Benefit Package header
      When User click on Add Benefit button
      And User should see New Package page
      And User click on Add New Leave button
      And User select Leave <leaveType>
      And User click on Add New Claim button
      And User select Claim <claimType>
      And User input <data> into respecting fields <fieldsName>
      Then User click on update button
      And User should see New Package created on the Benefit Package page with correct data

      Examples:
        | email                  | password     | leaveType              | claimType                 | data                                                                                            | fieldsName     |
        | "testnini@yopmail.com" | "Tester@456" | "Carry Forward Leave"  | "Transport Claim"         | "Automation Test Package,Automation Package Description,50,49,48,47,46,47,1000,1010,1020,1050"  | "name,description,Annual Leave,Sick/Medical,Hospitalization,Maternity,Paternity,Carry Forward Leave,Medical Claim,Optical/ Dental,Others,Transport Claim"       |  
        | "testnini@yopmail.com" | "Tester@456" | "Marriage Leave"       | "Office Supplies Claim"   | "Automation Test Package,Automation Package Description,50,49,48,47,46,47,1000,1010,1020,1050"  | "name,description,Annual Leave,Sick/Medical,Hospitalization,Maternity,Paternity,Marriage Leave,Medical Claim,Optical/ Dental,Others,Office Supplies Claim"      |
        | "testnini@yopmail.com" | "Tester@456" | "Emergency Leave"      | "Transport Claim"         | "Automation Test Package,Automation Package Description,50,49,48,47,46,47,1000,1010,1020,1050"  | "name,description,Annual Leave,Sick/Medical,Hospitalization,Maternity,Paternity,Emergency Leave,Medical Claim,Optical/ Dental,Others,Transport Claim"           |
        | "testnini@yopmail.com" | "Tester@456" | "Unpaid Leave"         | "Office Supplies Claim"   | "Automation Test Package,Automation Package Description,50,49,48,47,46,47,1000,1010,1020,1050"  | "name,description,Annual Leave,Sick/Medical,Hospitalization,Maternity,Paternity,Unpaid Leave,Medical Claim,Optical/ Dental,Others,Office Supplies Claim"        |
        | "testnini@yopmail.com" | "Tester@456" | "Compassionate Leave"  | "Transport Claim"         | "Automation Test Package,Automation Package Description,50,49,48,47,46,47,1000,1010,1020,1050"  | "name,description,Annual Leave,Sick/Medical,Hospitalization,Maternity,Paternity,Compassionate Leave,Medical Claim,Optical/ Dental,Others,Transport Claim"       |
        | "testnini@yopmail.com" | "Tester@456" | "Replacement Leave"    | "Office Supplies Claim"   | "Automation Test Package,Automation Package Description,50,49,48,47,46,47,1000,1010,1020,1050"  | "name,description,Annual Leave,Sick/Medical,Hospitalization,Maternity,Paternity,Replacement Leave,Medical Claim,Optical/ Dental,Others,Office Supplies Claim"   |

  Scenario Outline: Verify that Benefit Package does not created when user click Cancel
      Given User logs in using <email> and <password> credentials
      And User click on Company Benefit
      And User click on Benefit Package
      And User should see Benefit Package header
      When User click on Add Benefit button
      And User should see New Package page
      And User click on Add New Leave button
      And User select Leave <leaveType>
      And User click on Add New Claim button
      And User select Claim <claimType>
      And User input <data> into respecting fields <fieldsName>
      Then User click on Cancel button
      And User should not see New Package created on the Benefit Package page

      Examples:
        | email                  | password     | leaveType              | claimType                 | data                                                                                            | fieldsName     |
        | "testnini@yopmail.com" | "Tester@456" | "Carry Forward Leave"  | "Transport Claim"         | "Automation Test Package,Automation Package Description,50,49,48,47,46,47,1000,1010,1020,1050"  | "name,description,Annual Leave,Sick/Medical,Hospitalization,Maternity,Paternity,Carry Forward Leave,Medical Claim,Optical/ Dental,Others,Transport Claim"       |

    @Test
  Scenario Outline: Verify that user able to edit Benefit Package
      Given User logs in using <email> and <password> credentials
      And User click on Company Benefit
      And User click on Benefit Package
      And User should see Benefit Package header
      And an existing Benefit Package is available for editing

      When the user clicks the Edit button for the Benefit Package
      And the user clears the Package Name field
      And the user enters a new valid Package Name <newPackage>
      And the user clears the Description field
      And the user enters a new valid Description <newDesc>
      And the user updates the Leave Types <leaveTypes> with Leave Days <leaveDays>
      And the user updates the Claim Types <claimTypes> with Claim Amounts <claimAmounts>

      Then the user clicks the Update button
      And the user should see a success message for Benefit Package update
      And the Benefit Package should be updated with the new details in the listing

      Examples:
        | email                  | password     | newPackage          | newDesc                  | leaveTypes            | leaveDays | claimTypes          | claimAmounts |
        | "testnini@yopmail.com" | "Tester@456" | "Automation Update" | "Automation Update Desc" | "Replacement Leave"   | "15"      | "Transport Claim"   | "120.50"     |
