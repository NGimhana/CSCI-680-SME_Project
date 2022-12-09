#!/bin/bash

WORKING_DIR='/Users/bimalkadesilva/Documents/Fall2022/CSCI680-SME/Project/BLIZZARD-Replication-Package-ESEC-FSE2018'
test="$PWD"
echo "${test}"



# filename=${WORKING_DIR}/nadeeshan/projectList.txt
# n=1
# projectList=()
# ## Project List
# while read line; do
#     projectList+=(${line})
# done < $filename

# for project in ${projectList[@]}; do
#     ## Write bug id to a file
#     IFS='-'     # "-" is the delimiter
#     read -ra ADDR <<< "${project}"   # ${project} is read into an array as tokens separated by IFS
#     for ((i=1;i< ${#ADDR[@]} ;i+=2));
#     do
#         echo ${ADDR[i]} > "${WORKING_DIR}"/sample-input/bug-"${ADDR[i]}"-bugs.txt
#         java -jar "${WORKING_DIR}"/Blizzard_Runner_Final.jar -task reformulateQuery -repo "${project}" -bugIDFile "${WORKING_DIR}"/sample-input/bug-"${ADDR[i]}"-bugs.txt -queryFile "${WORKING_DIR}"/sample-input/bug-"${ADDR[i]}"-query.txt
#     done

# done