@REM ----------------------------------------------------------------------------
@REM ShopSphere Maven Wrapper Command Script for Windows
@REM ----------------------------------------------------------------------------
@echo off
setlocal

set "DIR=%~dp0"

if exist "%DIR%.tools\maven\apache-maven-3.9.9\bin\mvn.cmd" (
    "%DIR%.tools\maven\apache-maven-3.9.9\bin\mvn.cmd" %*
    exit /b %ERRORLEVEL%
)

if exist "%DIR%.mvn\wrapper\maven-wrapper.jar" (
    java -jar "%DIR%.mvn\wrapper\maven-wrapper.jar" %*
    exit /b %ERRORLEVEL%
)

where mvn >nul 2>nul
if %ERRORLEVEL% equ 0 (
    mvn %*
    exit /b %ERRORLEVEL%
)

echo [ERROR] Maven not found. Running setup-maven.ps1 to bootstrap portable Maven...
powershell -NoProfile -ExecutionPolicy Bypass -File "%DIR%setup-maven.ps1"

if exist "%DIR%.tools\maven\apache-maven-3.9.9\bin\mvn.cmd" (
    "%DIR%.tools\maven\apache-maven-3.9.9\bin\mvn.cmd" %*
    exit /b %ERRORLEVEL%
)

if exist "%DIR%.mvn\wrapper\maven-wrapper.jar" (
    java -jar "%DIR%.mvn\wrapper\maven-wrapper.jar" %*
    exit /b %ERRORLEVEL%
)

echo [ERROR] Unable to run Maven. Please run setup-maven.ps1 or install Apache Maven.
exit /b 1
