CSCI-680 - Software Maintenance & Evolution

This project concentrates on bug localization for Android apps where bugs are displayed on the mobile screen. While there are several previous works done in bug localization based on the same domain, our intention is to propose a baseline technique for bug localization in Android apps to address the research question, “Do we have a bug localization technique that can retrieve buggy code files and methods for GUI-based bugs in Android apps?” To answer this question we created Android-based ground Truth data set and implemented one of the previous TR-based Bug localization approaches called BLIZZARD with our data set. The result of this implementation then has been compared with the DNN-based model that we proposed for localizing buggy elements in Android apps. Both of the implementations have
been evaluated based on MAP, MRR, and HIT@K matrices

#### Contributions

* Buildup the evaluation data - AntroR2,
with additional (37) bugs and corresponding ground truth
data for bug localization

1. Collected Bug-Solution pairs => https://drive.google.com/drive/u/0/folders/1Y8FL42F6YleZDKoDsgkiibygPmodjFdK

2. Collected Buggy Source Codes => https://drive.google.com/drive/u/0/folders/1EPsFpq6P5S1N4UW0UbOeg8e7WHz-hVxp

3. Collected Fixed Source Codes => https://drive.google.com/drive/u/0/folders/18XOgFirP4bHLiCjjM9Mgl57UKnmQhSUt 

* Develop BLIZZARD based on existing source code
 and evaluate this technique on the evaluation data.
The evaluation will measure the technique’s performance
based on well-known metrics (e.g., HIT@K, MAP@K,
MRR@K).

1. Original Replication Package Repository => https://github.com/masud-technope/BLIZZARD-Replication-Package-ESEC-FSE2018 

2. Original Source code of used to build the above Replication Package => https://github.com/masud-technope/BLIZZARD 

* Develop a deep-learning-based bug localization technique
and evaluate this technique on the collected data.
The evaluation will measure the technique’s performance
based on well-known metrics (e.g., HIT@K, MAP@K,
MRR@K).

1. Original Replication package and source code of DNNLoc => https://github.com/emredogan7/bug-localization-by-dnn-and-rvsm 

