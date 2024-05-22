#!/usr/bin/env bash

set -e

for f in resources/*.json; do
    echo $f
    lein run < $f
    echo ""
done
