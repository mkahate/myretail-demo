#!/bin/bash


KEYSPACE=my_retail
HOST=localhost

# Setup app keyspace

docker exec -it cassandra cqlsh -e "DROP KEYSPACE IF EXISTS $KEYSPACE;"
docker exec -it cassandra cqlsh -e "CREATE KEYSPACE IF NOT EXISTS $KEYSPACE WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };"

FILES=`ls -1 ./db/cassandra/table`
for FILE in $FILES; do
    echo "docker exec -it cassandra cqlsh -k $KEYSPACE -f /scripts/table/$FILE"
    docker exec -it cassandra cqlsh -k ${KEYSPACE} -f /scripts/table/$FILE
done

exit $?
