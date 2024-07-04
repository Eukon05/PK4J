# PK4J

[![](https://jitpack.io/v/Eukon05/Politechnika4J.svg)](https://jitpack.io/#Eukon05/Politechnika4J)

PK4J is a Java 17 library, that allows you to retrieve information from Cracow University Of Technology's e-management
system (EHMS).
It supports two methods of retrieving data from EHMS: HTML scraping, and utilising EHMS's internal rest API.

## Features

- Retrieving announcements from EHMS
- Retrieving the user's personal data, such as their name, surname, PESEL number, album number, etc.
- Multiple active sessions (users) at once

## Usage

- Get an instance of the PK4J class. You can specify if you want to use the web scraper, or the rest api:  
  `PK4J api = PK4J.getInstance(); // This will get you the scraper-based implementation`  
  `PK4J apiRest = PK4J.getInstance(true); // This will get you the rest-based implementation`

- Create a new `EHMSUser` object that will store your EHMS credentials:  
  `EHMSUser user = EHMSUser.fromCredentials("yourlogin", "yourpassword");`

- Use your instance of the `PK4J` class with your `EHMSUser` object to perform requests to EHMS, for example:  
  `List<Announcement> announcements = api.getAnnouncements(user);`

## Credits

This project is made possible thanks to the JSoup team and their html parser.  
Special thanks to [PIayer69 and his ehmsChecker project](https://github.com/PIayer69/ehmsChecker). Without it, I would
probably still be wondering how to properly scrape data from EHMS ;)
