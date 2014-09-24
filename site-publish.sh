#!/bin/bash
cd target
git clone git@github.com:diirt/pvmanager-pages.git
cd pvmanager-pages
rm -rf *
cp -R ../site .
git commit --author="Gabriele Carcassi <gabriele.carcassi@gmail.com>" -m "Website update" .
git push origin
