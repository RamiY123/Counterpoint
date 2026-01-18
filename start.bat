@echo off
echo Starting Counterpoint...
echo.

REM Start backend in new window
echo Starting Spring Boot backend...
start "Counterpoint Backend" cmd /k "cd demo && mvnw spring-boot:run"

REM Wait for backend
echo Waiting for backend to initialize...
timeout /t 10 /nobreak > nul

REM Start frontend in new window
echo Starting React frontend...
start "Counterpoint Frontend" cmd /k "cd front && npm run dev"

echo.
echo Counterpoint is starting!
echo Backend: http://localhost:8081
echo Frontend: http://localhost:5173
echo.
pause
