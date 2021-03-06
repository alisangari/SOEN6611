import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Assignment2 {

	public static void main(String[] args) {
		System.out.println("Start at: "
				+ new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
						.format(new java.util.Date()));

		DBManager dbManager = new DBManager();
		dbManager.createDB();
		dbManager.createIssuesTable(new Issue());
		dbManager.createCcsTable();
		FileReader fr;
		ArrayList<Issue> issues;
		ArrayList<String> fileNames = FileReader.getFilesList();
		int fileBatchSize = 1000;
		int totalFileBatches = fileNames.size() / fileBatchSize;
		int startIndex, endIndex;
		for (int i = 0; i < totalFileBatches; i++) {
			issues = new ArrayList<Issue>();
			System.out.println("Reading file batch " + i);
			startIndex = i * fileBatchSize;
			endIndex = i * fileBatchSize + fileBatchSize;
			fr = new FileReader();
			Map<String, String> files = fr.readFiles(fileNames, startIndex,
					endIndex);
			for (Entry<String, String> file : files.entrySet()) {
				Issue issue = new Issue();
				issue.setId(file.getKey());
				issue.setReportDate(AttributeExtractor.extractReportDate(file
						.getValue()));
				issue.setCloseDate(AttributeExtractor.extractCloseDate(file
						.getValue()));
				issue.setStatus(AttributeExtractor.extractStatus(file
						.getValue()));
				if (!issue.getReportDate().equalsIgnoreCase("")) {
					issues.add(issue);
				}

				try{
				if (AttributeExtractor.ccExists(file.getValue())) {
					List<String> ccList = AttributeExtractor.extractCc(file.getKey());
					issue.setCc(((Integer) (ccList.size())).toString());
					dbManager.insertCcs(file.getKey(), ccList);
				}
				} catch (Exception e){
				}
			}
			System.out.print("Iserting batch " + i + " into db...");
			dbManager.sendIssuesToDb(issues);
			System.out.println(" done!");
		}
		dbManager.performPostExtractionActivities();
		System.out.println("End at:   "
				+ new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
						.format(new java.util.Date()));
	}
}
