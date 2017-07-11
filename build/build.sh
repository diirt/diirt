#!/bin/bash
set -ev

function doCompile {
  mvn clean verify -e
}

function doCompileWithDeploy {
  echo "<settings><servers><server><id>s3.site</id><username>\${env.S3USER}</username><password>\${env.S3PASS}</password></server></servers></settings>" > ~/settings.xml
  mvn clean verify --settings ~/settings.xml -PuploadRepo -e
}

function catTests {
  find ./ -type d -name "surefire-reports" -print0 | xargs -0 -I {} find {} -iname "*.txt" -type f | xargs cat
}

# Pull requests and commits to other branches shouldn't try to deploy, just build to verify
if [[ [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$REPO_ORG" == "diirt" ] && [ "$TRAVIS_BRANCH" =~  diirt-all-[0-9]+\.[0-9]+\.[0-9] ] ]]; then
    echo "Deploying"
    doCompileWithDeploy
    catTests
else
    echo "Skipping deploy; just doing a build."
    doCompile
    catTests
fi