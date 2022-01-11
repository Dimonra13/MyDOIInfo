## Desplegar la base de datos  

Para desplegar la base de datos dentro del clúster debemos seguir los siguientes pasos desde la carpeta con los archivos .yaml para la base de datos (kubernetes/DB):  

### Desplegar el Volumen Persistente

Este volumen es necesario para que los datos almacenados en la base de datos MySQL no se pierdan al reiniciar el contenedor o el deployment.

```
kubectl create -f pv.yaml
```

### 5.3. Desplegar el Volume Claim para el volumen persistente

```
kubectl create -f pv_claim.yaml
```
### 5.4. Desplegar el Secret 
Este secret es necesario para almacenar la información sensible de la base de datos (nombre y contraseña).

```
kubectl create -f db_secret.yaml
```
### 5.5. Desplegar el Deployment

```
kubectl create -f db_deployment.yaml
```

### 5.6. Desplegar el Servicio

```
kubectl create -f db_service.yaml
```

### Acceder a la base de datos MySQL con un pod cliente

Esto puede ser muy útil para cargar dumps, comprobar que los datos se guardan correctamente y acceder a logs de errores.

```
kubectl run -it --rm --image=mysql:5.6 --restart=Never mysql-client -- mysql -h mysql -p6mf3ZrmxPhNi
	
```

## Desplegar la app

Para desplegar la app correctamente y que el cliente web y la API sean accesibles desde el exterior del cluster debemos seguir los siguientes pasos desde la carpeta con los archivos .yaml para la aplicación (kubernetes/App):

### Desplegar el Deployment de la App

```
kubectl create -f app_deployment.yaml
```

### Desplegar el Load Balancer de la App

Los load balancer permiten que la aplicación sea accesible a través de internet y reparten el tráfico entre los diferentes nodos y pods de la aplicaciónen función de la tasa de uso que tenga cada uno. El load balancer que crearemos mediante el archivo yaml será detectado por DigitalOcean y se integrará con su propio servicio de load balancing.
```
kubectl create -f app_do_loadBalancer.yaml
```
