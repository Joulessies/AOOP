@echo off
echo Starting Cofitearia Milktea Inventory and Sales Management System...
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Java is not installed or not in PATH
    echo Please install Java 17 or higher and try again
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Maven is not installed or not in PATH
    echo Please install Maven and try again
    pause
    exit /b 1
)

echo Building the application...
call mvn clean compile

if %errorlevel% neq 0 (
    echo Error: Build failed
    pause
    exit /b 1
)

echo.
echo Starting the application...
echo.
echo Accessibility Features:
echo - Press F1 for help
echo - Press F2 to toggle high contrast mode
echo - Press F3 to toggle large text mode
echo - Press F4 to toggle accessibility mode
echo - Use Tab to navigate between fields
echo - Use Enter to activate buttons
echo.

call mvn javafx:run

pause
