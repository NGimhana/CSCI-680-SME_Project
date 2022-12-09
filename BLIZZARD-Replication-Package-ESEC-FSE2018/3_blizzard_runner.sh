#!/bin/bash

WORKING_DIR='/Users/bimalkadesilva/Documents/Fall2022/CSCI680-SME/Project/BLIZZARD-Replication-Package-ESEC-FSE2018'


filename=${WORKING_DIR}/nadeeshan/projectList.txt
n=1
projectList=()
## Project List
while read line; do
    projectList+=(${line})
done < $filename


for project in ${projectList[@]}; do
    ## Write bug id to a file
    IFS='-'     # "-" is the delimiter
    read -ra ADDR <<< "${project}"   # ${project} is read into an array as tokens separated by IFS
    for ((i=1;i< ${#ADDR[@]} ;i+=2));
    do
        echo ${ADDR[i]} > "${WORKING_DIR}"/sample-input/bug-"${ADDR[i]}"-bugs.txt
        java -jar "${WORKING_DIR}"/Blizzard_Runner_Final.jar -task getResult -repo bug-"${ADDR[i]}"  -queryFile ./sample-input/bug-"${ADDR[i]}"-query.txt -topk 1  -resultFile ./sample-output/bug_wise_results/bug-"${ADDR[i]}"-results.txt >> ./sample-output/results/results_1.txt
        java -jar "${WORKING_DIR}"/Blizzard_Runner_Final.jar -task getResult -repo bug-"${ADDR[i]}"  -queryFile ./sample-input/bug-"${ADDR[i]}"-query.txt -topk 2  -resultFile ./sample-output/bug_wise_results/bug-"${ADDR[i]}"-results.txt >> ./sample-output/results/results_2.txt
        java -jar "${WORKING_DIR}"/Blizzard_Runner_Final.jar -task getResult -repo bug-"${ADDR[i]}"  -queryFile ./sample-input/bug-"${ADDR[i]}"-query.txt -topk 3  -resultFile ./sample-output/bug_wise_results/bug-"${ADDR[i]}"-results.txt >> ./sample-output/results/results_3.txt
        java -jar "${WORKING_DIR}"/Blizzard_Runner_Final.jar -task getResult -repo bug-"${ADDR[i]}"  -queryFile ./sample-input/bug-"${ADDR[i]}"-query.txt -topk 4  -resultFile ./sample-output/bug_wise_results/bug-"${ADDR[i]}"-results.txt >> ./sample-output/results/results_4.txt
        java -jar "${WORKING_DIR}"/Blizzard_Runner_Final.jar -task getResult -repo bug-"${ADDR[i]}"  -queryFile ./sample-input/bug-"${ADDR[i]}"-query.txt -topk 5  -resultFile ./sample-output/bug_wise_results/bug-"${ADDR[i]}"-results.txt >> ./sample-output/results/results_5.txt
        java -jar "${WORKING_DIR}"/Blizzard_Runner_Final.jar -task getResult -repo bug-"${ADDR[i]}"  -queryFile ./sample-input/bug-"${ADDR[i]}"-query.txt -topk 6  -resultFile ./sample-output/bug_wise_results/bug-"${ADDR[i]}"-results.txt >> ./sample-output/results/results_6.txt
        java -jar "${WORKING_DIR}"/Blizzard_Runner_Final.jar -task getResult -repo bug-"${ADDR[i]}"  -queryFile ./sample-input/bug-"${ADDR[i]}"-query.txt -topk 7  -resultFile ./sample-output/bug_wise_results/bug-"${ADDR[i]}"-results.txt >> ./sample-output/results/results_7.txt
        java -jar "${WORKING_DIR}"/Blizzard_Runner_Final.jar -task getResult -repo bug-"${ADDR[i]}"  -queryFile ./sample-input/bug-"${ADDR[i]}"-query.txt -topk 8  -resultFile ./sample-output/bug_wise_results/bug-"${ADDR[i]}"-results.txt >> ./sample-output/results/results_8.txt
        java -jar "${WORKING_DIR}"/Blizzard_Runner_Final.jar -task getResult -repo bug-"${ADDR[i]}"  -queryFile ./sample-input/bug-"${ADDR[i]}"-query.txt -topk 9  -resultFile ./sample-output/bug_wise_results/bug-"${ADDR[i]}"-results.txt >> ./sample-output/results/results_9.txt
        java -jar "${WORKING_DIR}"/Blizzard_Runner_Final.jar -task getResult -repo bug-"${ADDR[i]}"  -queryFile ./sample-input/bug-"${ADDR[i]}"-query.txt -topk 10  -resultFile ./sample-output/bug_wise_results/bug-"${ADDR[i]}"-results.txt >> ./sample-output/results/results_10.txt
    done
done