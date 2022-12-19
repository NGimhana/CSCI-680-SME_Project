from util import csv2dict, tsv2dict, helper_collections, topk_accuarcy, list_to_csv_write_by_lines
from sklearn.neural_network import MLPRegressor
from joblib import Parallel, delayed, cpu_count
from math import ceil
import numpy as np
import os
import pandas as pd
import compute_statistics as cs

import matplotlib.pyplot as plt
import numpy as np


def oversample(samples):
    """ Oversamples the features for label "1" 
    
    Arguments:
        samples {list} -- samples from features.csv
    """
    samples_ = []

    # oversample features of buggy files
    for i, sample in enumerate(samples):
        samples_.append(sample)
        if i % 51 == 0:
            for _ in range(9):
                samples_.append(sample)

    return samples_


def features_and_labels(samples):
    """ Returns features and labels for the given list of samples
    
    Arguments:
        samples {list} -- samples from features.csv
    """
    features = np.zeros((len(samples), 1))
    labels = np.zeros((len(samples), 1))

    for i, sample in enumerate(samples):
        features[i][0] = float(sample["rVSM_similarity"])
        # features[i][1] = float(sample["collab_filter"])
        # features[i][2] = float(sample["classname_similarity"])
        # features[i][3] = float(sample["bug_recency"])
        # features[i][4] = float(sample["bug_frequency"])
        labels[i] = float(sample["match"])

    return features, labels


def kfold_split_indexes(k, len_samples):
    """ Returns list of tuples for split start(inclusive) and 
        finish(exclusive) indexes.
    
    Arguments:
        k {integer} -- the number of folds
        len_samples {interger} -- the length of the sample list
    """
    step = ceil(len_samples / k)
    ret_list = [(start, start + step) for start in range(0, len_samples, step)]

    return ret_list


def kfold_split(bug_reports, samples, start, finish):
    """ Returns train samples and bug reports for test
    
    Arguments:
        bug_reports {list of dictionaries} -- list of all bug reports
        samples {list} -- samples from features.csv
        start {integer} -- start index for test fold
        finish {integer} -- start index for test fold
    """
    train_samples = samples[:start] + samples[finish:]
    test_samples = samples[start:finish]

    test_br_ids = set([s["report_id"] for s in test_samples])
    test_bug_reports = [br for br in bug_reports if br["id"] in test_br_ids]

    return train_samples, test_bug_reports


def train_dnn(
    i, num_folds, samples, start, finish, sample_dict, bug_reports, br2files_dict
):
    """ Trains the dnn model and calculates top-k accuarcies
    
    Arguments:
        i {interger} -- current fold number for printing information
        num_folds {integer} -- total fold number for printing information
        samples {list} -- samples from features.csv
        start {integer} -- start index for test fold
        finish {integer} -- start index for test fold
        sample_dict {dictionary of dictionaries} -- a helper collection for fast accuracy calculation
        bug_reports {list of dictionaries} -- list of all bug reports
        br2files_dict {dictionary} -- dictionary for "bug report id - list of all related files in features.csv" pairs
    """
    print("Fold: {} / {}".format(i + 1, num_folds), end="\r")

    train_samples, test_bug_reports = kfold_split(bug_reports, samples, start, finish)
    train_samples = oversample(train_samples)
    np.random.shuffle(train_samples)
    X_train, y_train = features_and_labels(train_samples)

    clf = MLPRegressor(
        solver="sgd",
        alpha=1e-5,
        hidden_layer_sizes=(300,),
        random_state=1,
        max_iter=10000,
        n_iter_no_change=30,
    )
    clf.fit(X_train, y_train.ravel())

    acc_dict,results_dict  = topk_accuarcy(test_bug_reports, sample_dict, br2files_dict, clf=clf)
    return [acc_dict,results_dict,test_bug_reports]
    # return acc_dict


