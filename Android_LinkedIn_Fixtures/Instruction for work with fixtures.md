This is template instruction about using fixtures and server "vcr_akvelon.js"

1)	How use it:
When you start testing run script “run_fixtures.bat” (in Windows) from “Fixtures_Android” (separate branch in SVN for fixtures).
Write in start of test instruction “startFixtures("XXXXXXXX");”
When you run tests, fixtures will be changed automatically!
2)	How it work:
Script “run_fixtures.bat” (only for Win, for MACs instead type in terminal “./vcr_akvelon.js replay”) starts test server 'vcr_akvelon.js'.
Function “startFixtures” sends http request to localhost (vcr_akvelon.js) – something like “start fixture XXXXXXXX”.
vcr_akvelon.js running fixture from folder “XXXXXXXX” and send response – something like “successful”.
After that, function “startFixtures” over, and test continue.
In end of test automatically sends request “stop fixtures”.
3)	Notes:
- This is worked “as is” only for emulators. If you use device, then “localhost” (10.0.2.2) – undefined IP. Change constant “SERVICE_CONNECTION_STRING” in “com.linkedin.android.fixtures.ServerRequestResponseUtils” with IP of your machine where running test server (vcr_akvelon.js).
- For RECORD fixtures type in terminal "node vcr_akvelon.js replay,folder_for_fixture,with_counters" ("./vcr_akvelon.js replay,folder_for_fixture,with_counters" in MACs and other UNIXes). 3 parameter "with_counters" is not nessesary - it you not need in it then not write it and comma.
- For use tests you need only start 'vcr_akvelon.js' and run tests - all automatically. For it run “run_fixtures.bat” (in Windows) or type in terminal “./vcr_akvelon.js replay” (MACs and UNIXes).