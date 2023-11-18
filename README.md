# Politechnika4J (PK4J)

[![](https://jitpack.io/v/Eukon05/Politechnika4J.svg)](https://jitpack.io/#Eukon05/Politechnika4J)

PK4J is a Java 17 library, that allows you to retrieve information from Cracow University Of Technology's e-management
system (EHMS).

This library is currently in very early stages of development, so expect things to break and features to be missing.  
Everything on this page is subject to change, as the library evolves.

PK4J utilises JSoup to parse EHMS's HTML code into Java objects, but because of it's decoupled nature, in the future it
will be possible to easily allow it to retrieve data using the newly published EHMS REST api, instead of scraping the
website.

## Features (subject to change)

- Retrieving announcements from EHMS
- Retrieving the user's personal data, such as their name, surname, PESEL number, album number, etc.

## Usage

- Get an instance of the PK4J class - right now the only available variant is the scraper based one, but in the future
  you'll have the ability to force the library to use EHMS's new REST api  
  `PK4J api = PK4J.getInstance();`

- Create a new `EHMSUser` object to create a session in EHMS:  
  `EHMSUser user = EHMSUser.fromCredentials("yourlogin", "yourpassword");`

- Use your instance of the `PK4J` class with your `EHMSUser` object to perform requests to EHMS, for example:  
  `List<Announcement> announcements = api.getAnnouncements(user);`

## Credits

This project is made possible thanks to the JSoup team and their html parser.  
Special thanks to [PIayer69 and his ehmsChecker project](https://github.com/PIayer69/ehmsChecker). Without it, I would
probably still be wondering how to properly scrape data from EHMS ;)

I'd also like to thank Kalasoft, the creators of EHMS, for making its REST api publicly available right after I finished
writing this scraper...