def dnn_model_kfold(k=10):
    """ Run kfold cross validation in parallel
    
    Keyword Arguments:
        k {integer} -- the number of folds (default: {10})
    """
    samples = csv2dict("../data/features.csv")

    # These collections are speed up the process while calculating top-k accuracy
    sample_dict, bug_reports, br2files_dict = helper_collections(samples)

    np.random.shuffle(samples)

    # K-fold Cross Validation in parallel
    results_dicts = Parallel(n_jobs=-2)(  # Uses all cores but one
        delayed(train_dnn)(
            i, k, samples, start, step, sample_dict, bug_reports, br2files_dict
        )
        for i, (start, step) in enumerate(kfold_split_indexes(k, len(samples)))
    )
    
    # GET THE LAST RESULT ONLY => acc_dicts[9]
    acc_dicts = []
    for i in range(len(results_dicts)):
        acc_dicts.append(results_dicts[i][0])
    
    # Calculating the average HIT @ K from all folds
    avg_acc_dict = {}
    for key in acc_dicts[0].keys():
        avg_acc_dict[key] = round(sum([d[key] for d in acc_dicts]) / len(acc_dicts), 3)

    projects = []
    for i in range(len(results_dicts[9][2])):
        projects.append(results_dicts[9][2][i]["id"])
    
    
    # Hit @ K
    hitK_results=[]
    for project in projects:
        results = {'project': project}
        for k in results_dicts[9][1][project]:
            hitK = 0
            for topKFile in k['topK_predicted_files']:
                if topKFile in br2files_dict[project]:
                    hitK= hitK + 1
            results['hit@' + str(k['k'])] = hitK/1 # since our project has one bug report
        print(results)    
        hitK_results.append(results)

    ## This need to be run each time k is changed ##
    # MRR @ K
    DOCUMENTS_DATA_PATH="/Users/bimalkadesilva/Desktop/CSCI-680-SME_Project/mine/bug-localization-by-dnn-and-rvsm/results/results.csv"
    ACTUAL_DOCUMENTS_DATA_PATH='/Users/bimalkadesilva/Desktop/CSCI-680-SME_Project/mine/bug-localization-by-dnn-and-rvsm/results/actual_results.csv'
    PREDICTED_DOCUMENTS_DATA_PATH='/Users/bimalkadesilva/Desktop/CSCI-680-SME_Project/mine/bug-localization-by-dnn-and-rvsm/results/predicted_results.csv'
    
    df_documents = pd.read_csv(DOCUMENTS_DATA_PATH) 
    results = []
    for i in range(len(df_documents)):
        predeicted_files_list=df_documents.iloc[i,1][1:-1].split(",")
        relevancy_score_list=df_documents.iloc[i,3][1:-1].split(",")
        rank = 1
        for j in range(len(predeicted_files_list)):
            results_row={}
            results_row['bug_id'] = df_documents.iloc[i,0]
            results_row['url'] =  predeicted_files_list[j][1:-2]
            results_row['relevancy_score'] = float(relevancy_score_list[j][1:-1].replace('\'',''))
            results_row['rank'] = rank
            rank = rank + 1
            results.append(results_row)

    # csv header
    fieldnames = ["bug_id", "url", 'relevancy_score','rank']

    # Write Results to CSV
    list_to_csv_write_by_lines('../results/predicted_results.csv', fieldnames, results)
   
    ## Actual Ranked list
    results = []
    results_row={}
    for i in range(len(df_documents)):
        predeicted_files_list=df_documents.iloc[i,1][1:-1].split(",")
        relevancy_score_list=df_documents.iloc[i,3][1:-1].split(",")
        for j in range(len(predeicted_files_list)):
            results_row={}
            results_row['bug_id'] = df_documents.iloc[i,0]
            results_row['url'] =  predeicted_files_list[j][1:-2]

            if predeicted_files_list[j][1:-2] in br2files_dict[str(df_documents.iloc[i,0])]:
                results_row['relevancy_score'] = 1.0
            else :
                results_row['relevancy_score'] = 0.0  
            results.append(results_row)

    # Write Results to CSV
    list_to_csv_write_by_lines('../results/actual_results.csv', fieldnames, results)

    df_actual_documents = pd.read_csv(ACTUAL_DOCUMENTS_DATA_PATH)
    df_pred_documents = pd.read_csv(PREDICTED_DOCUMENTS_DATA_PATH)
    
    ## Actual Ranked list
    df1 = df_actual_documents[['bug_id', 'url', 'relevancy_score']]

    ## Predicted ranked list
    labeled_search_results = df_pred_documents.merge(df1, how='left', on=['bug_id', 'url']).fillna(0)

    ## Inspect the best rank of each relevancy grade
    relevances_rank = labeled_search_results.groupby(['bug_id', 'relevancy_score_x'])['rank'].min()
    
    ## Consider only on rank 1
    ranks = relevances_rank.loc[:, 1]
    
    print("MRR@"+str(len(results_dicts[9][1][project])))
    # Reciprocal rank of each query - bug report
    reciprocal_ranks = 1 / (ranks)
    print(reciprocal_ranks)

    # For MAP need to add [[]], [[]]
    actual_list=[]
    predicted_list=[]

    for project in projects:
        relevancy_score = []
        predicted_relevancy_score=[]
        for row in range(len(df1)):
            if str(df1.iloc[row,0]) == str(project):
                relevancy_score.append(df1.iloc[row,2])    
        actual_list.append(relevancy_score)

        for row in range(len(labeled_search_results)):
            if str(labeled_search_results.iloc[row,0]) == str(project):
                predicted_relevancy_score.append(labeled_search_results.iloc[row,2])
        predicted_list.append(predicted_relevancy_score)

    ## Compute MAP
    print("MAP@1", cs.mapk(actual_list,predicted_list,k=10))  

    hitK_mean_results_list=[]
    ## Compute Mean of Hit@K
    k=2
    for j in range(0,9): # Hit K Till 2-10
        mean = 0
        sum_of_hits = 0
        for i in hitK_results:    
            sum_of_hits = sum_of_hits + i['hit@' + str(k)]
        mean = sum_of_hits/len(hitK_results)
        hitK_mean_results_list.append(mean)
        k = k + 1

    # MRR@2 - [0.5+1.0]/7 = 0.21428571428
