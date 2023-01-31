import os

root_location = "/Users/bimalkadesilva/Desktop/Final-SME/CSCI-680-SME_Project/BLIZZARD-Replication-Package-ESEC-FSE2018"

## Issue List
issue_id_list_file = open(root_location + '/nadeeshan/projectList.txt')
temp_issues = issue_id_list_file.read().splitlines()

issues = []

for issue in temp_issues:
    issues.append(issue.split("-")[1])

## Issue List
issue_id_list_file = open(root_location + '/nadeeshan/projectList.txt')
temp_issues = issue_id_list_file.read().splitlines()

issues = []
   
for issue in temp_issues:
    issues.append(issue.split("-")[1])

# T1 - Original
# dir_list = os.listdir(root_location + '/nadeeshan/original_queries')

# T2-1 - original
dir_list = os.listdir(root_location + '/nadeeshan/t4_3')

## Issues in the andror2-bugs.txt
# for issue_id in issues:
#     # Issues in the andror2 bug list
#     if int(issue_id) in list(brg.url_bug_id_dict.values()):
#         app_name = str(issue_id).strip()
#         dir_path = root_location + "/" + "sample-input/" + "bug-"+ app_name + "-bugs.txt"
#         if not os.path.exists(dir_path):
#             with open(dir_path,'w+') as file:
#                 file.write(app_name)


for preprocessed_query_file in dir_list:
    if preprocessed_query_file.endswith(".txt"):
        with open(root_location + '/nadeeshan/t4_3/' + preprocessed_query_file, 'r') as preprocessed_query_file:
            file_name = preprocessed_query_file.name.split("/")[-1]
            Lines = preprocessed_query_file.readlines()  
            count = 0
            # Strips the newline character
            for line in Lines:
                count += 1
                file_name = os.path.splitext(file_name)[0].split("_")[2]
                with open(root_location + '/sample-input/bug-' + file_name + "-query.txt", "a") as query_file:
                    query_file.write(file_name +"	"+line.strip())
