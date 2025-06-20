@echo off
echo Starting Restaurant Microservices with Service Discovery...
echo.

echo ================================
echo Starting Config Server (Port 8888)
echo ================================
start cmd /k "cd config-server && mvn spring-boot:run"
timeout /t 15 /nobreak > nul

echo ================================
echo Starting Eureka Server (Port 8761)
echo ================================
start cmd /k "cd eureka-server && mvn spring-boot:run"
timeout /t 20 /nobreak > nul

echo ================================
echo Starting User Service (Port 8081)
echo ================================
start cmd /k "cd user-service && mvn spring-boot:run"
timeout /t 10 /nobreak > nul

echo ================================
echo Starting Menu Service (Port 8082)
echo ================================
start cmd /k "cd menu-service && mvn spring-boot:run"

echo.
echo ================================
echo All services starting...
echo.
echo Config Server:  http://localhost:8888
echo Eureka Server:  http://localhost:8761
echo User Service:   http://localhost:8081/api/users/info
echo Menu Service:   http://localhost:8082/api/menu/info
echo.
echo Service Communication Test:
echo http://localhost:8081/api/users/communicate-with-menu
echo http://localhost:8082/api/menu/communicate-with-user
echo.
echo Press any key to continue...
pause