# 8       0.5
# 1096    1.0 
    # MRR@3 - 0.40476185714
# 8       0.500000
# 53      0.333333
# 1096    1.000000
# 1147    1.000000
    # MRR@4 - 0.40476185714
# 8       0.500000
# 53      0.333333
# 1096    1.000000
# 1147    1.000000
    # MRR@5 - 0.47619042857
# 8       0.333333
# 53      1.000000
# 1096    1.000000
# 1147    1.000000    
    # MRR@6 - 0.38095228571
# 8       0.333333
# 53      0.333333
# 1096    1.000000
# 1147    1.000000

    # MRR@7 - 0.47619042857
# 8       0.333333
# 53      1.000000
# 1096    1.000000
# 1147    1.000000

    # MRR@8 - 0.47619042857
# 8       0.333333
# 53      1.000000
# 1096    1.000000
# 1147    1.000000
    # MRR@9 - 0.38095228571
# 8       0.333333
# 53      0.333333
# 1096    1.000000
# 1147    1.000000

    # MRR@10 - 0.47619042857
# 8       0.333333
# 53      1.000000
# 1096    1.000000
# 1147    1.000000

    # MAP@2 - 0.6428571428571429
    # MAP@3 - 0.42857142857142855
    # MAP@4 - 0.32142857142857145
    # MAP@5 - 0.2857142857142857
    # MAP@6 - 0.21428571428571427
    # MAP@7 - 0.2040816326530612
    # MAP@8 - 0.17857142857142858
    # MAP@9 - 0.14285714285714285
    # MAP@10 -  0.14285714285714285

    ######## Average Hit K Results
    xData=[2,3,4,5,6,7,8,9,10]
    yData_MAP = hitK_mean_results_list

    xData = np.array(xData)
    yData_Hit = np.array(yData_MAP)

    ## plot the data
    fig = plt.figure()
    ax = fig.add_subplot(1, 1, 1)
    ax.plot(xData, yData_Hit, 'bo-', color='tab:blue' )

    ## zip joins x and y coordinates in pairs
    for x,y in zip(xData,yData_Hit):
        label = "{:.2f}".format(y)
        plt.annotate(label,
                    (x,y), 
                    textcoords="offset points",
                    xytext=(0,10), 
                    ha='center') 

    ax.set_ylabel('HIT@K')
    ax.set_xlabel('K')
    ax.set_title("HIT@K")
    # display the plot
    plt.savefig('../results/HIT.png') 


    ######## Average MRR @ K Results
    xData=[2,3,4,5,6,7,8,9,10]
    mrr_mean_results_list =[0.21428571428, 0.40476185714, 0.40476185714, 0.47619042857,0.38095228571, 0.47619042857
    , 0.47619042857, 0.38095228571, 0.47619042857 ]
    yData_MAP = mrr_mean_results_list

    xData = np.array(xData)
    yData_Hit = np.array(yData_MAP)

    ## plot the data
    fig = plt.figure()
    ax = fig.add_subplot(1, 1, 1)
    ax.plot(xData, yData_Hit, 'bo-', color='tab:blue' )

    ## zip joins x and y coordinates in pairs
    for x,y in zip(xData,yData_Hit):
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
    plt.savefig('../results/MRR.png')


    ######## Average MAP @ K Results
    xData=[2,3,4,5,6,7,8,9,10]
    map_mean_results_list =[0.6428571428571429, 0.42857142857142855, 0.32142857142857145, 0.2857142857142857,
    0.21428571428571427, 0.2040816326530612, 0.17857142857142858, 0.14285714285714285, 0.14285714285714285]
    yData_MAP = map_mean_results_list

    xData = np.array(xData)
    yData_Hit = np.array(yData_MAP)

    ## plot the data
    fig = plt.figure()
    ax = fig.add_subplot(1, 1, 1)
    ax.plot(xData, yData_Hit, 'bo-', color='tab:blue' )

    ## zip joins x and y coordinates in pairs
    for x,y in zip(xData,yData_Hit):
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
    plt.savefig('../results/MAP.png')   

