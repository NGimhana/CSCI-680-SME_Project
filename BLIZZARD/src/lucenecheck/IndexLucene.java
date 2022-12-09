package lucenecheck;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import config.StaticData;

public class IndexLucene {

	String repoName;
	String projectName;
	String index;
	String docs;
	public int totalIndexed = 0;

	public IndexLucene(String repoName) {
		// initialization
		this.index = StaticData.BRICK_EXP + "/lucene/index-class/" + repoName;
		this.docs = StaticData.BRICK_EXP + "/corpus/norm-class/" + repoName;
		// this.makeIndexFolder(repoName);
		System.out.println("Index:" + this.index);
		System.out.println("Docs:" + this.docs);
	}

	public IndexLucene(String indexFolder, String docsFolder, String projectName) {
		this.index = indexFolder;
		this.docs = docsFolder;
		this.projectName = projectName;
	}

	protected void makeIndexFolder(String repoName) {
		new File(this.index + "/" + repoName).mkdir();
		this.index = this.index + "/" + repoName;
	}

	public void indexCorpusFiles() {
		// index the files
		try {
			Directory dir = FSDirectory.open(new File(index).toPath());
			Analyzer analyzer = new StandardAnalyzer();
			// Analyzer analyzer=new EnglishAnalyzer(Version.LUCENE_44);
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			IndexWriter writer = new IndexWriter(dir, config);
			indexDocs(writer, new File(this.docs),projectName);
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void clearIndexFiles() {
		// clearing index files
		File[] files = new File(this.index).listFiles();
		for (File f : files) {
			f.delete();
		}
		System.out.println("Index cleared successfully.");
	}

	protected void indexDocs(IndexWriter writer, File file,String projectName) {
		// writing to the index file
		if (file.canRead()) {
			if (file.isDirectory()) {
				String[] files = file.list();
				// an IO error could occur
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						indexDocs(writer, new File(file, files[i]),projectName);
					}
				}
			} else {
				if (isJavaFile(file)) {
					FileInputStream fis;
					FileWriter fileWriter;
					PrintWriter printWriter;
					
					    
					try {
						fis = new FileInputStream(file);
						printWriter = new PrintWriter(new BufferedWriter(new FileWriter(StaticData.BRICK_EXP + "/Lucene-Index2File-Mapping/"+ projectName + ".ckeys", true)));
					
					} catch (IOException fnfe) {
						return;
					}
					try {
						// make a new, empty document
						Document doc = new Document();
						
						Field pathField = new StringField("path", file.getPath(),
								Field.Store.YES);
						doc.add(pathField);
	
						doc.add(new TextField("contents", new BufferedReader(
								new InputStreamReader(fis, "UTF-8"))));
						// System.out.println("adding " + file);
						

						printWriter.write(totalIndexed+1 + ":" + file.getAbsolutePath() + "\n");
						writer.addDocument(doc);
	
						totalIndexed++;
	
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (CorruptIndexException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						try {
							fis.close();
							printWriter.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	protected boolean isJavaFile(File file) {
		// convert the file name into string
	    String fileName = file.toString();
	    int index = fileName.lastIndexOf('.');
	    if(index > 0) {
	        String extension = fileName.substring(index + 1);
	        return extension.equals("java");
	      }
		return false;
		
	}

	public static void main(String[] args) {
		long start=System.currentTimeMillis();
		String repoName="tomcat70";
		
		Scanner s;
		try {
			s = new Scanner(new File(StaticData.BRICK_EXP +"/nadeeshan/projectList.txt"));
		
			ArrayList<String> list = new ArrayList<String>();
			while (s.hasNextLine()){
			    list.add(s.nextLine());
			}
			s.close();
			
			for (String projectName : list) {
				// IndexLucene indexer=new IndexLucene(repoName);
				String docs = StaticData.BRICK_EXP + "/Corpus/" + projectName;
				String index = StaticData.BRICK_EXP + "/Lucene-Index/" + projectName;
				IndexLucene indexer = new IndexLucene(index, docs, projectName);
				indexer.indexCorpusFiles();
				System.out.println("Files indexed:" + indexer.totalIndexed);
				long end=System.currentTimeMillis();
				System.out.println("Time needed:"+(end-start)/1000+" s");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
