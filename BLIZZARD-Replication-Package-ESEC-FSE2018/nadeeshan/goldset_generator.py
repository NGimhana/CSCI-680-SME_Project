## Adding fixed file locations to gold set

import os
import  json
import bug_report_generator as brg

root_location = "/Users/bimalkadesilva/Documents/Fall2022/CSCI680-SME/Project/BLIZZARD-Replication-Package-ESEC-FSE2018"


## Issue List
issue_id_list_file = open(root_location + '/sample-input/andror2-bugs.txt')
issues = issue_id_list_file.read().splitlines()

## Navigate in BugLocation JSON files dir - Check JSON validation first!!
dir_list = os.listdir(root_location + '/nadeeshan/BugLocation-JSON-Files')



## Issues in the andror2-bugs.txt
for issue_id in issues:
    # Issues in the andror2 bug list
    if int(issue_id) in list(brg.url_bug_id_dict.values()):
        app_name = str(issue_id).strip()
        dir_path = root_location + "/" + "Goldset/" + "bug-"+ app_name
        if not os.path.exists(dir_path):
            os.mkdir(dir_path)
          
            


for json_file in dir_list:
    if json_file.endswith(".json"):
        json_data = [] # your list with json objects (dicts)
        with open(root_location + '/nadeeshan/BugLocation-JSON-Files/' + json_file) as json_file:
            json_data = json.load(json_file)
        for issue_id in issues:
            if int(issue_id) == int(json_data['id']):
                change_set_file = open(root_location + "/" + "Goldset" + "/bug-" + str(issue_id) + "/" + json_data['id'] + ".txt",'w')
                for change_set in json_data['fix_location']:
                    ## Ignore First item in before the / => Eg; AppName/app .... here we ignore AppName
                    change_set_file.write(root_location + "/Corpus/" + "bug-"+ str(issue_id) + "/" + "/".join(change_set['file_name'].split("/")[1:])+'\n')
                change_set_file.close()


