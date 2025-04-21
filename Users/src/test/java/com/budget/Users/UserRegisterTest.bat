@echo off
echo Testing registration...

:: Test 1: Valid Registration
echo Test 1: Valid Registration
curl -X POST http://localhost:8081/api/auth -H "Content-Type: application/json" -d "{ \"email\": \"test.user@example.com\", \"password\": \"StrongPass!1\", \"role\": \"USER\" }"
echo.

:: Test 2: Invalid Email, Valid Password
echo Test 2: Invalid Email, Valid Password
curl -X POST http://localhost:8081/api/auth -H "Content-Type: application/json" -d "{ \"email\": \"invalid-email\", \"password\": \"StrongPass!1\", \"role\": \"USER\" }"
echo.

:: Test 3: Valid Email, Invalid Password
echo Test 3: Valid Email, Invalid Password
curl -X POST http://localhost:8081/api/auth -H "Content-Type: application/json" -d "{ \"email\": \"test.user@example.com\", \"password\": \"weak\", \"role\": \"USER\" }"
echo.

:: Test 4: Duplicate Email
echo Test 4: Duplicate Email
curl -X POST http://localhost:8081/api/auth -H "Content-Type: application/json" -d "{ \"email\": \"test.user@example.com\", \"password\": \"StrongPass!1\", \"role\": \"USER\" }"
echo.

:: Test 5: Force General Error (e.g., force an exception in the controller)
echo Test 5: Force General Error
curl -X POST http://localhost:8081/api/auth -H "Content-Type: application/json" -d "{ \"email\": \"crash@test.com\", \"password\": "

:: NOTE: Test 5 error is handled by Spring before hitting controller,
:: so no custom message or UUID will be returned.
:: This is expected unless we implement a global @ControllerAdvice later.

echo.

pause
