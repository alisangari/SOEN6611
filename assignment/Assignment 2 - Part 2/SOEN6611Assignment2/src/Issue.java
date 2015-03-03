import java.util.HashMap;
import java.util.Map;

public class Issue {

	private String attachment = "";
	private String cc = "";
	private String closeDate = "";
	private String id = "";
	private String os = "";
	private String owner = "";
	private String priority = "";
	private String reportDate = "";
	private String status = "";
	private String type = "";

	public Issue() {
	}

	public Issue(String id, String status, String owner, String closeDate,
			String reportDate, String type, String priority, String os,
			String attachment, String cc) {
		super();
		this.id = id;
		this.status = status;
		this.owner = owner;
		this.closeDate = closeDate;
		this.reportDate = reportDate;
		this.type = type;
		this.priority = priority;
		this.os = os;
		this.attachment = attachment;
		this.cc = cc;
	}

	public Map<String, String> getFields() {
		Map<String, String> fields = new HashMap<String, String>();
		fields.put("id", getId());
		fields.put("attachment", getAttachment());
		fields.put("cc", getCc());
		fields.put("closeDate", getCloseDate());
		fields.put("os", getOs());
		fields.put("owner", getOwner());
		fields.put("priority", getPriority());
		fields.put("reportDate", getReportDate());
		fields.put("status", getStatus());
		fields.put("type", getType());
		return fields;
	}

	public String getAttachment() {
		return attachment;
	}

	public String getCc() {
		return cc;
	}

	public String getCloseDate() {
		return closeDate;
	}

	public String getId() {
		return id;
	}

	public String getOs() {
		return os;
	}

	public String getOwner() {
		return owner;
	}

	public String getPriority() {
		return priority;
	}

	public String getReportDate() {
		return reportDate;
	}

	public String getStatus() {
		return status;
	}

	public String getType() {
		return type;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setType(String type) {
		this.type = type;
	}

}
