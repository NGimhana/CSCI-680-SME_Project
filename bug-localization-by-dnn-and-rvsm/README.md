# Bug Localization by Using Bug Reports

Skip to the Run section if you just need to Run the solution with the provided data.
## Data Set Creation

1. Download the corpus(projects) from https://drive.google.com/drive/folders/1o8DFBHBKTaGLmTs7rDrwpBjX9J6_ZGLS?usp=sharing

2. Unzip the projects in to data/projects directory. After unzipping the directory hierachy should be like this,
--data
	|--projects
		|--bug-2
		|--bug-8
		|--bug-10
		|--bug-53
		|--bug-128
		|--bug-1096
		|--bug-1147

3. Download the Daset_AndroR2.csv and issues.json from https://drive.google.com/drive/folders/1BKnVBdeN4sok04gXe_n-kWyrwaU0Ck01?usp=sharing. And extract those to data/raw_data directory. Now data folder hierachy should look like this.
--data
	|--raw_data
		|--Dataset_AndroR2.csv
		|--issues.json

4. Download the bug_fix_json from  https://drive.google.com/drive/folders/1w8A09q9UFXBSnmlThr2sin-JtVtr6kUO?usp=sharing. And extract those to data/bug_fix_json directory. Now data folder hierachy should look like this.
--data
	|--bug_fix_json
		|--2.json
		|--8.json
		|--10.json
		|--53.json
		|--128.json
		|--1096.json
		|--1147.json

		

5. Update the data_generation_scripts/project_list.txt with the project(bug ids) ids.

6. execute ```python 1_bug_report_generator.py``` to generate raw bug reports to data/BR-Raw.

7. execute ```python 2_query_generator.py``` to generate train_questions.csv to data folder. This is the query we used to train DNN.

8. execute ```python 3_document_generator.py``` to generate train_questions.csv to data folder. This is the corpus document we used to train DNN.

#### Run

1. Navigate to src directory.

2. Update PROJECT_ABS_PATH in util.py. This should be pointing to data/projects directory.

2. execute ```python feature_extraction.py``` to generate the feature file from train_questions and train_documents. This will include the correct files with 50 random incorrect files and compared rSVM scores.

3. execute ```python main.py``` to train and generate results of DNNLOC.


