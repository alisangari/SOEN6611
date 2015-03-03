import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public static String extractCc(String file) {
		try {
			String pattern = "Cc:";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(file);
			if (m.find()) {
				return "true";
			}
		} catch (Exception e) {
		}
		return "";
	}

}
