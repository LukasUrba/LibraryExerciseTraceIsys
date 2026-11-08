# DECISIONS MADE

### Problems fixed  
1. Restructured the files to a more modern file system and used maven
2. Removed duplicate code in separate classes and put in a singular class
3. Changed field modifiers to private for better encapsulation.

### Problems left  


### Assumptions  
- Requirements state only students can borrow games, code states students and premium users. Former is implemented.
- Premium users can borrow 10 items. All other users are capped at 5.

### Changes to be made  
- Include more resource types for normal libraries such as films.
- Break up the monolithic createLoan method into single responsibility methods

### AI Use  
- Helped to create pom.xml with future requirements