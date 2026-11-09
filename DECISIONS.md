# DECISIONS MADE

### Problems fixed  
1. Restructured the files to a more modern file system and used maven
2. Removed duplicate code in separate classes and put in a singular class
3. Changed field modifiers to private for better encapsulation.
4. Changed LoanExtent to check for duplicate borrowing and only allow students to borrow games.
5. Added overdue and return surcharge to Loan class
6. File output service repaired.
7. Audit logger implemented independently as console output with future integration to TraceAuditLogger pending.
8. Implemented handling of concurrent actions.
9. Bash script to run in console without IDE added

### Problems left & Changes to be made
- Monolithic class structure was left because it would be a massive code rewrite to restructure and consider with SOLID principles. To rebuild the system, I would separate business logic more clearly, using interfaces to reduce coupling and make the system easier to maintain and test.
- Multiple methods unused and warnings given at compile time. These are left due to same reasons as above.
- Data storage inside of methods. Would require a database structure to be written to change it.
- Include more resource types for normal libraries such as films.
- Original 3 loans are run through the LoanExtent constructor directly and skip the auditing. This was left as it was used as example and would need separate audit handling or be considered not preexisting.
- Priority Members reservations and monthly payments have not been implemented. This would need a lot of clarification as well as consideration to implement.

### Assumptions  
- Requirements state only students can borrow games, code states students and premium users. Former is implemented.
- Premium users can borrow 10 items. All other users are capped at 5.
- Return surcharge is charged per day.

### AI Use  
- Helped to create pom.xml with future requirements
- Guidance for changing the functioning of the FileWriter
- Help with making audit more robust
- Final concurrency checks to add that I missed earlier and the test for it
- Bash script to run in console without IDE