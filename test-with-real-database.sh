#!/usr/bin/bash

# Supply user and admin_password for admin access

: ${HOST=localhost}
: ${PORT=8080}
: ${CA=--cacert /mnt/c/Users/kanta.munton/Documents/bench/ssl/rootCA.crt}

function assertCurl () {
  local expectedHttpCode=$1
  local curlCmd="$2 -w \"%{http_code}\""
  local result=$(eval $curlCmd)
  local httpCode="${result:(-3)}"
  RESPONSE='' && (( ${#result} > 3 )) && RESPONSE="${result%???}"

  if [ "$httpCode" = "$expectedHttpCode" ]
  then
    if [ "$httpCode" = "200" ]
    then
      echo "Test OK (HTTP Code: $httpCode)"
    else
      echo "Test OK (HTTP Code: $httpCode, $RESPONSE)"
    fi
  else
    echo  "Test FAILED, EXPECTED HTTP Code: $expectedHttpCode, GOT: $httpCode, WILL ABORT!"
    echo  "- Failing command: $curlCmd"
    echo  "- Response Body: $RESPONSE"
    exit 1
  fi
}

function assertEqual () {

  local expected=$1
  local actual=$2

  if [ "$actual" = "$expected" ]
  then
    echo "Test OK (actual value: $actual)"
  else
    echo "Test FAILED, EXPECTED VALUE: $expected, ACTUAL VALUE: $actual, WILL ABORT"
    exit 1
  fi
}

function testUrl () {
  url=$@
  if $url -ks -f -o /dev/null
  then
    return 0
  else
    return 1
  fi;
}

function waitForService () {
  url=$@
  echo -n "Wait for: $url... "
  n=0
  until testUrl $url
  do
    n=$((n + 1))
    if [[ $n == 1000 ]]
    then
      echo " Give up"
      exit 1
    else
      sleep 3
      echo -n ", retry #$n "
    fi
  done
  echo "DONE, continues..."
}

set -e

echo "Start Tests:" `date`

echo "HOST=${HOST}"
echo "PORT=${PORT}"

if [[ $@ == *"start"* ]]
then
  echo "Restarting the test environment..."
  echo "$ docker-compose down --remove-orphans"
  docker-compose down --remove-orphans
  echo "$ docker-compose up -d"
  docker-compose up -d
fi

waitForService curl $CA https://$HOST:$PORT/actuator/health

# Verify access to Eureka and that all eight microservices are # registered in Eureka

assertCurl 200 "curl $CA -H \"accept:application/json\" https://$user:$admin_password@$HOST:$PORT/eureka/api/apps -s"
assertEqual 9 $(echo $RESPONSE | jq ".applications.application | length")

assertCurl 401 "curl http://$HOST:9411/zipkin -s"
assertCurl 302 "curl http://$user:$admin_password@$HOST:9411/zipkin -s"

# Verify that a normal request works
assertCurl 200 "curl $CA https://$HOST:$PORT/api/v1/restaurants?distance=0.1 -s"
assertEqual 1 $(echo $RESPONSE | jq .site.id)

# Verify that a 422 (Unprocessable Entity) error is returned for a distance that is out of range (-1)
assertCurl 422 "curl $CA https://$HOST:$PORT/api/v1/restaurants?distance=-1 -s"
assertEqual "\"Invalid distance: -1\"" "$(echo $RESPONSE | jq .message)"

# Verify that a 422 error is returned for a distance that is not a number, i.e. invalid format
assertCurl 422 "curl $CA https://$HOST:$PORT/api/v1/restaurants?distance=invalid -s"
assertEqual "\"Distance is not a number\"" "$(echo $RESPONSE | jq .message)"


# Verify that a normal request works
assertCurl 200 "curl $CA https://$HOST:$PORT/api/v1/joke -s"
assertEqual "\"200\"" $(echo $RESPONSE | jq .status)

# Verify that a normal request works
assertCurl 200 "curl $CA https://$HOST:$PORT/api/v1/reviews/2 -s"
assertEqual 0 $(echo $RESPONSE | jq ".data | length")


# Verify that forbidden is returned with invalid csrf token
assertCurl 403 "curl $CA -X POST -H \"Content-Type: application/json\" -d '{\"rating\": 1, \"comment\": \"hello\", \"restuarantId\": 1}' https://$HOST:$PORT/api/v1/reviews/1 -s"
#assertEqual 0 $(echo $RESPONSE | jq ".data | length")

# Verify that a 422 error is returned for a restaurant id lower than 1, i.e. invalid format
assertCurl 422 "curl $CA https://$HOST:$PORT/api/v1/reviews/0 -s"
assertEqual "\"Invalid restaurant id: 0\"" "$(echo $RESPONSE | jq .message)"

# Verify that a 404 error is returned for a restaurant id not in database
assertCurl 404 "curl $CA https://$HOST:$PORT/api/v1/reviews/999999 -s"
assertEqual "\"No restaurant found with id: 999999\"" "$(echo $RESPONSE | jq .message)"

# Verify that a 400 (Bad Request) error error is returned for a restaurant id that is not a number, i.e. invalid format
assertCurl 400 "curl $CA https://$HOST:$PORT/api/v1/reviews/invalid -s"
assertEqual "\"Type mismatch.\"" "$(echo $RESPONSE | jq .message)"

# Verify access to Swagger and OpenAPI URLs - restaurant-composite
echo "Swagger/OpenAPI tests for restaurant-composite"
assertCurl 401 "curl $CA -s  https://$HOST:$PORT/api/v1/restaurants/openapi/swagger-ui.html"
assertCurl 302 "curl $CA -s  https://$user:$admin_password@$HOST:$PORT/api/v1/restaurants/openapi/swagger-ui.html"
assertCurl 200 "curl $CA -sL https://$user:$admin_password@$HOST:$PORT/api/v1/restaurants/openapi/swagger-ui.html"
assertCurl 200 "curl $CA -s  https://$user:$admin_password@$HOST:$PORT/api/v1/restaurants/openapi/v3/api-docs/swagger-config"
assertCurl 200 "curl $CA -s  https://$user:$admin_password@$HOST:$PORT/api/v1/restaurants/openapi/v3/api-docs"
assertEqual "3.0.1" "$(echo $RESPONSE | jq -r .openapi)"
assertCurl 200 "curl $CA -s  https://$user:$admin_password@$HOST:$PORT/api/v1/restaurants/openapi/v3/api-docs.yaml"

# Verify access to Swagger and OpenAPI URLs - joke
echo "Swagger/OpenAPI tests for joke"
assertCurl 401 "curl $CA -s  https://$HOST:$PORT/api/v1/joke/openapi/swagger-ui.html"
assertCurl 302 "curl $CA -s  https://$user:$admin_password@$HOST:$PORT/api/v1/joke/openapi/swagger-ui.html"
assertCurl 200 "curl $CA -sL https://$user:$admin_password@$HOST:$PORT/api/v1/joke/openapi/swagger-ui.html"
assertCurl 200 "curl $CA -s  https://$user:$admin_password@$HOST:$PORT/api/v1/joke/openapi/v3/api-docs/swagger-config"
assertCurl 200 "curl $CA -s  https://$user:$admin_password@$HOST:$PORT/api/v1/joke/openapi/v3/api-docs"
assertEqual "3.0.1" "$(echo $RESPONSE | jq -r .openapi)"
assertCurl 200 "curl $CA -s  https://$user:$admin_password@$HOST:$PORT/api/v1/joke/openapi/v3/api-docs.yaml"

# Verify access to Swagger and OpenAPI URLs - restaurant reviews
echo "Swagger/OpenAPI tests for review-composite"
assertCurl 401 "curl $CA -s  https://$HOST:$PORT/api/v1/reviews/openapi/swagger-ui.html"
assertCurl 302 "curl $CA -s  https://$user:$admin_password@$HOST:$PORT/api/v1/reviews/openapi/swagger-ui.html"
assertCurl 200 "curl $CA -sL https://$user:$admin_password@$HOST:$PORT/api/v1/reviews/openapi/swagger-ui.html"
assertCurl 200 "curl $CA -s  https://$user:$admin_password@$HOST:$PORT/api/v1/reviews/openapi/v3/api-docs/swagger-config"
assertCurl 200 "curl $CA -s  https://$user:$admin_password@$HOST:$PORT/api/v1/reviews/openapi/v3/api-docs"
assertEqual "3.0.1" "$(echo $RESPONSE | jq -r .openapi)"
assertCurl 200 "curl $CA -s  https://$user:$admin_password@$HOST:$PORT/api/v1/reviews/openapi/v3/api-docs.yaml"


if [[ $@ == *"stop"* ]]
then
    echo "We are done, stopping the test environment..."
    echo "$ docker-compose down"
    docker-compose down
fi

echo "End, all tests OK:" `date`