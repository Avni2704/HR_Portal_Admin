Feature: Allowance Management for Admin


  Scenario Outline: Verify that Employee can navigate to Allowance Management
    Given User logs in using <email> and <password> credentials
    And User click on Allowance Management
    And User can see Allowance Directory header
    And User click on Pending Approval
    And User can see Pending Approval header
    When User click on Claim History
    Then User can see Claim History header

    Examples:
      | email                  | password    |
      | "testnini@yopmail.com" | "Tester@456"|

  Scenario Outline: Verify that clicking view button redirect to the correct employee Claim Summary page and navigate through other tab
    Given User logs in using <email> and <password> credentials
    When User click on Allowance Management
    And User can see Allowance Directory header
    And an existing Allowance Directory is available for viewing
    And user click on view action button in Allowance Management
    And user should see the same employee name as header in Allowance Management
    And Claim Summary tab should be active
    And user click on Claim History tab
    And Claim History tab should be active
    And Pending Approval button should redirect to the employee pending approval leave list in Allowance Management

    Examples:
      | email                  | password    |
      | "testnini@yopmail.com" | "Tester@456"|

  Scenario Outline: Verify that admin able to reject single claim request and the request should disappear from the list
    Given User logs in using <email> and <password> credentials
    When User click on Allowance Management
    And User click on Pending Approval
    And User can see Pending Approval header
    And user click on action button
    And user click on the Reject Leave button in the side panel
    And reject reason modal should display
    And user input Reject Reason
    And user click on Cancel button and modal should be close
    Then user click on action button
    And user click on the Reject Leave button in the side panel
    And the textbox should save the reject reason as a draft
    And User click on Confirm button
    And all the selected entry should be removed from the list

    Examples:
      | email                  | password    |
      | "testnini@yopmail.com" | "Tester@456"|

  Scenario Outline: Verify that admin able to accept single claim request and the request should disappear from the list
    Given User logs in using <email> and <password> credentials
    When User click on Allowance Management
    And User click on Pending Approval
    And User can see Pending Approval header
    Then user click on action button
    And user click on the Approve Leave button in the side panel
    And system should display success toast message
    And all the selected entry should be removed from the list

    Examples:
      | email                  | password    |
      | "testnini@yopmail.com" | "Tester@456"|


  Scenario Outline: Verify that admin able to reject bulk claim request and the request should disappear from the list
    Given User logs in using <email> and <password> credentials
    When User click on Allowance Management
    And User click on Pending Approval
    And User can see Pending Approval header
    Then user can select multiple claim entry in Allowance Management
    And user can see selected count
    And user click on the Reject Leave button
    And reject reason modal should display
    And user input Reject Reason
    And user click on Cancel button and modal should be close
    And user click on the Reject Leave button
    And the textbox should save the reject reason as a draft
    And User click on Confirm button
    And all the selected entry should be removed from the list

    Examples:
      | email                  | password    |
      | "testnini@yopmail.com" | "Tester@456"|


  Scenario Outline: Verify that admin able to approve bulk claim request and the request should disappear from the list
    Given User logs in using <email> and <password> credentials
    When User click on Allowance Management
    And User click on Pending Approval
    And User can see Pending Approval header
    Then user can select multiple claim entry in Allowance Management
    And user can see selected count
    And user click on the Approve Leave button
    And system should display success toast message in Allowance Management
    And all the selected entry should be removed from the list

    Examples:
      | email                  | password    |
      | "testnini@yopmail.com" | "Tester@456"|


  Scenario Outline: TC060 Verify that clicking Filter button shows "Select Columns to Display" with all column checkboxes
    Given User logs in using <email> and <password> credentials
    And User click on Allowance Management
    And User can see Allowance Directory header
    When User click on Claim History
    And User can see Claim History header
    And User click on Filter button
    Then User should see filter prompt

    Examples:
      | email                  | password      |
      | "testnini@yopmail.com"  | "Tester@456"  |


  Scenario Outline: Verify that all checked columns are visible in the table behind the modal
    Given User logs in using <email> and <password> credentials
    And User click on Allowance Management
    And User can see Allowance Directory header
    When User click on Claim History
    And User can see Claim History header
    And User click on Filter button
    Then All checked column should visible in the table behind the modal in Allowance Management

    Examples:
      | email                   | password      |
      | "testnini@yopmail.com"  | "Tester@456"  |



  Scenario Outline: Verify toggling each column visibility updates the table correctly
    Given User logs in using <email> and <password> credentials
    And User click on Allowance Management
    And User can see Allowance Directory header
    When User click on Claim History
    And User can see Claim History header
    When User click on Filter button
    And User should see filter prompt
    Then User toggles <column> checkbox to <expectedState> in Allowance Management
    And The <column> column should be <expectedState> in the Allowance Management table

    Examples:
      | email                   | password      | column           | expectedState   |
      | "testnini@yopmail.com"  | "Tester@456"  |"Employee ID"     | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Employee ID"     | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Name"            | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Name"            | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Nickname"        | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Nickname"        | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Claim Type"      | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Claim Type"      | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Email"           | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Email"           | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Amount (RM)"     | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Amount (RM)"     | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Attachment"      | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Attachment"      | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Submit Date"     | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Submit Date"     | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Visit Date"      | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Visit Date"      | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Status"          | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Status"          | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Reject Reason"   | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Reject Reason"   | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Remarks"         | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Remarks"         | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Last Update by"  | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Last Update by"  | "visible"       |


  Scenario Outline: Filter table by field with value
    Given User logs in using <email> and <password> credentials
    And User click on Allowance Management
    And User can see Allowance Directory header
    And User click on Claim History
    And User can see Claim History header
    And User click on Filter button
    And User should see filter prompt
    And User toggles <column> checkbox to <expectedState>
    When User enters <value> in <field> in Allowance Management
    And User clicks Search
    Then The table should only show rows matching <field> with <value>

    Examples:
      | email                   | password    | field               | value                     |  column             | expectedState |
      |"testnini@yopmail.com"   | "Tester@456"| "Employee ID"       | "108"                     |  "Employee ID"      | "visible"     |
      |"testnini@yopmail.com"   | "Tester@456"| "Name"              | "athirah"                 |  "Name"             | "visible"     |
      |"testnini@yopmail.com"   | "Tester@456"| "Nickname"          | "nisa"                 |  "Nickname"             | "visible"     |
      #|"testnini@yopmail.com"   | "Tester@456"| "Employee Email"    | "awtestingbot@outlook.com"|  "Employee Email"   | "visible"     |

  # Verify Status dropdown

  Scenario Outline: TC-A078 Filter table by Status <status>
    Given User logs in using <email> and <password> credentials
    And User click on Allowance Management
    And User can see Allowance Directory header
    And User click on Claim History
    And User can see Claim History header
    And User click on Filter button
    And User should see filter prompt
    When User selects Status <status>
    And User clicks Search
    Then The table should only show rows matching <field> with <status>

    Examples:
      |email                 |password      | status       | field     |
      |"testnini@yopmail.com"| "Tester@456" | "Cancelled"  | "Status"  |
      |"testnini@yopmail.com"| "Tester@456" | "Approved"   | "Status"  |
      |"testnini@yopmail.com"| "Tester@456" | "Pending"    | "Status"  |
      |"testnini@yopmail.com"| "Tester@456" | "Rejected"   | "Status"  |

  Scenario Outline: TC-A078 Filter table by Status <status>
    Given User logs in using <email> and <password> credentials
    And User click on Allowance Management
    And User can see Allowance Directory header
    And User click on Claim History
    And User can see Claim History header
    And User click on Filter button
    And User should see filter prompt
    When User selects Claim Type <claimType>
    And User clicks Search
    Then The table should only show rows matching <field> with <claimType>

    Examples:
      |email                 | password     | claimType                 | field         |
      |"testnini@yopmail.com"| "Tester@456" | "Medical Claim"           | "Claim Type"  |
      |"testnini@yopmail.com"| "Tester@456" | "Optical / Dental Claim"  | "Claim Type"  |
      |"testnini@yopmail.com"| "Tester@456" | "Other Claim"             | "Claim Type"  |
      |"testnini@yopmail.com"| "Tester@456" | "Transport Claim"         | "Claim Type"  |
      |"testnini@yopmail.com"| "Tester@456" | "Office Supplies Claim"   | "Claim Type"  |



  Scenario Outline: TC-A0091 Filter table by Visit Date Range
    Given User logs in using <email> and <password> credentials
    And User click on Allowance Management
    And User can see Allowance Directory header
    And User click on Claim History
    And User can see Claim History header
    And User click on Filter button
    And User should see filter prompt
    When User sets Date From <dateFrom> and Date To <dateTo> in Allowance Management
    And User clicks Search
    Then The table should only show rows within the date range <dateFrom> to <dateTo> in Allowance Management

    Examples:
      |email                  | password    | dateFrom            | dateTo              |
      |"testnini@yopmail.com" | "Tester@456"| "20 November 2025"  | "05 December 2025"  |


  Scenario Outline: TC-A0091 Filter table by Submission Date Range
    Given User logs in using <email> and <password> credentials
    And User click on Allowance Management
    And User can see Allowance Directory header
    And User click on Claim History
    And User can see Claim History header
    And User click on Filter button
    And User should see filter prompt
    When User sets Submission Date From <dateFrom> and Submission Date To <dateTo> in Allowance Management
    And User clicks Search
    Then The table should only show rows within the date range <dateFrom> to <dateTo> in Allowance Management

    Examples:
      |email                  | password    | dateFrom            | dateTo              |
      |"testnini@yopmail.com" | "Tester@456"| "20 November 2025"  | "05 December 2025"  |

  Scenario Outline: Search applies criteria and Clear resets fields
    Given User logs in using <email> and <password> credentials
    And User click on Allowance Management
    And User can see Allowance Directory header
    And User click on Claim History
    And User can see Claim History header
    And User click on Filter button
    And User should see filter prompt
    And User enters <value> in <field> in Allowance Management
    And User selects Status <status>
    And User selects Claim Type <claimType>
    And User sets Date From <dateFrom> and Date To <dateTo> in Allowance Management
    And User clicks Search
    And The table should only show rows matching <field> with <value>
    And The table should only show rows within the date range <dateFrom> to <dateTo> in Allowance Management
    And User click on Filter button
    And User should see filter prompt
    When User clicks Clear
    Then All filter fields should be empty or default in Allowance Management
    And User closes the filter modal
    And The table should show all rows

    Examples:
      |email                  | password    | value                 | field                       |status         | claimType     | dateFrom            | dateTo              |
      |"testnini@yopmail.com" | "Tester@456"| "303,athirah,puteri"  | "Employee ID,Name,Nickname" | "Approved"    | "OTHER_CLAIM"| "20 November 2025"  | "05 March 2026"  |



  Scenario Outline: Search shows friendly "No results" state
    Given User logs in using <email> and <password> credentials
    And User click on Allowance Management
    And User can see Allowance Directory header
    And User click on Claim History
    And User can see Claim History header
    And User click on Filter button
    And User should see filter prompt
    When User enters <value> in <field>
    And User clicks Search
    Then The table should show No results message in Allowance Management

    Examples:
      |email                  | password    | value            | field            |
      |"testnini@yopmail.com" | "Tester@456"| "fifi"           | "Employee Name"  |


  Scenario Outline: Pressing Enter triggers Search
    Given User logs in using <email> and <password> credentials
    And User click on Allowance Management
    And User can see Allowance Directory header
    And User click on Claim History
    And User can see Claim History header
    When User click on Filter button
    And User should see filter prompt
    And User selects Status <status>
    And User sets Date From <dateFrom> and Date To <dateTo> in Allowance Management
    And User enters <value> in <field> in Allowance Management
    Then User click on ENTER key
    And The table should only show rows matching <field> with <value>
    And The table should only show rows within the date range <dateFrom> to <dateTo> in Allowance Management

    Examples:
      |email                  | password    | value      | field  | dateFrom            | dateTo              | status    |
      |"testnini@yopmail.com" | "Tester@456"| "athirah"  | "Name" | "20 November 2025"  | "05 March 2026"  | "Pending" |

  @Test
  Scenario Outline: Verify that Export respects the filtered data and visible columns
    Given User logs in using <email> and <password> credentials
    And User click on Allowance Management
    And User can see Allowance Directory header
    And User click on Claim History
    And User can see Claim History header
    When User click on Filter button
    And User should see filter prompt
    And User selects Status <status>
    And User sets Date From <dateFrom> and Date To <dateTo> in Allowance Management
    And User enters <value> in <field> in Allowance Management
    And User clicks Search
    Then admin click export button
    And exported file should contain only filtered data and visible columns in Allowance Management

    Examples:
      |email                  | password    | value      | field  | dateFrom            | dateTo              | status      |
      |"testnini@yopmail.com" | "Tester@456"| "nisa"  | "Name" | "20 November 2025"  | "05 March 2026"     | "Approved"  |

