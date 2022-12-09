import matplotlib.pyplot as plt
import csv

WORKING_DIR='/Users/bimalkadesilva/Documents/Fall2022/CSCI680-SME/Project/BLIZZARD-Replication-Package-ESEC-FSE2018/sample-output/results'

field_names = [[],['project_name', 'Hit@1' , 'MRR@1' ,'MAP@1'],
['project_name', 'Hit@2' , 'MRR@2' ,'MAP@2'],
['project_name', 'Hit@3' , 'MRR@3' ,'MAP@3'],
['project_name', 'Hit@4' , 'MRR@4' ,'MAP@4'],
['project_name', 'Hit@5' , 'MRR@5' ,'MAP@5'],
['project_name', 'Hit@6' , 'MRR@6' ,'MAP@6'],
['project_name', 'Hit@7' , 'MRR@7' ,'MAP@7'],
['project_name', 'Hit@8' , 'MRR@8' ,'MAP@8'],
['project_name', 'Hit@9' , 'MRR@9' ,'MAP@9'],
['project_name', 'Hit@10' , 'MRR@10' ,'MAP@10']]

def list_to_csv_write_by_lines(file_name, field_names, results):
    with open(file_name, 'w', newline='') as f:
        writer = csv.DictWriter(f, fieldnames=field_names)
        writer.writeheader()
        writer.writerows(results)

## Results @ 1
results_array = ["", open(WORKING_DIR + '/results_1.txt', "r"),
 open(WORKING_DIR + '/results_2.txt', "r"),
open(WORKING_DIR + '/results_3.txt', "r"),
open(WORKING_DIR + '/results_4.txt', "r"),
open(WORKING_DIR + '/results_5.txt', "r"),
open(WORKING_DIR + '/results_6.txt', "r"),
open(WORKING_DIR + '/results_7.txt', "r"),
open(WORKING_DIR + '/results_8.txt', "r"),
open(WORKING_DIR + '/results_9.txt', "r"),
open(WORKING_DIR + '/results_10.txt', "r")]

yData_Hit = []
yData_MRR = []
yData_MAP = []
xData = []

for j in range(1,11,1):
    
    lines = results_array[j].read().splitlines()

    for line in range(len(lines)):
        for n in range(len(lines)):
            if lines[n] == "Collection of results started. Please wait.." or lines[n] == "Localization results collected successfully :-)" or lines[n] == "Bug Localization Performance:" or lines[n] == "Time elapsed:1 seconds":
                lines[n] = ""
    lines = [i for i in lines if i!='']

    results = []
    project_count = 0
    hit_tot = 0.0
    mrr_tot = 0.0
    map_tot = 0.0
    for i in range(0,len(lines)-3,4):
        results_row = {}
        results_row['project_name'] = lines[i].split(":")[1] 

        results_row[field_names[j][1]] = lines[i+1].split(":")[1]
        hit_tot = hit_tot + float(lines[i+1].split(":")[1])

        results_row[field_names[j][2]] = lines[i+2].split(":")[1]
        mrr_tot = mrr_tot + float(lines[i+2].split(":")[1])

        results_row[field_names[j][3]] = lines[i+3].split(":")[1]
        map_tot = map_tot + float(lines[i+3].split(":")[1])

        project_count = project_count + 1
        results.append(results_row)

    results_row = {}
    results_row['project_name'] = "Mean" 
    results_row[field_names[j][1]] = hit_tot / project_count 
    results_row[field_names[j][2]] = mrr_tot / project_count
    results_row[field_names[j][3]] = map_tot / project_count
    results.append(results_row)

    yData_Hit.append(hit_tot / project_count)
    yData_MRR.append(mrr_tot / project_count)
    yData_MAP.append(map_tot / project_count)

    xData.append(j)

    # list_to_csv_write_by_lines(WORKING_DIR + '/results_' + str(j)+ '.csv', field_names[j] , results)  


## Graphs
######################## - HIT@K
import matplotlib.pyplot as plt
import numpy as np

xData = np.array(xData)
yData_Hit = np.array(yData_Hit)

# plot the data
fig = plt.figure()
ax = fig.add_subplot(1, 1, 1)
ax.plot(xData, yData_Hit, 'bo-', color='tab:blue' )

# zip joins x and y coordinates in pairs
for x,y in zip(xData,yData_Hit):

    label = "{:.2f}".format(y)

    plt.annotate(label,
                 (x,y), 
                 textcoords="offset points",
                 xytext=(0,10), 
                 ha='center') 

ax.set_ylabel('Mean HIT@K')
ax.set_xlabel('K')
ax.set_title("Mean HIT@K")
# display the plot
plt.show()


#################### MRR@K
yData_MRR = np.array(yData_MRR)

# plot the data
fig = plt.figure()
ax = fig.add_subplot(1, 1, 1)
ax.plot(xData, yData_MRR, 'bo-', color='tab:blue' )

# zip joins x and y coordinates in pairs
for x,y in zip(xData,yData_MRR):

    label = "{:.2f}".format(y)

    plt.annotate(label,
                 (x,y), 
                 textcoords="offset points",
                 xytext=(0,10), 
                 ha='center') 

ax.set_ylabel('MRR@K')
ax.set_xlabel('K')
ax.set_title("MRR@K")
# display the plot
plt.show()

################### MAP@K
yData_MAP = np.array(yData_MAP)

# plot the data
fig = plt.figure()
ax = fig.add_subplot(1, 1, 1)
ax.plot(xData, yData_MAP, 'bo-', color='tab:blue' )

# zip joins x and y coordinates in pairs
for x,y in zip(xData,yData_MAP):

    label = "{:.2f}".format(y)

    plt.annotate(label,
                 (x,y), 
                 textcoords="offset points",
                 xytext=(0,10), 
                 ha='center') 

ax.set_ylabel('MAP@K')
ax.set_xlabel('K')
ax.set_title("MAP@K")
# display the plot
plt.show()

