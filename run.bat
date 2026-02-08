@echo off
REM Academic Seminar Management System - Build & Run Script for Windows

echo =========================================
echo Academic Seminar Management System
echo =========================================
echo.

REM Clean previous build
echo Cleaning previous build...
del /S /Q *.class 2>nul
if exist sources.txt del sources.txt

REM Find all Java source files
echo Finding Java source files...
dir /s /B *.java > sources.txt

REM Compile
echo.
echo Compiling Java files...
javac -d . @sources.txt

if %ERRORLEVEL% EQU 0 (
    echo.
    echo [SUCCESS] Compilation successful!
    echo.
    
    REM Run the application
    echo Launching application...
    echo.
    java gui.AppGUI
) else (
    echo.
    echo [ERROR] Compilation failed!
    pause
    exit /b 1
)
