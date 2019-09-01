This is my final project for C195 - an appointment application. Requirements met as follows:

    A. Log-in form determine's user's location using the user's system's default timezone.
    B. Provide the ability to add, update, and delete customer records in the database, including name, address, and phone number.
    C. Provide the ability to add, update, and delete appointments, capturing the type of appointment and a link to the specific customer record in the database.
        The customer's name and phone number are retrieved by selecting an appointment in the available table and clicking the "Get Customer" button, demonstrating a link.
    D. Provide the ability to view the calendar by month and by week.
        Achieved through use of a TabPane.
    E. Provide the ability to automatically adjust appointment times based on user time zones and daylight savings time.
        Achieved automatically through use of ZonedDateTime conversion methods found in utils/DateTimeConverter.java
    F. Write exception controls to prevent each of hte following. You may use the same mechanism of exception control more than once, but you msut incorporate at least two different mechanisms of exception control.
        Scheduling an appointment outside business hours
            Business hours are defined as 8-5 UTC. An If>Then is used to alert the user. If OK is selected, the appointment is created. If not, creation is stopped.
        Scheduling overlapping appointments    
            User/consultant and customer IDs is not considered for overlapping appointments at this time
        Entering nonexistent or invalid customer database
            Necessary fields are required at multiple levels. Exceptions are caught through an if>then, throws, AND try catch blocks at various points in the program.
        Entering an incorrect username and password
             Valid username/passwords include test/test, ryan/ryan, and wanda/wanda.
    G. Write two or more lambda expression to make your program more efficient, justifying the use of each lambda expression with an in-line commend.
        Lambda alerts are found throughout the program. Alerts in the views/LoginController.java file have inline comments.
    H. Write code to provide an alert if there is an appointment within 15 minutes of the user's log-in.
    I. Provide the ability to generate each of the following reports:
        Number of appointment types by month
        The schedule for each consultant   
            This displays the next year's of appointments, rather than from all time, for the sake of practicality
        One additional report of your choice
            A schedule for each customer has been created
    J. Provide the ability to track user activity by recording timestamps for user log-ins in a .txt file. Each new record should be appended to the log file, if the log file already exists.
        See utils/Logger.java
    K. Demonstrate professional communication in the context and presentation of your submission.