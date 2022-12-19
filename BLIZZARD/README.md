# BLIZZARD: Improving IR-Based Bug Localization with Context-Aware Query Reformulation

This is the Blizzard 1.0.0 source code.

## Build and Export the executable JAR file
1. Use Eclipse IDE to import the project as Java Project. (File > Import > General > Import Projects from File System or Archieve)

2. Change the project's java compiler version to 1.8 (Project Right click > Build Path > Configure Build Path > Libraries > JRE system Library > change this to JDK1.8)
3. Do a Project Refresh.

4. Run > Run Configurations > Java Application. You now should see 3 Java launchers. (IndexLucene-1, QueryGenerator-2, BLIZZARDRunner-3)

5. Change the ```BRICK_EXP``` in StaticData.java to the Working Directory to your BLIZZARD replication package /path/to/BLIZZARD-Replication-Package-ESEC-FSE2018 

6. Lets export the build project as a executable JAR. (File > Export > Runnable JAR File. Select BLIZZARDRunner-3 - BLIZZARD as the Launch configuration and select the 2nd option as the Library Handling. Select the export destination as the  BLIZZARD replication package /path/to/BLIZZARD-Replication-Package-ESEC-FSE2018.  Use the export file name as ```Blizzard_Runner_Final```

7. Download the corpus(projects) from https://drive.google.com/drive/folders/1o8DFBHBKTaGLmTs7rDrwpBjX9J6_ZGLS?usp=sharing

8. Extract the downloaded projects to /path/to/BLIZZARD-Replication-Package-ESEC-FSE2018/Corpus. Now the Corpus directory hierachy should be like below.
--Corpus
    |--bug-2
    |--bug-8
    |--bug-10
    |--bug-53
    |--bug-128
    |--bug-1096
    |--bug-1047


9. Run ```IndexLucene-1``` from Eclipse run configurations to generate Lucene Indexes to projects mentioned on the replication package nadeeshan/projectlist.txt

**NOTE** Please clean the /path/to/BLIZZARD-Replication-Package-ESEC-FSE2018/Lucene-Index and Lucene-Index2File-Mapping directories first to remove exisitng lucene indexes.

10. Now See the https://github.com/NGimhana/CSCI-680-SME_Project/tree/main/BLIZZARD-Replication-Package-ESEC-FSE2018 README to Run BLIZZARD Replication package to generate Results.




