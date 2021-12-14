# Comandos de Docker necesarios para el desarrollo

## Base de datos  
### Crear la base de datos
```
docker run --name=mysql -p 3306:3306 -d dimonra13/mydoiinfodb:v1
```
### Iniciar la base de datos (previamente creada)
```
docker start mysql
```
### Parar la base de datos
```
docker stop mysql
```
### Borrar la base de datos
```
docker rm mysql
```
## Base de datos para el testing
### Crear la base de datos
```
docker run --name=mysqltest -p 3306:3306 -d dimonra13/mydoiinfotestdb:v1
```
### Iniciar la base de datos (previamente creada)
```
docker start mysqltest
```
### Parar la base de datos
```
docker stop mysqltest
```
### Borrar la base de datos
```
docker rm mysqltest
```
## Ejecutar la aplicación en un entorno de desarrollo local
### Crear el contenedor con la aplicación
```
docker run --name=mydoiinfoapp -d --network=host dimonra13/mydoiinfoapp:v1
```
### Ejecutar la aplicación (previamente creada)
```
docker start mydoiinfoapp
```
### Parar la ejecución de la aplicación
```
docker stop mydoiinfoapp
```
### Borrar el contenedor con la aplicación
```
docker rm mydoiinfoapp
```