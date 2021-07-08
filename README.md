# MyRetail
MyRetail Project for HQ in Richmond, VA

#
Swagger UI

`http://localhost:8080/swagger-ui.html`

Api Docs

`http://localhost:8080/v2/api-docs`

Test the restful API

`http://localhost:8080/myretail/v1/products/51509273`

#


**Languages used**  
Kotlin,
Java,
Groovy

#
Run the Project in local Docker

Prerequisites
1) Java
2) Docker
3) IDE like Intellij or Eclipse

Steps
1) Download the project to any local folder

2) Go to project folder and execute
This should start cassandra

`docker-compose up --build`

Setup cassandra seperately

`docker run --name cassandra -d cassandra:latest`


3) Run MyRetailApplication in IDE


#
Manually running the Project

Prerequisites
1) Java
2) IDE like Intellij or Eclipse
3) Cassandra

Steps
1) Download the project to any local folder

2) Have an IDE like Intellij or Eclipse

3) Install Java in system

4) Install Cassandra

5) Start Cassandra

6) execute <Cassandra> cqlsh.sh

7)
Execute CQL commands
--Create keyspace

`CREATE KEYSPACE IF NOT EXISTS my_retail WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };`


`CREATE TABLE IF NOT EXISTS product (
    product_id text,
    currency_code text,
    value decimal,
    PRIMARY KEY((product_id), currency_code)
);`

`INSERT INTO product (product_id, currency_code, value) VALUES ('51509273', 'USD', 10.99);
INSERT INTO product (product_id, currency_code, value) VALUES ('54154777', 'USD', 20.99);
INSERT INTO product (product_id, currency_code, value) VALUES ('13860428', 'USD', 22.99);
INSERT INTO product (product_id, currency_code, value) VALUES ('13860428', 'INR', 1098.99);
INSERT INTO product (product_id, currency_code, value) VALUES ('51446183', 'USD', 30.99);`

8) Run MyRetailApplication in IDE
