package kankan.wheel.widget.regionselect.model;

import java.io.Serializable;

public class ResultForGuild implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2164900716684507296L;

	private String jobName;

	private String jobID;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobID() {
		return jobID;
	}

	public void setJobID(String jobID) {
		this.jobID = jobID;
	}

	@Override
	public String toString() {
		return "ResultForGuild [jobName=" + jobName + ", jobID=" + jobID + "]";
	}

}