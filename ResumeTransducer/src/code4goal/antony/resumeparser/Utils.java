package code4goal.antony.resumeparser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Utils {

    JSONObject resultJSON = new JSONObject();
    JSONObject basicJSON = new JSONObject();
    JSONArray InterestsArr = new JSONArray();
    JSONArray skillsArr = new JSONArray();


    public JSONObject getResultJSON(JSONObject obj) {
	resultJSON.put("basics", getBasics(obj));
	if (getSkills(obj) != null) {
	    resultJSON.put("skills", getSkills(obj));
	}
	if (getWork(obj) != null) {
	    resultJSON.put("work", getWork(obj));
	}
	if (getInterests(obj) != null) {
	    resultJSON.put("interests", getInterests(obj));
	}
	if (getEducation(obj) != null) {
	    resultJSON.put("education", getEducation(obj));
	}
	return resultJSON;
    }

    public JSONObject getBasics(JSONObject obj) {
	if (obj.containsKey("basics")) {
	    JSONObject subObj = (JSONObject) obj.get("basics");
	    String[] basics = { "name", "title", "email", "phone", "url" };
	    for (String key1 : basics) {
		if (subObj.containsKey(key1)) {
		    if (key1 == "title") {
			basicJSON.put("label", subObj.get(key1));
		    } else if (key1 == "url") {
			basicJSON.put("website", subObj.get(key1));
		    } else if (key1 == "name") {
			String s = "";
			JSONObject nameObj = (JSONObject) subObj.get("name");
			String names[] = { "firstName", "middleName", "surname" };
			for (String key : names) {
			    if (nameObj.containsKey(key)) {
				String name = (String) nameObj.get(key);
				s = s + name + " ";
			    }
			}
			basicJSON.put(key1, s);
		    } else {
			basicJSON.put(key1, subObj.get(key1));
		    }
		}
	    }
	}
	if (obj.containsKey("summary")) {
	    JSONArray arr = (JSONArray) obj.get("summary");
	    String[] summary = { "Summary", "summary" };
	    for (int i = 0; i < arr.size(); i++) {
		JSONObject json = (JSONObject) arr.get(i);
		for (String key : summary) {
		    if (json.containsKey(key)) {
			basicJSON.put("summary", json.get(key));
		    }
		}
	    }
	}
	return basicJSON;
    }

    public JSONArray getWork(JSONObject obj) {
	JSONArray workJSON = new JSONArray();
	if (obj.containsKey("work_experience")) {
	    JSONArray arr = (JSONArray) obj.get("work_experience");
	    String[] works = { "organization", "jobtitle", "url", "date_start", "date_end", "text", "Projects" };
	    for (int i = 0; i < arr.size(); i++) {
		JSONObject json = (JSONObject) arr.get(i);
		JSONObject work = new JSONObject();
		for (String key : works) {
		    if ((json.containsKey(key))) {
			if (key == "organization") {
			    work.put("company", json.get(key));
			} else if (key == "jobtitle") {
			    work.put("position", json.get(key));
			} else if (key == "url") {
			    work.put("website", json.get(key));
			} else if (key == "date_start") {
			    work.put("startDate", json.get(key));
			} else if (key == "date_end") {
			    work.put("endDate", json.get(key));
			} else if (key == "text") {
			    work.put("summary", json.get(key));
			} else {
			    work.put("summary", json.get(key));
			}
		    }
		}
		if (!work.isEmpty()) {
		    workJSON.add(work);
		}
	    }
	}
	JSONArray resultJSON = new JSONArray();
	for (int i = 0; i < workJSON.size(); i++) {
	    if (!workJSON.get(i).equals(null)) {
		resultJSON.add(workJSON.get(i));
	    }
	}
	return resultJSON;
    }

    public JSONArray getInterests(JSONObject obj) {
	if (obj.containsKey("misc")) {
	    JSONArray arr = new JSONArray();
	    arr = (JSONArray) obj.get("misc");
	    if (arr != null) {
		InterestsArr = arr;
	    }
	}
	return InterestsArr;
    }

    public JSONArray getEducation(JSONObject obj) {
	JSONArray educationArr = new JSONArray();
	if (obj.containsKey("basics")) {
	    if (obj.containsKey("education_and_training")) {
		JSONArray arr = new JSONArray();
		arr = (JSONArray) obj.get("education_and_training");
		if (arr != null) {
		    educationArr = arr;
		}
	    }
	}
	return educationArr;
    }

    public JSONArray getSkills(JSONObject obj) {
	if (obj.containsKey("basics")) {
	    if (obj.containsKey("skills")) {
		JSONArray arr = new JSONArray();
		arr = (JSONArray) obj.get("skills");
		if (arr != null) {
		    skillsArr = arr;
		}
	    }
	} else {
	    if (obj.containsKey("skills")) {
		JSONArray arr = new JSONArray();
		arr = (JSONArray) obj.get("skills");
		if (arr != null) {
		    skillsArr = arr;
		}
	    }
	}
	return skillsArr;
    }
}
