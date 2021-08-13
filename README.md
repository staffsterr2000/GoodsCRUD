A CRUD program for managing goods. For an iteration you can:
- create a thing ('-c' predicate);
- update a thing ('-u' predicate);
- delete a thing ('-d' predicate).

Using:
1. Run the application.
2. Enter either the path to existing .txt database with goods or
enter a path to abscent .txt file (for example my_file.txt)
and it will be created at the moment.
3. Enter predicate (they are above)
3.1. For creation you need to enter the thing's description, price
and how many you have in reserve.
3.2. While deleting you need to enter the thing's ID.
3.3. While updating you need to enter the thing's ID and the steps
from creation.

Launching:
1. Clone the project
2. With cmd go to the project folder
3. Then go to the 'src' folder
4. Run command 'java test.Main'