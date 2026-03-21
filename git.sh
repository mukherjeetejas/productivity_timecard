#!/bin/bash
git status
git add .
git commit -m "$1"
git push
echo "commit $1 pushed to repository"