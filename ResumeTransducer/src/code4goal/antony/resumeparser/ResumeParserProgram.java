package code4goal.antony.resumeparser;

import static gate.Utils.stringFor;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.util.GateException;
import gate.util.Out;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

<<<<<<< HEAD
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
=======
import org.apache.pdfbox.util.operator.Concatenate;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
>>>>>>> 5aeca1b4bad77ab8a67c372454e65631f24207ef
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.ToXMLContentHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class ResumeParserProgram {
    private static File parseToHTMLUsingApacheTikka(String file) throws IOException, SAXException, TikaException {
	String ext = FilenameUtils.getExtension(file);
	String outputFileFormat = "";
	if (ext.equalsIgnoreCase("html") | ext.equalsIgnoreCase("pdf") | ext.equalsIgnoreCase("doc")
		| ext.equalsIgnoreCase("docx")) {
	    outputFileFormat = ".html";
	} else if (ext.equalsIgnoreCase("txt") | ext.equalsIgnoreCase("rtf")) {
	    outputFileFormat = ".txt";
	} else {
	    System.out.println("Input format of the file " + file + " is not supported.");
	    return null;
	}
	String OUTPUT_FILE_NAME = FilenameUtils.removeExtension(file) + outputFileFormat;
	ContentHandler handler = new ToXMLContentHandler();
	InputStream stream = new FileInputStream(file);
	AutoDetectParser parser = new AutoDetectParser();
	Metadata metadata = new Metadata();
	try {
	    parser.parse(stream, handler, metadata);
	    FileWriter htmlFileWriter = new FileWriter(OUTPUT_FILE_NAME);
	    htmlFileWriter.write(handler.toString());
	    htmlFileWriter.flush();
	    htmlFileWriter.close();
	    return new File(OUTPUT_FILE_NAME);
	} finally {
	    stream.close();
	}
    }

    public static JSONObject loadGateAndAnnie(File file) throws GateException, IOException {
	Out.prln("Initialising basic system...");
	Gate.init();
	Out.prln("...basic system initialised");
	Annie annie = new Annie();
	annie.initAnnie();
	Corpus corpus = Factory.newCorpus("Annie corpus");
	String current = new File(".").getAbsolutePath();
	URL u = file.toURI().toURL();
	FeatureMap params = Factory.newFeatureMap();
	params.put("sourceUrl", u);
	params.put("preserveOriginalContent", new Boolean(true));
	params.put("collectRepositioningInfo", new Boolean(true));
	Out.prln("Creating doc for " + u);
	Document resume = (Document) Factory.createResource("gate.corpora.DocumentImpl", params);
	Out.prln("params:::" + params);
	corpus.add(resume);
	System.out.println("Corpus::" + corpus);

	/*
	 * String Name = ""; String name = resume.toString(); for (int i = 25; i
	 * < 47; i++) { Name = Name + name.charAt(i); }
	 */

	JSONObject profileJSON = new JSONObject();


	annie.setCorpus(corpus);
	annie.execute();
	Iterator iter = corpus.iterator();
	JSONObject parsedJSON = new JSONObject();
	Out.prln("Started parsing...");
	if (iter.hasNext()) {
	    Document doc = (Document) iter.next();
	    AnnotationSet defaultAnnotSet = doc.getAnnotations();
	    AnnotationSet curAnnSet;
	    Iterator it;
	    Annotation currAnnot;
	    curAnnSet = defaultAnnotSet.get("NameFinder");
	    if (curAnnSet.iterator().hasNext()) {
		currAnnot = (Annotation) curAnnSet.iterator().next();
		Out.prln("currAnnot::"+currAnnot);
		JSONObject nameJSON = new JSONObject();
		String[] nameFeatures = new String[] { "firstName", "middleName", "surname" };
		for (String feature : nameFeatures) {
		    String s = (String) currAnnot.getFeatures().get(feature);
		    if (s != null && s.length() > 0) {
			nameJSON.put(feature, s);
		    }

<<<<<<< HEAD
		}
		Out.prln("Name::"+nameJSON);
	    }
	    /*curAnnSet = defaultAnnotSet.get("TitleFinder");
	    Out.prln("Title Finder::::" + curAnnSet);
	    if (curAnnSet.iterator().hasNext()) {
		currAnnot = (Annotation) curAnnSet.iterator().next();
		Out.prln("Title Finder::::" + currAnnot);
		String title = stringFor(doc, currAnnot);
		Out.prln("Title ::::" + title);
		if (title != null && title.length() > 0) {
		    profileJSON.put("title", title);
		}
	    }*/
	 /*   String[] annSections = new String[] { "EmailFinder", "AddressFinder", "PhoneFinder", "URLFinder" };
	    String[] annKeys = new String[] { "email", "address", "phone", "url" };
	    for (short i = 0; i < annSections.length; i++) {
		String annSection = annSections[i];
		curAnnSet = defaultAnnotSet.get(annSection);
		it = curAnnSet.iterator();
		JSONArray sectionArray = new JSONArray();
		while (it.hasNext()) {
		    currAnnot = (Annotation) it.next();
		    String s = stringFor(doc, currAnnot);
		    if (s != null && s.length() > 0) {
			sectionArray.add(s);
		    }
		}
		if (sectionArray.size() > 0) {
		    profileJSON.put(annKeys[i], sectionArray);
		}
	    }*/
/*	    if (!profileJSON.isEmpty()) {
		parsedJSON.put("basics", profileJSON);
	    }
	    // awards,credibility,education_and_training,extracurricular,misc,skills,summary
	    String[] otherSections = new String[] { "summary", "education_and_training", "skills", "accomplishments",
		    "awards", "credibility", "extracurricular", "misc" };
	    for (String otherSection : otherSections) {
		curAnnSet = defaultAnnotSet.get(otherSection);
		it = curAnnSet.iterator();
		JSONArray subSections = new JSONArray();
		while (it.hasNext()) {
		    JSONObject subSection = new JSONObject();
		    currAnnot = (Annotation) it.next();
		    String key = (String) currAnnot.getFeatures().get("sectionHeading");
		    String value = stringFor(doc, currAnnot);
		    if (!StringUtils.isBlank(key) && !StringUtils.isBlank(value)) {
			subSection.put(key, value);
		    }
		    if (!subSection.isEmpty()) {
			subSections.add(subSection);
		    }
		}
		if (!subSections.isEmpty()) {
		    parsedJSON.put(otherSection, subSections);
		}
	    }
	    curAnnSet = defaultAnnotSet.get("work_experience");
	    it = curAnnSet.iterator();
	    JSONArray workExperiences = new JSONArray();
	    while (it.hasNext()) {
		JSONObject workExperience = new JSONObject();
		currAnnot = (Annotation) it.next();
		String key = (String) currAnnot.getFeatures().get("sectionHeading");
		if (key.equals("work_experience_marker")) {
		    String[] annotations = new String[] { "date_start", "date_end", "jobtitle", "organization" };
		    for (String annotation : annotations) {
			String v = (String) currAnnot.getFeatures().get(annotation);
			if (!StringUtils.isBlank(v)) {
			    // details.put(annotation, v);
			    workExperience.put(annotation, v);
			}
		    }
		    key = "text";
		}
		String value = stringFor(doc, currAnnot);
		if (!StringUtils.isBlank(key) && !StringUtils.isBlank(value)) {
		    workExperience.put(key, value);
		}
		if (!workExperience.isEmpty()) {
		    workExperiences.add(workExperience);
		}

	    }
	    if (!workExperiences.isEmpty()) {
		parsedJSON.put("work_experience", workExperiences);
	    }*/
=======
			// Name
			curAnnSet = defaultAnnotSet.get("NameFinder");
			if (curAnnSet.iterator().hasNext()) 
			{ // only one name will be found.
				currAnnot = (Annotation) curAnnSet.iterator().next();
				// Needed name Features
				/*String[] nameFeatures = new String[] { "firstName","middleName", "surname" };
				StringBuilder sb = new StringBuilder();
				for (String feature : nameFeatures) 
				{
					String s = (String) currAnnot.getFeatures().get(feature);
					if (s != null && s.length() > 0) 
					{
						sb.append(s).append(" ");
					}
				}
				profileJSON.put("name",sb);
			} // name
*/
				// Needed name Features
				JSONObject nameJson = new JSONObject();
				String[] nameFeatures = new String[] { "firstName",
						"middleName", "surname" };
				for (String feature : nameFeatures) {
					String s = (String) currAnnot.getFeatures().get(feature);
					if (s != null && s.length() > 0) {
						nameJson.put(feature, s);
					}
				}
				profileJSON.put("name", nameJson);
			} // name
			// title
			curAnnSet = defaultAnnotSet.get("TitleFinder");
			if (curAnnSet.iterator().hasNext()) { // only one title will be
													// found.
				currAnnot = (Annotation) curAnnSet.iterator().next();
				String title = stringFor(doc, currAnnot);
				if (title != null && title.length() > 0) {
					profileJSON.put("title", title);
				}
			}// title
>>>>>>> 5aeca1b4bad77ab8a67c372454e65631f24207ef

	}
	Out.prln("Completed parsing...");
	//return parsedJSON;
	return profileJSON;
    }

    public static void main(String[] args) throws IOException {
	/*
	 * String path =
	 * "C:\\Training\\ResumeParser\\ResumeTransducer\\Resumes\\"; File
	 * folder = new File(path); File[] listOfFiles = folder.listFiles();
	 *
	 * for (int i = 0; i < listOfFiles.length; i++) {
	 */
	/*
	 * if (listOfFiles[i].isFile()) { System.out.println("File " +
	 * listOfFiles[i].getName()); } else if (listOfFiles[i].isDirectory()) {
	 * System.out.println("Directory " + listOfFiles[i].getName()); }
	 */
	/*
	 * String inputFileName = null ; String outputFileName =null; if
	 * (listOfFiles[i].isFile()) { inputFileName = path+
	 * listOfFiles[i].getName(); outputFileName
	 * =path+FilenameUtils.removeExtension(listOfFiles[i].getName())+
	 * ".json"; }
	 */
	String inputFileName = "E:\\Yuti.docx";
	String outputFileName = "E:\\output.json";

	try {
	    File tikkaConvertedFile = parseToHTMLUsingApacheTikka(inputFileName);

	    if (tikkaConvertedFile != null) {
		JSONObject parsedJSON = loadGateAndAnnie(tikkaConvertedFile);

		// Out.prln("ParsedJSON::"+ parsedJSON);

		// JSONObject resultJSON = basicJSON(parsedJSON);

		Utils util = new Utils();
		//JSONObject getJSON = util.getResultJSON(parsedJSON);

<<<<<<< HEAD
		Out.prln("Writing to output...");
		FileWriter jsonFileWriter = new FileWriter(outputFileName);
		jsonFileWriter.write(parsedJSON.toJSONString());
		jsonFileWriter.flush();
		jsonFileWriter.close();
		Out.prln("Output written to file " + outputFileName);
	    }
	} catch (Exception e) {
	    System.out.println("Sad Face :( .Something went wrong.");
	    e.printStackTrace();
	}
    }
=======
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Sad Face :( .Something went wrong.");
			e.printStackTrace();
		}
	}
	public static JSONObject resultJSON(JSONObject obj)
	{
		  JSONObject resultJSON = new JSONObject();
		  resultJSON.put("basics",basicJSON(obj));
		  //Out.prln(resultJSON);
		  resultJSON.put("work",workJSON(obj));
		  JSONArray array = interestsJSON(obj);
		  if(!array.isEmpty()){
		  resultJSON.put("interests",array);
		  }
		  resultJSON.put("education",educationJSON(obj));
		  resultJSON.put("skills",skillsJSON(obj));
		  return resultJSON;
	}
	
	
