# Politechnika4J (PK4J)

PK4J is a Java 17 library, that allows you to retrieve information from Cracow University Of Technology's e-management
system (EHMS).

This library is currently in very early stages of development, so expect things to break and features to be missing.  
Everything on this page is subject to change, as the library evolves.

PK4J is, essentially, a web-scraper, that wraps information gathered from EHMS into Java objects, for use in other
applications.

## Features (subject to change)

- Retrieving announcements from EHMS

## Usage

- Create a new `EHMSUser` object to create a session in EHMS:  
  `EHMSUser user = new EHMSUser(yourlogin, yourpassword)`

- Use `PK4J` class with your `user` object to perform requests to EHMS. This example shows how to retrieve announcements
  from EHMS:  
  `List<Announcement> announcements = PK4J.getAnnouncements(user)`

## Credits

This project is made possible thanks to the JSoup team and their html parser.  
Special thanks to [PIayer69 and his ehmsChecker project](https://github.com/PIayer69/ehmsChecker). Without it, I would
probably still be wondering how to properly scrape data from EHMS ;)