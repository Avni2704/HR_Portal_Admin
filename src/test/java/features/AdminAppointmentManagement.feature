Feature: Admin Appointment Management

  @TC001
  Scenario Outline: Verify that the Appointment Management section is visible in sidebar
    Given User logs in using <email> and <password> credentials
    When User click on Appointment Management
    Then User can see Appointment List header

    Examples:
      | email                  | password    |
      | "testnini@yopmail.com" | "Tester@456"|

  @TC002
  Scenario Outline: Verify that the active tab is highlighted with a green colour
    Given User logs in using <email> and <password> credentials
    When User click on Appointment Management
    Then User can see Appointment List header
    And User can see active tab is highlighted with a green colour

    Examples:
      | email                  | password     |
      | "testnini@yopmail.com" | "Tester@456" |

  @TC003
  Scenario Outline: Verify that the sidebar collapses/expands without UI distortion
    Given User logs in using <email> and <password> credentials
    When User click on Appointment Management
    And User close the sidebar
    And User open the sidebar
    Then User can see Appointment List header

    Examples:
      | email                  | password    |
      | "testnini@yopmail.com" | "Tester@456"|

  @TC004
  Scenario Outline: Verify that navigation labels do not overflow or break UI on smaller screens
    Given User logs in using <email> and <password> credentials
    When User click on Appointment Management
    And User resize the screen
    Then User can see Appointment List header

    Examples:
      | email                  | password    |
      | "testnini@yopmail.com" | "Tester@456"|

  @TC005
  Scenario Outline: Verify that the sidebar is still functional after multiple quick clicks
    Given User logs in using <email> and <password> credentials
    When User click on Appointment Management
    And User close and open the sidebar multiple times
    Then User can see Appointment List header

    Examples:
      | email                  | password    |
      | "testnini@yopmail.com" | "Tester@456"|

  @TC006
  Scenario Outline: Verify that page content and the selected menu item remain in sync when using browser back/forward
    Given User logs in using <email> and <password> credentials
    When User click on Appointment Management
    And User click browser back button
    And User click browser forward button
    Then User can see Appointment List header

    Examples:
      | email                  | password    |
      | "testnini@yopmail.com" | "Tester@456"|

  @TC007
  Scenario Outline: Verify that export button generates a file of appointment history
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    And User can see Appointment List header
    When User click on Export button
    Then Appointment List file should be downloaded

    Examples:
      | email                  | password    |
      | "testnini@yopmail.com" | "Tester@456"|

  @TC008
  Scenario Outline: Verify that page loads correct default header
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    When User can see Appointment List header
    Then Appointment List table should display the correct header

    Examples:
      | email                 | password     |
      | "testnini@yopmail.com" | "Tester@456" |

  @TC009
  Scenario Outline: Verify that Appointment List header display correctly
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    When User can see Appointment List header
    Then Column <columnName> should display correct data

    Examples:
      | email                      | password     | columnName        |
      | "awtestingbot@outlook.com" | "Perfume@123"| "Employee Name"   |
      | "awtestingbot@outlook.com" | "Perfume@123"| "Mentor Name"     |
      | "awtestingbot@outlook.com" | "Perfume@123"| "Appointment Date"|
      | "awtestingbot@outlook.com" | "Perfume@123"| "Start Time"      |
      | "awtestingbot@outlook.com" | "Perfume@123"| "End Time"        |
      | "awtestingbot@outlook.com" | "Perfume@123"| "Remark"          |
      | "awtestingbot@outlook.com" | "Perfume@123"| "Status"          |

  @TC014
  Scenario Outline: Verify that Time have correct format
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    When User can see Appointment List header
    Then Column <columnName> should display correct format

    Examples:
      | email                       | password      | columnName     |
      | "awtestingbot@outlook.com"  | "Perfume@123" | "Start Time"   |
      | "awtestingbot@outlook.com"  | "Perfume@123" | "End Time"     |

  @TC019
  Scenario Outline: Verify that Status are colour coded correctly
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    When User can see Appointment List header
    Then Column Status should colour coded correctly

    Examples:
      |  email                      |  password     |
      | "awtestingbot@outlook.com"  | "Perfume@123" |

  @TC020
  Scenario Outline: Verify header sorting icon works
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    And User can see Appointment List header
    When User clicks on header sorting icon for column <columnName>
    And Column <columnName> is sorted in descending order
    And User clicks on header sorting icon for column <columnName>
    Then Column <columnName> is sorted in ascending order

    Examples:
      | email                      |  password    |   columnName        |
      | "awtestingbot@outlook.com" | "Perfume@123"| "Employee Name"     |
      | "awtestingbot@outlook.com" | "Perfume@123"| "Mentor Name"       |
      | "awtestingbot@outlook.com" | "Perfume@123"| "Appointment Date"  |
      | "awtestingbot@outlook.com" | "Perfume@123"| "Start Time"        |
      | "awtestingbot@outlook.com" | "Perfume@123"| "End Time"          |
      | "awtestingbot@outlook.com" | "Perfume@123"| "Remark"            |
      | "awtestingbot@outlook.com" | "Perfume@123"| "Status"            |

  @TC027
  Scenario Outline: TC027 Verify that each row have action button
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    When User can see Appointment List header
    Then Each row should have action button

    Examples:
      | email                      | password     |
      | "awtestingbot@outlook.com" | "Perfume@123"|

  @TC028
  Scenario Outline: TC028 Verify that each row have action button
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    When User can see Appointment List header
    Then User click on the action button
    And User should see actions

    Examples:
      | email                      | password     |
      | "awtestingbot@outlook.com" | "Perfume@123"|

  @TC029
  Scenario Outline: TC029 Verify the behaviour of Status when selecting Mark As Completed
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    And User can see Appointment List header
    And User locates a row with status <currentStatus>
    And User clicks on the Action button for that row
    When User selects "Mark as Completed"
    And User should see <modalMessage>
    And User click Update button
    Then The row should now show status <expectedStatus>

    Examples:
      | email                      | password       | currentStatus    | modalMessage                                                             | expectedStatus |
      #| "testnini@yopmail.com" | "Tester@456"  | "Upcoming"       |  "Are you sure you wish to update the appointment status to COMPLETED ?" | "Completed"    |
      | "testnini@yopmail.com" | "Tester@456"  | "Acknowledged"   |  "Are you sure you wish to update the appointment status to COMPLETED ?" | "Completed"    |
      #| "testnini@yopmail.com" | "Tester@456"  | "Completed"      |  "Are you sure you wish to update the appointment status to COMPLETED ?" | "Completed"    |
      #| "testnini@yopmail.com" | "Tester@456"  | "Cancelled"      |  "Are you sure you wish to update the appointment status to COMPLETED ?" | "Completed"    |
      #| "testnini@yopmail.com" | "Tester@456"  | "Overdue"        |  "Are you sure you wish to update the appointment status to COMPLETED ?" | "Completed"    |

  @TC030
  Scenario Outline: TC029 Verify the behaviour of Status when selecting Mark As Completed
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    And User can see Appointment List header
    And User locates a row with status <currentStatus>
    And User clicks on the Action button for that row
    When User selects "Mark as Completed"
    And User should see <modalMessage>
    And User click Cancel button in the dialog
    Then The row should now show status <expectedStatus>

    Examples:
      | email                      | password       | currentStatus    | modalMessage                                                             | expectedStatus |
      #| "testnini@yopmail.com" | "Tester@456"  | "Upcoming"       |  "Are you sure you wish to update the appointment status to COMPLETED ?" | "Upcoming"    |
      #| "testnini@yopmail.com" | "Tester@456"  | "Acknowledged"   |  "Are you sure you wish to update the appointment status to COMPLETED ?" | "Acknowledged"    |
      #| "testnini@yopmail.com" | "Tester@456"  | "Completed"      |  "Are you sure you wish to update the appointment status to COMPLETED ?" | "Completed"    |
      #| "testnini@yopmail.com" | "Tester@456"  | "Cancelled"      |  "Are you sure you wish to update the appointment status to COMPLETED ?" | "Cancelled"    |
      | "testnini@yopmail.com" | "Tester@456"  | "Overdue"        |  "Are you sure you wish to update the appointment status to COMPLETED ?" | "Overdue"    |


  @TC031
  Scenario Outline: TC029 Verify the behaviour of Status when selecting Mark As Completed
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    And User can see Appointment List header
    And User locates a row with status <currentStatus>
    And User clicks on the Action button for that row
    When User selects "Mark as Cancelled"
    And User should see <modalMessage>
    And User click Update button
    Then The row should now show status <expectedStatus>

    Examples:
      | email                      | password       | currentStatus    | modalMessage                                                             | expectedStatus |
      #| "testnini@yopmail.com" | "Tester@456"  | "Upcoming"       |  "Are you sure you wish to update the appointment status to CANCELLED ?" | "Cancelled"    |
      #| "testnini@yopmail.com" | "Tester@456"  | "Acknowledged"   |  "Are you sure you wish to update the appointment status to CANCELLED ?" | "Cancelled"    |
      | "testnini@yopmail.com" | "Tester@456"  | "Completed"      |  "Are you sure you wish to update the appointment status to CANCELLED ?" | "Cancelled"    |
      #| "testnini@yopmail.com" | "Tester@456"  | "Cancelled"      |  "Are you sure you wish to update the appointment status to CANCELLED ?" | "Cancelled"    |
      #| "testnini@yopmail.com" | "Tester@456"  | "Overdue"        |  "Are you sure you wish to update the appointment status to CANCELLED ?" | "Cancelled"    |

  @TC032
  Scenario Outline: TC029 Verify the behaviour of Status when selecting Mark As Completed
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    And User can see Appointment List header
    And User locates a row with status <currentStatus>
    And User clicks on the Action button for that row
    When User selects "Mark as Cancelled"
    And User should see <modalMessage>
    And User click Cancel button in the dialog
    Then The row should now show status <expectedStatus>

    Examples:
      | email                      | password       | currentStatus    | modalMessage                                                             | expectedStatus |
      #| "testnini@yopmail.com" | "Tester@456"  | "Upcoming"       |  "Are you sure you wish to update the appointment status to CANCELLED ?" | "Upcoming"    |
      #| "testnini@yopmail.com" | "Tester@456"  | "Acknowledged"   |  "Are you sure you wish to update the appointment status to CANCELLED ?" | "Acknowledged"    |
      #| "testnini@yopmail.com" | "Tester@456"  | "Completed"      |  "Are you sure you wish to update the appointment status to CANCELLED ?" | "Completed"    |
      #| "testnini@yopmail.com" | "Tester@456"  | "Cancelled"      |  "Are you sure you wish to update the appointment status to CANCELLED ?" | "Cancelled"    |
      | "testnini@yopmail.com" | "Tester@456"  | "Overdue"        |  "Are you sure you wish to update the appointment status to CANCELLED ?" | "Overdue"    |

  @TC060
  Scenario Outline: TC060 Verify that clicking Filter button shows "Select Columns to Display" with all column checkboxes
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    And User can see Appointment List header
    When User click on Filter button
    Then User should see filter prompt

    Examples:
      | email                  | password      |
      | "testnini@yopmail.com"  | "Tester@456"  |

  @TC061
  Scenario Outline: Verify that all checked columns are visible in the table behind the modal
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    And User can see Appointment List header
    When User click on Filter button
    Then All checked column should visible in the table behind the modal

    Examples:
      | email                   | password      |
      | "testnini@yopmail.com"  | "Tester@456"  |

  @TC062
  Scenario Outline: Verify toggling each column visibility updates the table correctly
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    And User can see Appointment List header
    When User click on Filter button
    And User should see filter prompt
    Then User toggles <column> checkbox to <expectedState>
    And The <column> column should be <expectedState> in the table

    Examples:
      | email                   | password      | column                      | expectedState   |
      | "testnini@yopmail.com"  | "Tester@456"  |"Employee Name"              | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Employee Name"              | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Employee Nickname"          | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Employee Nickname"          | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Employee Email"             | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Employee Email"             | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Mentor Name"                | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Mentor Name"                | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Mentor Nickname"            | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Mentor Nickname"            | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Mentor Email"               | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Mentor Email"               | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Appointment Date"           | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Appointment Date"           | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Appointment Start Time"     | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Appointment Start Time"     | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Appointment End Time"       | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Appointment End Time"       | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Remark"                     | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Remark"                     | "visible"       |
      | "testnini@yopmail.com"  | "Tester@456"  |"Status"                     | "hidden"        |
      | "testnini@yopmail.com"  | "Tester@456"  |"Status"                     | "visible"       |

  #TC-A077-TC-A102
   # Verify individual field filtering
  @TC-A077
  Scenario Outline: Filter table by field with value
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    And User can see Appointment List header
    And User click on Filter button
    And User should see filter prompt
    And User toggles <column> checkbox to <expectedState>
    When User enters <value> in <field>
    And User clicks Search
    Then The table should only show rows matching <field> with <value>

    Examples:
      | email                   | password    | field               | value                     |  column             | expectedState |
      |"testnini@yopmail.com"   | "Tester@456"| "Employee Name"     | "athirah"                 |  "Employee Name"    | "visible"     |
      |"testnini@yopmail.com"   | "Tester@456"| "Employee Nickname" | "put"                     |  "Employee Nickname"| "visible"     |
      |"testnini@yopmail.com"   | "Tester@456"| "Employee Email"    | "awtestingbot@outlook.com"|  "Employee Email"   | "visible"     |
      |"testnini@yopmail.com"   | "Tester@456"| "Mentor Name"       | "kangkung"                |  "Mentor Name"      | "visible"     |
      |"testnini@yopmail.com"   | "Tester@456"| "Mentor Nickname"   | "the guy"                 |  "Mentor Nickname"  | "visible"     |
      |"testnini@yopmail.com"   | "Tester@456"| "Mentor Email"      | "awtestingbot@outlook.com"|  "Mentor Email"     | "visible"     |

  # Verify Status dropdown
  @TC-A078
  Scenario Outline: TC-A078 Filter table by Status <status>
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    And User can see Appointment List header
    And User click on Filter button
    And User should see filter prompt
    When User selects Status <status>
    And User clicks Search
    Then The table should only show rows matching <field> with <status>

    Examples:
      |email                 |password      | status        | field     |
      |"testnini@yopmail.com"| "Tester@456" | "Upcoming"    | "Status"  |
      |"testnini@yopmail.com"| "Tester@456" | "Acknowledged"| "Status"  |
      |"testnini@yopmail.com"| "Tester@456" | "Completed"   | "Status"  |
      |"testnini@yopmail.com"| "Tester@456" | "Cancelled"   | "Status"  |
      |"testnini@yopmail.com"| "Tester@456" | "Overdue"     | "Status"  |

  # Verify Date From and Date To calendar
  @date
  Scenario Outline: TC-A0091 Filter table by Date Range
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    And User can see Appointment List header
    And User click on Filter button
    And User should see filter prompt
    When User sets Date From <dateFrom> and Date To <dateTo>
    And User clicks Search
    Then The table should only show rows within the date range <dateFrom> to <dateTo>

    Examples:
      |email                  | password    | dateFrom            | dateTo              |
      |"testnini@yopmail.com" | "Tester@456"| "20 November 2025"  | "05 December 2025"  |

  # Verify Search & Clear functionality
  Scenario Outline: Search applies criteria and Clear resets fields
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    And User can see Appointment List header
    And User click on Filter button
    And User should see filter prompt
    When User enters <value> in <field>
    And User selects Status <status>
    And User sets Date From <dateFrom> and Date To <dateTo>
    And User clicks Search
    And The table should only show rows matching <field> with <value>
    And The table should only show rows within the date range <dateFrom> to <dateTo>
    And User click on Filter button
    And User should see filter prompt
    When User clicks Clear
    Then All filter fields should be empty or default
    And User closes the filter modal
    And The table should show all rows

    Examples:
      |email                  | password    | value               | field               | status      | dateFrom            | dateTo              |
      |"testnini@yopmail.com" | "Tester@456"| "athirah"           | "Employee Name"     | "Overdue"   | "20 November 2025"  | "05 December 2025"  |

  #Verify No results state
  Scenario Outline: Search shows friendly "No results" state
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    And User can see Appointment List header
    And User click on Filter button
    And User should see filter prompt
    When User enters <value> in <field>
    And User clicks Search
    Then The table should show No results message

    Examples:
      |email                  | password    | value            | field              |
      |"testnini@yopmail.com" | "Tester@456"| "fifi"           | "Employee Name"  |

  # Verify pressing Enter triggers Search
  @key
  Scenario Outline: Pressing Enter triggers Search
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    And User can see Appointment List header
    And User click on Filter button
    And User should see filter prompt
    When User sets Date From <dateFrom> and Date To <dateTo>
    And User selects Status <status>
    And User enters <value> in <field>
    Then User click on ENTER key
    And The table should only show rows matching <field> with <value>
    And The table should only show rows within the date range <dateFrom> to <dateTo>

    Examples:
      |email                  | password    | value      | field           | dateFrom            | dateTo              | status    |
      |"testnini@yopmail.com" | "Tester@456"| "athirah"  | "Employee Name" | "20 November 2025"  | "05 December 2025"  | "Overdue" |


  Scenario Outline: Verify that Export respects the filtered data and visible columns
    Given User logs in using <email> and <password> credentials
    And User click on Appointment Management
    And User can see Appointment List header
    And User click on Filter button
    And User should see filter prompt
    When User sets Date From <dateFrom> and Date To <dateTo>
    And User selects Status <status>
    And User enters <value> in <field>
    And User clicks Search
    Then admin click export button
    And exported file should contain only filtered data and visible columns

    Examples:
      |email                  | password    | value      | field           | dateFrom            | dateTo              | status    |
      |"testnini@yopmail.com" | "Tester@456"| "athirah"  | "Employee Name" | "20 November 2025"  | "05 December 2025"  | "Overdue" |