Jatyta Web 
==========

Jatyta Web es una herramienta desarrollada a partir de Crawljax Web UI para automatizar el testing
de aplicaciones Web dinámicas.
Se puede extender esta herramienta, implementando plugins [plugin architecture](https://github.com/crawljax/crawljax/wiki/Writing-a-plugin).
Para más información acerca de Crawljax, puede visitar [Crawljax website](http://crawljax.com).
Ésta herramienta fue desarrollada como aporte del Proyecto Final de Grado de la Carrera de Ingeniería en Informática de la Facultad de Politécnica de la Universidad Nacional de Asunción

Autores
-------

Univ. Miguel Angel Giménez, miguel.gimenez@pol.una.py
Univ. Alberto Ramón Espínola Recalde,  bettopindu@gmail.com
Phd. Juan Ignacio Pane, jpane@pol.una.py

Documentación
-------------

Se puede encontrar documentación acerca de Crawljax [Crawljax wiki](https://github.com/crawljax/crawljax/wiki/). 


Licencia
--------

Este proyecto está bajo la licencia ["Apache License, Version 2.0"](https://github.com/crawljax/crawljax/blob/master/LICENSE).


Ejecución
---------
Clonar el proyecto git, luego compilar con:

```
usage: mvn clean package
```

Para crear la base de datos kbjatyta se debe ejecutar el script ubicado en el proyecto:

```
psql  -f "src/main/database/20161107_create_db.sql" -h localhost -p 5432 -U postgres -v -d postgres 
```

Luego se deben crear las tablas, ejecutando el script: 

```
psql  -f "src/main/database/20161107_kbjatyta_schema_only.sql" -h localhost -p 5432 -U postgres -v -d kbjatyta 
```


Una vez compilado el proyecto y creada la base de datos, ejecutar la aplicación con:

```
usage: mvn exec:java -Dexec.mainClass="com.crawljax.web.Main"
```
