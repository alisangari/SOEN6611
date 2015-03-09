import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AttributeExtractor {

	public static String extractReportDate(String file) {
		try {
			String pattern = "Reported by";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(file);
			if (m.find()) {
				String subset1 = file.substring(m.end());
				r = Pattern.compile("title=\"");
				Matcher m1 = r.matcher(subset1);
				if (m1.find()) {
					String subset2 = subset1.substring(m1.end());
					r = Pattern.compile("\"");
					Matcher m2 = r.matcher(subset2);
					if (m2.find()) {
						return (subset2.substring(0, m2.start())).trim();
					}
				}
			}
			// Tue Sep 2 12:19:25 2008
		} catch (Exception e) {
		}
		return "";
	}

	public static String extractCloseDate(String file) {
		try {
			String pattern = "Closed:";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(file);
			if (m.find()) {
				String subset1 = file.substring(m.end());
				r = Pattern.compile("\">");
				Matcher m1 = r.matcher(subset1);
				if (m1.find()) {
					String subset2 = subset1.substring(m1.end());
					r = Pattern.compile("<");
					Matcher m2 = r.matcher(subset2);
					if (m2.find()) {
						return (subset2.substring(0, m2.start())).trim();
					}
				}
			}
			// Tue Sep 2 12:19:25 2008
		} catch (Exception e) {
		}
		return "";
	}

	public static String extractStatus(String file) {
		try {
			String pattern = "Status:";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(file);
			if (m.find()) {
				String subset1 = file.substring(m.end());
				r = Pattern.compile("<span");
				Matcher m1 = r.matcher(subset1);
				if (m1.find()) {
					String subset2 = subset1.substring(m1.end());
					r = Pattern.compile(">");
					Matcher m2 = r.matcher(subset2);
					if (m2.find()) {
						String subset3 = subset2.substring(m2.end());
						r = Pattern.compile("<");
						Matcher m3 = r.matcher(subset3);
						if (m3.find()) {
							return (subset3.substring(0, m3.start())).trim();
						}
					}
				}
			}
		} catch (Exception e) {
		}
		return "";
	}

	public static String extractAttachment(String file) {
		try {
			String pattern = "Attachment";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(file);
			if (m.find()) {
				String subset1 = file.substring(m.end());
				r = Pattern.compile("\">");
				Matcher m1 = r.matcher(subset1);
				if (m1.find()) {
					String subset2 = subset1.substring(m1.end());
					r = Pattern.compile("<");
					Matcher m2 = r.matcher(subset2);
					if (m2.find()) {
						return (subset2.substring(0, m2.start())).trim();
					}
				}
			}
			// Tue Sep 2 12:19:25 2008
		} catch (Exception e) {
		}
		return "";
	}

	public static boolean ccExists(String file) {
		try {
			String pattern = "Cc:";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(file);
			if (m.find()) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static List<String> extractCc(String fileName) {
		try {
			File html = FileReader.getFile(fileName);
			Document document = Jsoup.parse(html, "UTF-8");
			Elements tableElements = document.body()
					.getElementById("issuemeta").getElementsByTag("table");
			Elements anchors = tableElements.iterator().next()
					.getElementsByTag("td").get(3).children();
			List<String> emails = new ArrayList<String>();
			for (Element anchor : anchors) {
				emails.add(anchor.ownText());
			}
			return emails;
		} catch (IOException e) {
//			e.printStackTrace();
		} catch (Exception e){
//			e.printStackTrace();
		}
		return null;
	}

	// public HashMap<String, String> extractPeople(File html){
	// HashMap<String, String> emails = new HashMap<String, String>();
	//
	// for (Entry element : extractOwner(html)) {
	// if(emails.containsKey(element.get))
	// emails.put(anchor.ownText(),"");
	// }
	// return emails;
	// } catch (IOException e) {
	// e.printStackTrace();
	// return null;
	// }
	// }
	//
	public static HashMap<String, String> extractOwner(String file) {
		HashMap<String, String> owner = new HashMap<String, String>();
		try {
			String pattern = "Owner:";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(file);
			if (m.find()) {
				String subset1 = file.substring(m.end());
				pattern = "[A-Za-z0-9._@]";
				Pattern r2 = Pattern.compile(pattern);
				Matcher m2 = r2.matcher(subset1);
				if (m2.find()) {
					owner.put((subset1.substring(0, m2.start())).trim(),
							extractReportDate(file));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return owner;
	}

	public HashMap<String, String> extractConversationContributers(File html) {

		return null;
	}
}
