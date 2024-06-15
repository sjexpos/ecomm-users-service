#!/usr/bin/env bash

export AWS_REGION=us-east-1
export AWS_DEFAULT_REGION=us-east-1
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_ENDPOINT=http://localhost:4566

cd infrastructure/spring-boot

mvn spring-boot:run

cd ../..

