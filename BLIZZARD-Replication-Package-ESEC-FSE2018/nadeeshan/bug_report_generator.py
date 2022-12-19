import pandas as pd
import  json
import os
import csv

df = pd.read_csv('Dataset_ AndroR2.csv', usecols=['ID','GitHub Issue'])

## Write file_data objects to bug files
root_location = "../BR-Raw"

json_data = [] # your list with json objects (dicts)
with open('issues.json') as json_file:
   json_data = json.load(json_file)

bugIDData = []
githubData =[]
body =[]


issues = {'issues' : []}
df = df[['ID','GitHub Issue']]

dic = df.to_dict(orient='records')

for meta_data in dic:
      for bug_report in json_data:
         if meta_data['GitHub Issue'] == bug_report['HTML_url']:
            file_data = {'bug_id':{}, 'url': {}, 'body': {}}
            file_data['bug_id'] = meta_data['ID']
            file_data['url'] = meta_data['GitHub Issue']
            file_data['body'] = (bug_report['title'] + " " + bug_report['body'])
            issues['issues'].append(file_data)



# app_name_bug_id_dict = {}

url_bug_id_dict = {}



def main():

   directory_names = []

    # Iterate bug reports
   for issue in issues['issues']:
      file_name = issue['bug_id']
      directory_names.append(file_name)
      url_bug_id_dict[issue['url']] = issue['bug_id']
  
   directory_names = set(directory_names)
   ## write diretory_names to projectList.txt
   # projectList = open('projectList.txt', 'w')
   # for name in directory_names:
   #    projectList.write(name +'\n') 
   # projectList.close()

   # project list
   project_file = open('projectList.txt','r')
   temp_projects  = project_file.read().splitlines()
   projects = []
   
   for project in temp_projects:
      projects.append(project.split("-")[1])

   finished_list = []
   ## create directories in BR-Raw folder
   for directory in directory_names:
      if str(directory) in projects:
         dir_path = root_location + '/bug-' + str(directory)
         if not os.path.exists(dir_path):
            os.mkdir(dir_path)
      
   for issue in issues['issues']:
      bug_id = url_bug_id_dict[issue['url']]
      if str(bug_id) in projects:
         if not bug_id in finished_list:
            file_path = root_location + '/bug-' +str(bug_id) + "/" + str(bug_id) + ".txt"
            bug_file = open(file_path, 'w')
            bug_report_content = "Bug " + str(bug_id) + " - " + issue['body']  
            bug_file.write(bug_report_content)
            bug_file.close()
            finished_list.append(bug_id)


# # Uncomment to run the script
main()

   
