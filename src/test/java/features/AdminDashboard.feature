Feature: Dashboard Feature
  This feature deals with all the components in the application

  #TC-A016
  Scenario Outline: Verify Admin can successfully lands on the dashboard after login
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard

    Examples:
      | email                  | password       |
      | "testnini@yopmail.com" | "Tester@456" |

  #TC-A017
  Scenario Outline: Verify Admin's name  and greeting are displayed correctly
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin see username and greeting

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A018
  Scenario Outline: Verify the current date is displayed correctly.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin see the current date

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A019
  Scenario Outline: Verify “Announcements” section displays recent updates/messages posted by HR/Admin.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin see the announcements section

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A020
  Scenario Outline: Verify "Holiday this Month” section shows relevant public holidays with dates and states.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin see the holiday section

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A021
  Scenario Outline: Verify Admin can click on "Logout" button
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin click logout

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A022
  Scenario Outline: Verify Admin can click on "Leave Management" sidebar
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin click leave management
    Then admin see leave calender page

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A023
  Scenario Outline: Verify Admin can click on "Allowance Management" sidebar
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin click allowance management
    Then admin see allowance directory page

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A024
  Scenario Outline: Verify Admin can click on “Appointment Management" sidebar
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin click appointment management
    Then admin see appointment list page

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A025
  Scenario Outline: Verify Admin can click on "Dashboard" sidebar
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin click appointment management
    Then admin click dashboard
    And admin see the dashboard

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A026
  Scenario Outline: Verify Admin can click on the 'Profile' icon to view the dropdown.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin click profile icon
    Then admin see dropdown and change password

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A027
  Scenario Outline: Verify "Celebration Corner” section shows the list of the employees names and their birthday dates.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin see celebration corner

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A028

  #TC-A029
  Scenario Outline: Verify Admin can click the "Durian" Icon to close the sidebar
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin click company icon

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A030
  Scenario Outline: Verify Holidays are sorted correctly in ascending order by date.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin see holidays displayed in ascending order by date

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A031
  Scenario Outline: Dashboard is responsive and elements realign correctly on various screen sizes.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And dashboard responsive and elements realign

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A032
  Scenario Outline: Verify Admin can click "Change Password" Dropdown
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin click profile icon
    Then admin see dropdown and change password
    And admin click change password dropdown

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A033
  #Scenario Outline: Verify dashboard is not accessible if Admin session expired.

  #TC-A034
  Scenario Outline: Verify Admin can click "Create Announcement" button if 15 posts is not reached.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin click create announcement

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A035
  Scenario Outline: Verify Announcement section displays the number of remaining posts
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin confirm remaining announcement

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A036
  Scenario Outline: Verify Admin can switch between "Posted" and "Upcoming" announcement tabs.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin switch posted and upcoming announcement

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A037
  Scenario Outline: Verify Admin can view "Active" announcement.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin view active announcement

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A038
  Scenario Outline: Verify Admin can view "Active" announcement.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    Then admin view active announcement with green tag

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A039
  Scenario Outline: Verify new announcement has 'Active' tag.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    And admin see the dashboard
    And admin create announcement <expectedTitle> start <pickStartDate> end <pickEndDate> description <description>
    And admin see the new announcement <expectedTitle>
    Then admin see new announcement status <expectedTitle> must be <expectedStatus>

    Examples:
      | email              | password       | expectedTitle       | pickStartDate | pickEndDate | description                                    | expectedStatus |
      | "nami@mailsac.com" | "Password@123" | "Test Announcement" | "25"          | "5"         | "It is a testing announcement by Tester"       | "Active"       |

  #TC-A040
  Scenario Outline: Verify old announcement has 'Expired' tag.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    Then admin view expired announcement with red tag

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A041
  Scenario Outline: Verify Admin can click on the Pin icon to pin the announcement.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin click pin icon and identify color change
    Then admin see pin announcement banner

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A042
  Scenario Outline: Verify Admin can click on the edit the announcement.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin click edit icon
    Then admin see create announcement tab

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A043
  Scenario Outline: Verify “Employee on Leave” displays employee names, leave type
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    Then admin see employee on leave with details

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A044
  Scenario Outline: Verify Admin can click “View All” to see full leave list.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin see employee on leave with details
    And admin click view all
    Then admin see leave calender page

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A045
  #TC-A046

  #TC-A047
  Scenario Outline: Verify Admin can click on "User Management" sidebar
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin click user management
    Then admin see list employee page

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A048
  Scenario Outline: Verify Admin can click on "Company Benefits" sidebar
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin click company benefits
    Then admin see job position list page

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A049
  Scenario Outline: Verify Admin can click on the delete the announcement.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    Then admin select company modal
    And admin see the dashboard
    And admin click delete icon
    Then admin see delete announcement banner

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A050
  Scenario Outline: Verify Create Announcement button is disabled after reaching 15 announcements.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin see remaining
    Then Admin see disabled button

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |


  #TC-A051
  Scenario Outline: Verify Admin can Create announcement with all valid fields filled and a valid file attachment (jpg/pdf ≤ 5MB).
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click create announcement
    And admin insert title <expectedTitle>
    And admin insert start date <pickStartDate>
    And admin insert end date <pickEndDate>
    And admin insert description <description>
    And admin upload attachment <attachment>
    When admin click create button
    Then admin see announcement banner

    Examples:
      | email              | password       | expectedTitle       | pickStartDate | pickEndDate  | description                                    | attachment                             |
      | "nami@mailsac.com" | "Password@123" | "Test Announcement" | "1"           | "15"         | "It is a testing announcement by Tester"       | "C:\\Users\\naqiy\\Desktop\\empty.pdf" |


  #TC-A052
  Scenario Outline: Verify Admin can Create announcement with only mandatory fields filled.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click create announcement
    And admin insert title <expectedTitle>
    And admin insert start date <pickStartDate>
    And admin insert end date <pickEndDate>
    And admin insert description <description>
    Then admin click create button

    Examples:
      | email              | password       | expectedTitle       | pickStartDate | pickEndDate | description                                    |
      | "nami@mailsac.com" | "Password@123" | "Test Announcement" | "1"           | "15"         | "It is a testing announcement by Tester"      |

  #TC-A053
  Scenario Outline: Verify the Start Date cannot be after End Date.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click create announcement
    And admin insert title <expectedTitle>
    And admin insert start date <pickStartDate>
    And admin insert end date <pickEndDate>
    And admin insert description <description>
    And admin click create button
    Then admin see validation error

    Examples:
      | email              | password       | expectedTitle       | pickStartDate | pickEndDate | description                                    |
      | "nami@mailsac.com" | "Password@123" | "Test Announcement" | "8"           | "4"         | "It is a testing announcement by Tester"       |

  #TC-A054
  Scenario Outline: Verify Admin Upload a valid .jpg file ≤ 5MB.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click create announcement
    And admin insert title <expectedTitle>
    And admin insert start date <pickStartDate>
    And admin insert end date <pickEndDate>
    And admin insert description <description>
    And admin upload attachment <attachment>
    And admin click create button
    Then admin see announcement banner

    Examples:
      | email              | password       | expectedTitle       | pickStartDate | pickEndDate  | description                                    | attachment                                 |
      | "nami@mailsac.com" | "Password@123" | "Test Announcement" | "1"           | "15"         | "It is a testing announcement by Tester"       | "C:\\Users\\naqiy\Desktop\\exampleJPG.jpg" |

  #TC-A055
  Scenario Outline: Upload a valid .pdf file ≤ 5MB.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click create announcement
    And admin insert title <expectedTitle>
    And admin insert start date <pickStartDate>
    And admin insert end date <pickEndDate>
    And admin insert description <description>
    And admin upload attachment <attachment>
    And admin click create button
    Then admin see announcement banner

    Examples:
      | email              | password       | expectedTitle       | pickStartDate | pickEndDate  | description                                    | attachment                             |
      | "nami@mailsac.com" | "Password@123" | "Test Announcement" | "1"           | "15"         | "It is a testing announcement by Tester"       | "C:\\Users\\naqiy\\Desktop\\empty.pdf" |

  #TC-A056
  Scenario Outline: Ensure character counter updates as Description is typed (max 255).
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click create announcement
    And admin insert title <expectedTitle>
    And admin insert start date <pickStartDate>
    And admin insert end date <pickEndDate>
    And admin insert description <description>
    And admin upload attachment <attachment>
    And admin click create button
    Then admin see announcement banner

    Examples:
      | email              | password       | expectedTitle       | pickStartDate | pickEndDate  | description                                                                                                                                                                                                                                                          | attachment                             |
      | "nami@mailsac.com" | "Password@123" | "Test Announcement" | "1"           | "15"         | "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis,"    | "C:\\Users\\naqiy\\Desktop\\empty.pdf" |

  #TC-A057
  Scenario Outline: Verify Admin can Click "Cancel" and verify that the form is cleared or the modal closes.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click create announcement
    And admin insert title <expectedTitle>
    And admin insert start date <pickStartDate>
    And admin insert end date <pickEndDate>
    And admin insert description <description>
    And admin upload attachment <attachment>
    And admin click cancel button
    And admin click create announcement
    Then admin verify data inserted not saved <expectedTitle>

    Examples:
      | email              | password       | expectedTitle       | pickStartDate | pickEndDate  | description                                    | attachment                         |
      | "nami@mailsac.com" | "Password@123" | "Test Announcement" | "1"           | "15"         | "It is a testing announcement by Tester"       | "C:\\Users\\naqiy\\Desktop\\empty.pdf" |

  #TC-A058
  Scenario Outline: Verify Admin can Click "Create" and verify that announcement is submitted and modal closes.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click create announcement
    And admin insert title <expectedTitle>
    And admin insert start date <pickStartDate>
    And admin insert end date <pickEndDate>
    And admin insert description <description>
    And admin upload attachment <attachment>
    And admin click create button
    And admin see announcement banner
    Then admin see the dashboard

    Examples:
      | email              | password       | expectedTitle       | pickStartDate | pickEndDate | description                                    | attachment                             |
      | "nami@mailsac.com" | "Password@123" | "Test Announcement" | "1"           | "15"         | "It is a testing announcement by Tester"      | "C:\\Users\\naqiy\\Desktop\\empty.pdf" |

  #TC-A059
  Scenario Outline: Verify Admin Leave Title field empty and click "Create".
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click create announcement
    And admin insert title <expectedTitle>
    And admin insert start date <pickStartDate>
    And admin insert end date <pickEndDate>
    And admin insert description <description>
    And admin upload attachment <attachment>
    And admin click create button
    Then admin see validation message

    Examples:
      | email              | password       | expectedTitle | pickStartDate | pickEndDate | description                               | attachment                         |
      | "nami@mailsac.com" | "Password@123" | ""            | "1"           | "15"        | "It is a testing announcement by Tester"  | "C:\\Users\\naqiy\\Desktop\\empty.pdf" |

  #TC-A060
  Scenario Outline: Verify Admin Leave Start Date field empty and click "Create".
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click create announcement
    And admin insert title <expectedTitle>
    And admin insert end date <pickEndDate>
    And admin insert description <description>
    And admin upload attachment <attachment>
    And admin click create button
    Then admin see validation message

    Examples:
      | email              | password       | expectedTitle        | pickStartDate | pickEndDate | description                               | attachment                          |
      | "nami@mailsac.com" | "Password@123" | "Test Announcement"  | ""            | "15"        | "It is a testing announcement by Tester"  | "C:\\Users\\naqiy\\Desktop\\empty.pdf" |

  #TC-A061
  Scenario Outline: Verify Admin Leave End Date field empty and click "Create"
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click create announcement
    And admin insert title <expectedTitle>
    And admin insert start date <pickStartDate>
    And admin insert description <description>
    And admin upload attachment <attachment>
    And admin click create button
    Then admin see validation message

    Examples:
      | email              | password       | expectedTitle        | pickStartDate | pickEndDate | description                               | attachment                          |
      | "nami@mailsac.com" | "Password@123" | "Test Announcement"  | "1"           | ""          | "It is a testing announcement by Tester"  | "C:\\Users\\naqiy\\Desktop\\empty.pdf" |

  #TC-A062
  Scenario Outline: Verify Admin Leave Description field empty and click "Create"
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click create announcement
    And admin insert title <expectedTitle>
    And admin insert start date <pickStartDate>
    And admin insert end date <pickEndDate>
    And admin insert description <description>
    And admin upload attachment <attachment>
    And admin click create button
    Then admin see validation message

    Examples:
      | email              | password       | expectedTitle        | pickStartDate | pickEndDate | description  | attachment                         |
      | "nami@mailsac.com" | "Password@123" | "Test Announcement"  | "1"           | "15"        | ""           | "C:\\Users\\naqiy\\Desktop\\empty.pdf" |

  #TC-A063
  Scenario Outline: Verify Admin couldn't insert more than 255 char in Description field
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click create announcement
    And admin insert title <expectedTitle>
    And admin insert start date <pickStartDate>
    And admin insert end date <pickEndDate>
    And admin insert description <description>
    Then verify description max length
    And admin upload attachment <attachment>
    And admin click create button
    Then admin see announcement banner

    Examples:
      | email              | password       | expectedTitle       | pickStartDate | pickEndDate  | description                                                                                                                                                                                                                                                                                                                                                                                                                                                       | attachment                         |
      | "nami@mailsac.com" | "Password@123" | "Test Announcement" | "1"           | "15"         | "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum"    | "C:\\Users\\naqiy\\Desktop\\empty.pdf" |

  #TC-A064
  Scenario Outline: Upload a file with invalid format ((e.g., .exe, .docx, .png)
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click create announcement
    And admin insert title <expectedTitle>
    And admin insert start date <pickStartDate>
    And admin insert end date <pickEndDate>
    And admin insert description <description>
    And admin upload attachment <attachment>
    And admin click create button
    Then admin see file validation

    Examples:
      | email              | password       | expectedTitle       | pickStartDate | pickEndDate  | description                                    | attachment                                    |
      | "nami@mailsac.com" | "Password@123" | "Test Announcement" | "1"           | "15"         | "It is a testing announcement by Tester"       | "C:\\Users\\naqiy\\Desktop\\exampleDOCX.docx" |

  #TC-A065
  Scenario Outline: Upload a file larger than 5MB – should show error message.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click create announcement
    And admin insert title <expectedTitle>
    And admin insert start date <pickStartDate>
    And admin insert end date <pickEndDate>
    And admin insert description <description>
    And admin upload attachment <attachment>
    And admin click create button
    Then admin see large file validation

    Examples:
      | email              | password       | expectedTitle       | pickStartDate | pickEndDate  | description                                    | attachment                                  |
      | "nami@mailsac.com" | "Password@123" | "Test Announcement" | "1"           | "15"         | "It is a testing announcement by Tester"       | "C:\\Users\\naqiy\\Desktop\\sample15mb.pdf" |

  #TC-A066

  #TC-A067
  Scenario Outline: Verify that only the Title can be edited and saved successfully.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click edit icon
    And admin insert title <expectedTitle>
    And admin click create button
    Then admin see updated announcement banner

    Examples:
      | email              | password       | expectedTitle              |
      | "nami@mailsac.com" | "Password@123" | "Test Announcement Edited" |

  #TC-A068
  Scenario Outline: Verify that only the Start Date can be edited and saved successfully.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click edit icon
    And admin insert start date <pickStartDate>
    And admin click create button
    Then admin see updated announcement banner

    Examples:
      | email              | password       | pickStartDate  |
      | "nami@mailsac.com" | "Password@123" | "5"            |

  #TC-A069
  Scenario Outline: Verify that only the End Date can be edited and saved successfully.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click edit icon
    And admin insert end date <pickEndDate>
    And admin click create button
    Then admin see updated announcement banner

    Examples:
      | email              | password       | pickEndDate  |
      | "nami@mailsac.com" | "Password@123" | "20"         |

  #TC-A070
  Scenario Outline: Verify that only the Description can be edited and saved successfully.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click edit icon
    And admin insert description <description>
    And admin click create button
    Then admin see updated announcement banner

    Examples:
      | email              | password       | description                                            |
      | "nami@mailsac.com" | "Password@123" | "This is the test for editing the description"         |

  #TC-A071
  Scenario Outline: Verify that an existing attachment can be replaced with a valid file (jpg/pdf ≤ 5MB).
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click edit icon
    And admin upload attachment <attachment>
    And admin click create button
    Then admin see updated announcement banner

    Examples:
      | email              | password       | attachment                                    |
      | "nami@mailsac.com" | "Password@123" | "C:\\Users\\naqiy\Desktop\\exampleJPG.jpg"    |

  #TC-A073
  Scenario Outline: Verify that no changes still allow form submission without errors.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click edit icon
    And admin click create button
    Then admin see updated announcement banner

    Examples:
      | email              | password       |
      | "nami@mailsac.com" | "Password@123" |

  #TC-A074
  Scenario Outline: Verify that clicking “Cancel” discards all changes and closes the form.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click edit icon
    And admin insert title <expectedTitle>
    And admin insert start date <pickStartDate>
    And admin insert end date <pickEndDate>
    And admin insert description <description>
    And admin upload attachment <attachment>
    And admin click cancel button

    Examples:
      | email              | password       | expectedTitle              | pickStartDate | pickEndDate  | description                              | attachment                                  |
      | "nami@mailsac.com" | "Password@123" | "Test Announcement Edited" | "5"           | "20"         | "This is for test editing purpose"       | "C:\\Users\\naqiy\\Desktop\\sample15mb.pdf" |

  #TC-A075
  Scenario Outline: Verify that deleting the Title and saving triggers a validation error.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click edit icon
    And admin insert title <expectedTitle>
    And admin click create button
    Then admin see validation message

    Examples:
      | email              | password       | expectedTitle              |
      | "nami@mailsac.com" | "Password@123" | "Test Announcement Edited" |

 #TC-A076
  Scenario Outline: Verify that deleting the Description triggers a validation error.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click edit icon
    And admin insert description <description>
    And admin click create button
    Then admin see validation message

    Examples:
      | email              | password       | description                                            |
      | "nami@mailsac.com" | "Password@123" | "This is the test for editing the description"         |

  #TC-A077
  Scenario Outline: Verify that setting the End Date earlier than the Start Date shows a validation error.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click edit icon
    And admin insert start date <pickStartDate>
    And admin insert end date <pickEndDate>
    And admin click create button
    Then admin see validation error

    Examples:
      | email              | password       | pickStartDate | pickEndDate |
      | "nami@mailsac.com" | "Password@123" | "8"           | "4"         |

  #TC-A078
  Scenario Outline: Verify that entering more than 255 characters in Description is blocked or shows an error.
    Given admin on the login page
    And the email <email>
    And the password <password>
    When admin clicked login
    And admin select company modal
    Then admin see the dashboard
    And admin click edit icon
    And admin insert description <description>
    Then verify description max length
    And admin click create button
    Then admin see announcement banner

    Examples:
      | email              | password       |  description                                                                                                                                                                                                                                                                   |
      | "nami@mailsac.com" | "Password@123" | "(Test Edit) Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis,"  |