public static JSONObject basicJSON(JSONObject obj)
	{
	  JSONObject basicJSON = new JSONObject();
		  if(obj.containsKey("basics"))
		  {
		  			 //Out.prln(obj.get(key));
			  JSONObject subObj =(JSONObject)obj.get("basics");
			  String[] basics = {"name","title","email","phone","url"};
			  String[] summary = {"summary"};
			  for(String key1:basics)
			  	{
				  if(subObj.containsKey(key1))
				  {
					  if(key1=="title")
						  basicJSON.put("label", subObj.get(key1));
					  else if(key1=="url")
						  basicJSON.put("website", subObj.get(key1));
					  else if(key1=="name")
					  {
						String s="";
						JSONObject nameObj = (JSONObject) subObj.get("name");
						String names[] = {"firstName","middleName","surname"};
						for(String key:names)  
						{
						if(nameObj.containsKey(key))
						  {
							  String name = (String) nameObj.get(key);
							  s=s+name+" ";
								//Out.prln(s);
						  }
						}
						  basicJSON.put(key1,s);
					  }

					  else
						  basicJSON.put(key1, subObj.get(key1));
				  }
			  	}
		  	}
		  //JSONArray summaryJSON = new JSONArray();
		  if(obj.containsKey("summary"))
		  {
			  JSONArray arr = (JSONArray)obj.get("summary");
			  String[] summary = {"Summary","SUMMARY","PROFESSIONAL SUMMARY","CAREER OBJECTIVE","PROFESSIONAL EXPERIENCE","Profile","PROFILE","Career Summary"};
			  for(int i=0;i<arr.size();i++)
			  {
				  JSONObject json = (JSONObject)arr.get(i);
				  JSONObject sum = new JSONObject();
				  for(String key:summary)
				  {
					  if(json.containsKey(key))
					  {
						  basicJSON.put("summary",json.get(key));
					  }
				  }
				  //basicJSON.put("summary",sum);  
			  }
			  //basicJSON.put("summary",summaryJSON);
		  }	  //basicsJSON.put("basics", basicJSON);
	  return basicJSON;
	}
