# Demo_Nave
Proyecto demo para la prueba tecnica


### 1. Clonar la aplicación o descomprime el fichero 

### 2. Levante los servicios con docker compose

Para levantar los contenedores definidos en el docker_compose.yml se puede utilizar el siguiente comando:

Primero situate en la carpeta raiz donde esta el documento docker-compose.yml para poder ejecutar el comando.

```bash
docker-compose up -d
```

Y después de esto si no te falla ya te levante los servicios
- Mysql 
- Redis
- Aplicación sprintboot (lanzado los test unitario con mvn test)

### 3. Accede a la aplicación en postman para realizar las pruebas
http://localhost:8080/api/nave

### 4. Documentación OpenAPI
http://localhost:8080/swagger-ui/index.html