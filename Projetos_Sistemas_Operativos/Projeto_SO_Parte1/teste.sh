#!/bin/bash

j=847474hh
i=$( echo $j | awk '/[a-zA-Z]/' )
echo "$i"
echo "\$j"
