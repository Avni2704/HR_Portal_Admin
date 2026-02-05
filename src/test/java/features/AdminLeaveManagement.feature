Feature: Leave Management for Admin

  Scenario Outline: Verify that Employee can navigate to Leave Management
    Given User logs in using <email> and <password> credentials
    And User click on Leave Management
    And User can see Leave Calendar header
    And User click on Leave Directory
    And User can see Leave Directory header
    And User click on Pending Approval
    And User can see Pending Approval header
    When User click on Leave History
    Then User can see Leave History header

    Examples:
      | email                  | password    |
      | "testnini@yopmail.com" | "Tester@456"|

  Scenario Outline: Load Leave Calendar in Listing View with current month
    Given User logs in using <email> and <password> credentials
    When User click on Leave Management
    And User can see Leave Calendar header
    Then the current month and year should be displayed
    And the calendar listing should show the correct days for the current month

    Examples:
      | email                  | password    |
      | "testnini@yopmail.com" | "Tester@456"|

  Scenario Outline: Load Leave Calendar in Calendar View with current month
    Given User logs in using <email> and <password> credentials
    When User click on Leave Management
    And User can see Leave Calendar header
    Then User click on Calendar View button
    And the current month and year should be displayed
    And the calendar listing should show the correct days for the current month
    And today date should be highlighted with a blue circle
    And clicking on date <date> displays leave details or shows no event
    And weekends should be visually differentiated in grey
    And public holidays should be displayed with red styling
    And user should be able to navigate months smoothly

    Examples:
      | email                  | password    | date |
      | "testnini@yopmail.com" | "Tester@456"| 19   |

  Scenario Outline: Verify that clicking Today button will navigates back to the current month
    Given User logs in using <email> and <password> credentials
    When User click on Leave Management
    And User can see Leave Calendar header
    Then User click on Calendar View button
    And the current month and year should be displayed
    And the calendar listing should show the correct days for the current month
    And today date should be highlighted with a blue circle
    And clicking on date <date> displays leave details or shows no event
    And weekends should be visually differentiated in grey
    And public holidays should be displayed with red styling
    And user should be able to navigate months smoothly
    And clicking Today button will navigates back to current month

    Examples:
      | email                  | password    | date |
      | "testnini@yopmail.com" | "Tester@456"| 19   |


  Scenario Outline: Verify that clicking view button redirect to the correct employee Leave Summary page and navigate through other tab
    Given User logs in using <email> and <password> credentials
    When User click on Leave Management
    And User can see Leave Calendar header
    Then User click on Leave Directory
    And User can see Leave Directory header
    And an existing Leave Directory is available for viewing
    And user click on view action button
    And user should see the same employee name as header
    And Leave Summary tab should be active
    And user click on Upcoming Leave tab
    And Upcoming Leave tab should be active
    And user click on Leave History tab
    And Leave History tab should be active
    And Pending Approval button should redirect to the employee pending approval leave list

    Examples:
      | email                  | password    |
      | "testnini@yopmail.com" | "Tester@456"|

  @Test
  Scenario Outline: Verify that admin able to reject bulk leave request and the request should disappear from the list
    Given User logs in using <email> and <password> credentials
    When User click on Leave Management
    And User click on Pending Approval
    And User can see Pending Approval header
    Then user can select multiple leave entry
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

    @Approve
  Scenario Outline: Verify that admin able to approve bulk leave request and the request should disappear from the list
    Given User logs in using <email> and <password> credentials
    When User click on Leave Management
    And User click on Pending Approval
    And User can see Pending Approval header
    Then user can select multiple leave entry
    And user can see selected count
    And user click on the Approve Leave button
    And system should display success toast message
    And all the selected entry should be removed from the list

    Examples:
      | email                  | password    |
      | "testnini@yopmail.com" | "Tester@456"|

  Scenario Outline: TC060 Verify that clicking Filter button shows "Select Columns to Display" with all column checkboxes
    Given User logs in using <email> and <password> credentials
    And User click on Leave Management
    And User can see Leave Calendar header
    When User click on Leave History
    And User can see Leave History header
    And User click on Filter button
    Then User should see filter prompt

    Examples:
      | email                  | password      |
      | "testnini@yopmail.com"  | "Tester@456"  |

  Scenario Outline: Verify that all checked columns are visible in the table behind the modal
    Given User logs in using <email> and <password> credentials
    And User click on Leave Management
    And User can see Leave Calendar header
    When User click on Leave History
    And User can see Leave History header
    When User click on Filter button
    Then All checked column should visible in the table behind the modal

    Examples:
      | email                   | password      |
      | "testnini@yopmail.com"  | "Tester@456"  |


  Scenario Outline: Verify toggling each column visibility updates the table correctly
    Given User logs in using <email> and <password> credentials
    And User click on Leave Management
    And User can see Leave Calendar header
    When User click on Leave History
    And User can see Leave History header
    When User click on Filter button
    And User should see filter prompt
    Then User toggles <column> checkbox to <expectedState> in Leave Management
    And The <column> column should be <expectedState> in the Leave Management table

    Examples:
      | email                   | password      | column           | expectedState   |
      | "testnini@yopmail.com"  | "Tester@456"  |"Employee ID"     | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Employee ID"     | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Name"            | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Name"            | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Nickname"        | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Nickname"        | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Leave Type"      | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Leave Type"      | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Days"            | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Days"            | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Start Date"      | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Start Date"      | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"End Date"        | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"End Date"        | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Time Off"        | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Time Off"        | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Attachment"      | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Attachment"      | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Status"          | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Status"          | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Reject Reason"   | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Reject Reason"   | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Remarks"         | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Remarks"         | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Last Update by"  | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Last Update by"  | "visible"       |

   # Verify individual field filtering

  Scenario Outline: Filter table by field with value
    Given User logs in using <email> and <password> credentials
    And User click on Leave Management
    And User can see Leave Calendar header
    And User click on Leave History
    And User can see Leave History header
    And User click on Filter button
    And User should see filter prompt
    And User toggles <column> checkbox to <expectedState>
    When User enters <value> in <field> in Leave Management
    And User clicks Search
    Then The table should only show rows matching <field> with <value>

    Examples:
      | email                   | password    | field               | value                     |  column             | expectedState |
      |"testnini@yopmail.com"   | "Tester@456"| "Employee ID"       | "108"                     |  "Employee ID"      | "visible"     |
      |"testnini@yopmail.com"   | "Tester@456"| "Employee Name"     | "athirah"                 |  "Name"             | "visible"     |
      |"testnini@yopmail.com"   | "Tester@456"| "Nickname"          | "put"                     |  "Nickname"         | "visible"     |
      #|"testnini@yopmail.com"   | "Tester@456"| "Employee Email"    | "awtestingbot@outlook.com"|  "Employee Email"   | "visible"     |

  # Verify Status dropdown

  Scenario Outline: TC-A078 Filter table by Status <status>
    Given User logs in using <email> and <password> credentials
    And User click on Leave Management
    And User can see Leave Calendar header
    And User click on Leave History
    And User can see Leave History header
    And User click on Filter button
    And User should see filter prompt
    When User selects Status <status>
    And User clicks Search
    Then The table should only show rows matching <field> with <status>

    Examples:
      |email                 |password      | status        | field     |
      |"testnini@yopmail.com"| "Tester@456" | "Approved"    | "Status"  |
      |"testnini@yopmail.com"| "Tester@456" | "Cancelled"   | "Status"  |
      |"testnini@yopmail.com"| "Tester@456" | "Rejected"    | "Status"  |


    # Verify Leave Type dropdown

  Scenario Outline: TC-A078 Filter table by Status <status>
    Given User logs in using <email> and <password> credentials
    And User click on Leave Management
    And User can see Leave Calendar header
    And User click on Leave History
    And User can see Leave History header
    And User click on Filter button
    And User should see filter prompt
    When User selects Leave Type <leaveType>
    And User clicks Search
    Then The table should only show rows matching <field> with <leaveType>

    Examples:
      |email                 | password     | leaveType               | field         |
      |"testnini@yopmail.com"| "Tester@456" | "Annual Leave"          | "Leave Type"  |
      |"testnini@yopmail.com"| "Tester@456" | "Sick Leave"            | "Leave Type"  |
      |"testnini@yopmail.com"| "Tester@456" | "Hospitalisation Leave" | "Leave Type"  |
      |"testnini@yopmail.com"| "Tester@456" | "Maternity Leave"       | "Leave Type"  |
      |"testnini@yopmail.com"| "Tester@456" | "Paternity Leave"       | "Leave Type"  |
      |"testnini@yopmail.com"| "Tester@456" | "Carry Forward Leave"   | "Leave Type"  |
      |"testnini@yopmail.com"| "Tester@456" | "Marriage Leave"        | "Leave Type"  |
      |"testnini@yopmail.com"| "Tester@456" | "Emergency Leave"       | "Leave Type"  |
      |"testnini@yopmail.com"| "Tester@456" | "Unpaid Leave"          | "Leave Type"  |
      |"testnini@yopmail.com"| "Tester@456" | "Compassionate Leave"   | "Leave Type"  |

  # Verify Date From and Date To calendar

  Scenario Outline: TC-A0091 Filter table by Date Range
    Given User logs in using <email> and <password> credentials
    And User click on Leave Management
    And User can see Leave Calendar header
    And User click on Leave History
    And User can see Leave History header
    And User click on Filter button
    And User should see filter prompt
    When User sets Date From <dateFrom> and Date To <dateTo> in Leave Management
    And User clicks Search
    Then The table should only show rows within the date range <dateFrom> to <dateTo> in Leave Management

    Examples:
      |email                  | password    | dateFrom            | dateTo              |
      |"testnini@yopmail.com" | "Tester@456"| "20 November 2025"  | "05 December 2025"  |

  # Verify Search & Clear functionality
  Scenario Outline: Search applies criteria and Clear resets fields
    Given User logs in using <email> and <password> credentials
    And User click on Leave Management
    And User can see Leave Calendar header
    And User click on Leave History
    And User can see Leave History header
    And User click on Filter button
    And User should see filter prompt
    When User enters <value> in <field> in Leave Management
    And User selects Status <status>
    And User selects Leave Type <leaveType>
    And User sets Date From <dateFrom> and Date To <dateTo> in Leave Management
    And User clicks Search
    And The table should only show rows matching <field> with <value> in Leave Management
    And The table should only show rows within the date range <dateFrom> to <dateTo> in Leave Management
    And User click on Filter button
    And User should see filter prompt
    When User clicks Clear
    Then All filter fields should be empty or default in Leave Management
    And User closes the filter modal
    And The table should show all rows

    Examples:
      |email                  | password    | value                 | field                       |status         | leaveType     | dateFrom            | dateTo              |
      |"testnini@yopmail.com" | "Tester@456"| "303,athirah,puteri"  | "Employee ID,Name,Nickname" | "Approved"    | "Annual Leave"| "20 November 2025"  | "05 December 2025"  |

  #Verify No results state
  Scenario Outline: Search shows friendly "No results" state
    Given User logs in using <email> and <password> credentials
    And User click on Leave Management
    And User can see Leave Calendar header
    And User click on Leave History
    And User can see Leave History header
    And User click on Filter button
    And User should see filter prompt
    When User enters <value> in <field>
    And User clicks Search
    Then The table should show No results message in Leave Management

    Examples:
      |email                  | password    | value            | field            |
      |"testnini@yopmail.com" | "Tester@456"| "fifi"           | "Employee Name"  |

  # Verify pressing Enter triggers Search
  Scenario Outline: Pressing Enter triggers Search
    Given User logs in using <email> and <password> credentials
    And User click on Leave Management
    And User can see Leave Calendar header
    And User click on Leave History
    And User can see Leave History header
    When User click on Filter button
    And User should see filter prompt
    And User selects Status <status>
    And User sets Date From <dateFrom> and Date To <dateTo> in Leave Management
    And User enters <value> in <field> in Leave Management
    Then User click on ENTER key
    And The table should only show rows matching <field> with <value> in Leave Management
    And The table should only show rows within the date range <dateFrom> to <dateTo> in Leave Management

    Examples:
      |email                  | password    | value      | field  | dateFrom            | dateTo              | status    |
      |"testnini@yopmail.com" | "Tester@456"| "athirah"  | "Name" | "20 November 2025"  | "05 December 2025"  | "Approved" |



  Scenario Outline: Verify that Export respects the filtered data and visible columns
    Given User logs in using <email> and <password> credentials
    And User click on Leave Management
    And User can see Leave Calendar header
    And User click on Leave History
    And User can see Leave History header
    When User click on Filter button
    And User should see filter prompt
    And User selects Status <status>
    And User sets Date From <dateFrom> and Date To <dateTo> in Leave Management
    And User enters <value> in <field> in Leave Management
    And User clicks Search
    Then admin click export button
    And exported file should contain only filtered data and visible columns in Leave Management
      #And exported file should contain only filtered data and visible columns


    Examples:
      |email                  | password    | value      | field  | dateFrom            | dateTo              | status    |
      |"testnini@yopmail.com" | "Tester@456"| "athirah"  | "Name" | "20 November 2025"  | "05 December 2025"  | "Approved" |

