package com.csw.programentity;

/**
 * 节目信息
 * @author lgj
 *
 */
public class ProgramInfo {
	private String programId;
	private String programName;
	private String programUrl;

	public ProgramInfo(String programId, String programName, String programUrl) {
		super();
		this.programId = programId;
		this.programName = programName;
		this.programUrl = programUrl;
	}

	public ProgramInfo() {
		super();
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getProgramUrl() {
		return programUrl;
	}

	public void setProgramUrl(String programUrl) {
		this.programUrl = programUrl;
	}

}
