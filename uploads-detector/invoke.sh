#!/bin/bash

helpFunction()
{
  echo "Usage: $0 [OPTIONS]"
  echo ""
  echo "   Bash script to run lambda locally"
  echo ""
  echo "Options:"
  echo -e "\t-e [local|dev|qa|stage|prod]   Environment to connect"
  echo -e "\t-p <port number>               debugging port"
  echo ""
  exit 1 # Exit script after printing help
}

while getopts "e:p:" opt
do
  case "$opt" in
    e ) parameterEnv="$OPTARG" ;;
    p ) parameterPort="$OPTARG" ;;
    ? ) helpFunction ;; # Print helpFunction in case parameter is non-existent
  esac
done

if [ -z "$parameterEnv" ]
then
   echo "Environment parameter is required";
   helpFunction
fi

if [ "$parameterEnv" != "local" ] && [ "$parameterEnv" != "dev" ] && [ "$parameterEnv" != "qa" ] && [ "$parameterEnv" != "stage" ] && [ "$parameterEnv" != "prod" ]
then
  echo "Invalid environment value: $parameterEnv"
  helpFunction
fi

red='\033[0;31m'
blue='\033[0;34m'
cyan='\033[0;36m'
green='\033[0;32m'
yellow='\033[0;33m'
red_bold='\033[1;31m'
blue_bold='\033[1;34m'
cyan_bold='\033[1;36m'
green_bold='\033[1;32m'
white_bold='\033[1;37m'
yellow_bold='\033[1;33m'
NC='\033[0m' # No Color

DEBUGGING=""
if [ ! -z "$parameterPort" ]
then
  DEBUGGING="-d $parameterPort"
fi

ENVIRONMENT=$parameterEnv
SAM_CMD=sam
AWS_REGION=us-east-1

SECURITY_GROUP=""
BACKEND_SUBNETS=""
DOCKER_NETWORK=""
if [ "$ENVIRONMENT" == "local" ]
then
  SECURITY_GROUP=""
  BACKEND_SUBNETS=""
  DOCKER_NETWORK="--docker-network appthing_backend"
fi

cd target

#	--debug \
$SAM_CMD local invoke $DEBUGGING $DOCKER_NETWORK \
    --event ../event.json \
	--template template.yaml ${SECURITY_GROUP} ${SUBNETS} \
	S3NotificationLambdaFunction \
	--parameter-overrides \
		UsersServiceUri="http://172.16.208.1:8080/api"

cd ..

