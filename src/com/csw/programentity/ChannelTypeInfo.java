package com.csw.programentity;

import java.util.List;

/**
 * 节目类型
 * 
 * @author lgj
 * 
 */
public class ChannelTypeInfo {
	private String channelTypeId;
	private String channelTypeName;
	private List<ProgramInfo> programInfoList;

	public ChannelTypeInfo(String channelTypeId, String channelTypeName,
			List<ProgramInfo> programInfoList) {
		super();
		this.channelTypeId = channelTypeId;
		this.channelTypeName = channelTypeName;
		this.programInfoList = programInfoList;
	}

	public ChannelTypeInfo() {
		super();
	}

	public String getChannelTypeId() {
		return channelTypeId;
	}

	public void setChannelTypeId(String channelTypeId) {
		this.channelTypeId = channelTypeId;
	}

	public String getChannelTypeName() {
		return channelTypeName;
	}

	public void setChannelTypeName(String channelTypeName) {
		this.channelTypeName = channelTypeName;
	}

	public List<ProgramInfo> getProgramInfoList() {
		return programInfoList;
	}

	public void setProgramInfoList(List<ProgramInfo> programInfoList) {
		this.programInfoList = programInfoList;
	}

}
