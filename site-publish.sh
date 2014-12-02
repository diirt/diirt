#!/bin/bash
cd target
rm -rf maven-site
git clone git@github.com:diirt/maven-site.git
cd maven-site
rm -rf *
cp -R ../staging/* .
git add -A
git commit --author="Gabriele Carcassi <gabriele.carcassi@gmail.com>" -m "Website update"
git push origin
