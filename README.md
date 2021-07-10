# MyRetail
This is a demo for Rest API implementation

1) To retrieve product details(Product Price and Currency) from the database
2) Make external API calls to fetch product details like Name

#
Test the restful API

`http://localhost:8080/myretail/v1/products/51509273`

#
**Languages used**  
Kotlin,
Java,
Groovy

#
Run the project in local Docker

Prerequisites
1) Java
2) Docker
3) IDE like Intellij or Eclipse

Steps
1) Download/clone the project to your local system

2) Go to project folder and execute below command from the IDE terminal, this should start cassandra

`docker-compose up --build`

Setup cassandra separately

`docker run --name cassandra -d cassandra:latest`


3) Run MyRetailApplication in IDE


#
Manually running the Project

Prerequisites
1) Java
2) IDE like Intellij or Eclipse
3) Cassandra

Steps
1) Download the project to your local system

2) Have an IDE like Intellij or Eclipse

3) Install Java in system

4) Install Cassandra

5) Start Cassandra

6) Execute <Cassandra> cqlsh.sh

7) Execute CQL commands

8) Run MyRetailApplication from IDE

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