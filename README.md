# Politechnika4J (PK4J)

PK4J is a Java 8 library, that allows you to retrieve information from Cracow University Of Technology's e-management
system (EHMS).

This library is currently in very early stages of development, so expect things to break and features to be missing.

PK4J is, essentially, a web-scraper, that wraps information gathered from EHMS into Java objects, for use in other
applications.

## Features (subject to change)

- Retrieving announcements from EHMS

## Usage (subject to change)

- Login to EHMS and retrieve a `Student` object containing your EHMS session:

```java 
Student student=PK4J.authenticate(yourlogin,yourpassword);
```

- From the `Student` object, you can make calls to EHMS, for example, you can retrieve all of your announcements:

```java
List<Announcement> announcements=student.getAnnouncements();

        announcements.forEach(System.out::println);
```

## Credits

This project is made possible thanks to the JSoup team and their html parser.
Special thanks to [PIayer69 and his ehmsChecker project](https://github.com/PIayer69/ehmsChecker). Without it, I would
probably still be wondering how to properly scrape data from EHMS.
