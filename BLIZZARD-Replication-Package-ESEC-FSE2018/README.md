
Improving IR-Based Bug Localization with Context-Aware Query Reformulation
=========================================================================================

## Run

Skip steps (1 - 9) if you need to run the Blizzard with exisiting data.

1. Download issues.json from https://drive.google.com/drive/folders/1BKnVBdeN4sok04gXe_n-kWyrwaU0Ck01?usp=sharing. And extract those to ```nadeeshan``` directory. Now ```nadeeshan``` folder hierachy should look like this.

--nadeeshan
    |--blizzard
    |--BugLocation-JSON-Files
    |--bug_report_generator.py
    |--goldset_generator.py
    |--Dataset_AndroR2.csv
    |--issues.json
    |--BugLocation-JSON-Files

2. Download the bug_fix_json from  https://github.com/jmahmud47/FaultLocalizationCode/tree/main/data/JSON-Files-All. And extract those to ```nadeeshan/BugLocation-JSON-Files directory. Now data folder hierachy should look like this.
--nadeeshan
	|--BugLocation-JSON-Files
		|--2.json
		|--8.json
		|--10.json
		|--53.json
		|--128.json
		|--1096.json
		|--1147.json

3. Navigate to ```nadeeshan```directory

4. Execute ```python bug_report_generator.py``` to generate bug reports. Now the Br-Raw should have generated bug reports.

5. Change the root_location (line 7) in goldset_generator.py to the Working Directory to your BLIZZARD replication package /path/to/BLIZZARD-Replication-Package-ESEC-FSE2018 

6. Execute ```python goldset_generator.py``` to generate goldsets(change sets) - (Clear Goldset directory before execute)

7. Navigate to root directory

8. Execute ```./2_query_generator.sh``` to formulate queries (Clear sample-input directory before execute)

9. Execute ```./3_blizzard_runner.sh``` to run Blizzard and generate results (Clear sample-output/bug_wise_results and sample-output/results directories before execute)

10. Execute ```python 4_results_generator.py``` to generate results (works only with K <= 10)

