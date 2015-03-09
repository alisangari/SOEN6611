import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileReader {

	public FileReader() {

	}

	public Map<String, String> readFiles(ArrayList<String> fileNames,
			int startIndex, int endIndex) {
		Map<String, String> files = new HashMap<String, String>();
		String fileContent = "";
		for (int i = startIndex; i < endIndex; i++) {
			fileContent = readFile(Constants.FILE_LOCATION_ON_DISK
					+ fileNames.get(i));
			if (!fileContent.trim().equalsIgnoreCase("")) {
				files.put(fileNames.get(i), fileContent);
			}
		}
		return files;
	}

	public static String readFile(String fileName) {
		StringBuilder sb = new StringBuilder();
		try {
			Scanner sc = new Scanner(new File(fileName));
			while (sc.hasNextLine()) {
				sb.append(sc.nextLine());
			}
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static File getFile(String fileName){
        return new File(Constants.FILE_LOCATION_ON_DISK + fileName);
	}

	public static ArrayList<String> getFilesList() {
		ArrayList<String> fileNames = new ArrayList<String>();
		File folder = new File(Constants.FILE_LOCATION_ON_DISK);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			fileNames.add(file.getName());
		}
		return fileNames;
	}

}
