# Counterpoint - Real-Time AI Debate Platform
# PowerShell startup script

Write-Host "Starting Counterpoint..." -ForegroundColor Cyan

# Start backend
Write-Host "`nStarting Spring Boot backend..." -ForegroundColor Yellow
$backend = Start-Process -FilePath "powershell" -ArgumentList "-NoExit", "-Command", "cd demo; ./mvnw spring-boot:run" -PassThru -WorkingDirectory $PSScriptRoot

# Wait for backend to start
Write-Host "Waiting for backend to initialize..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

# Start frontend
Write-Host "`nStarting React frontend..." -ForegroundColor Yellow
$frontend = Start-Process -FilePath "powershell" -ArgumentList "-NoExit", "-Command", "cd front; npm run dev" -PassThru -WorkingDirectory $PSScriptRoot

Write-Host "`n✅ Counterpoint is starting!" -ForegroundColor Green
Write-Host "Backend: http://localhost:8081" -ForegroundColor Cyan
Write-Host "Frontend: http://localhost:5173" -ForegroundColor Cyan
Write-Host "`nPress any key to stop all services..." -ForegroundColor Gray

$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

# Cleanup
Write-Host "`nStopping services..." -ForegroundColor Yellow
Stop-Process -Id $backend.Id -Force -ErrorAction SilentlyContinue
Stop-Process -Id $frontend.Id -Force -ErrorAction SilentlyContinue
Write-Host "Done!" -ForegroundColor Green