public static JSONArray workJSON(JSONObject obj)
	{
		  JSONArray workJSON = new JSONArray();
			  if(obj.containsKey("work_experience"))
			  {
				  JSONArray arr = (JSONArray)obj.get("work_experience");
				  String[] works = {"organization","jobtitle","url","date_start","date_end","text","Projects"};
				  for(int i=0;i<arr.size();i++)
				  {
					  JSONObject json = (JSONObject)arr.get(i);
					  JSONObject work = new JSONObject();
					  for(String key:works)
					  {
						  if((json.containsKey(key)))
						  {
							  if(key=="organization")
								  work.put("company",json.get(key));
							  else if(key=="jobtitle")
							  work.put("position",json.get(key));
							  else if(key=="url")
								  work.put("website",json.get(key));
							  else if(key=="date_start")
								  work.put("startDate",json.get(key));
							  else if(key=="date_end")
								  work.put("endDate",json.get(key));
							  else if(key=="text")
								  work.put("summary",json.get(key));
							  else
								  work.put("summary",json.get(key));
						  }
					  }
					  if(!work.isEmpty()){
					  workJSON.add(work);
					  }
				  }
			  }
		/*	  JSONArray resultJSON = new JSONArray();
		for(int i=0;i<workJSON.size();i++)	  
		{
		if (!workJSON.get(i).equals(null))
			resultJSON.add(workJSON.get(i));
		}*/
		return workJSON;
	}
public static JSONArray interestsJSON(JSONObject obj)
{
	JSONObject interestsJSON = new JSONObject();
	JSONArray  arr = new JSONArray();
		  if(obj.containsKey("misc"))
		  {
			  
			  arr = (JSONArray)obj.get("misc");
		  }
	  return arr;
}
public static JSONArray educationJSON(JSONObject obj)
{
	JSONArray  arr = new JSONArray();
	if(obj.containsKey("basics"))
	{
		  if(obj.containsKey("education_and_training"))
		  {
			  arr = (JSONArray)obj.get("education_and_training");
		  }
	}
	return arr;
}
public static JSONArray skillsJSON(JSONObject obj)
{
	JSONArray  arr = new JSONArray();
	if(obj.containsKey("basics"))
	{
		  if(obj.containsKey("skills"))
		  {
			  arr = (JSONArray)obj.get("skills");
		  }
	}
	return arr;
}

>>>>>>> 5aeca1b4bad77ab8a67c372454e65631f24207ef
}