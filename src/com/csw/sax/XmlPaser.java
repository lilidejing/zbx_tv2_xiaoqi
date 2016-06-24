package com.csw.sax;


import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.csw.programentity.ChannelTypeInfo;
import com.csw.programentity.ProgramInfo;

public class XmlPaser extends DefaultHandler {

	private ChannelTypeInfo channelTypeInfoEntity;
	private List<ChannelTypeInfo> channelTpyeInfoList=new ArrayList<ChannelTypeInfo>();

	private ProgramInfo programInfoEntity;
	private List<ProgramInfo> programInfoEntityList = new ArrayList<ProgramInfo>();

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		
	}

	/*public XmlPaser(List<ProgramInfo> programInfoEntityList) {
		this.programInfoEntityList = programInfoEntityList;
	}*/

	public XmlPaser() {
		super();
		
	}

	public XmlPaser(List<ChannelTypeInfo> channelTpyeInfoList){
		this.channelTpyeInfoList=channelTpyeInfoList;
	}
	
	
	/*public ChannelTypeInfo getChannelTypeInfoEntity() {
		return channelTypeInfoEntity;
	}

	public void setChannelTypeInfoEntity(ChannelTypeInfo channelTypeInfoEntity) {
		this.channelTypeInfoEntity = channelTypeInfoEntity;
	}
*/
	public List<ChannelTypeInfo> getChannelTpyeInfoList() {
		return channelTpyeInfoList;
	}
	public void setChannelTpyeInfoList(List<ChannelTypeInfo> channelTpyeInfoList) {
		this.channelTpyeInfoList = channelTpyeInfoList;
	}


	/*public ProgramInfo getProgramInfoEntity() {
		return programInfoEntity;
	}


	public void setProgramInfoEntity(ProgramInfo programInfoEntity) {
		this.programInfoEntity = programInfoEntity;
	}
*/
	public List<ProgramInfo> getProgramInfoEntityList() {
		return programInfoEntityList;
	}
	public void setProgramInfoEntityList(List<ProgramInfo> programInfoEntityList) {
		this.programInfoEntityList = programInfoEntityList;
	}

	private String tagName = null;
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (tagName != null) {

			if ("program".equals(tagName)) {

				String programUrl = new String(ch, start, length);
				programInfoEntity.setProgramUrl(programUrl);
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		this.tagName=localName;
		if(tagName.equals("channelType")){
			channelTypeInfoEntity.setProgramInfoList(programInfoEntityList);
			channelTpyeInfoList.add(channelTypeInfoEntity);
			programInfoEntityList=new ArrayList<ProgramInfo>();
		}else if (tagName.equals("program")) {
			programInfoEntityList.add(programInfoEntity);
			
		}
		tagName = null;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		this.tagName = localName;
		
		if("channelType".equals(tagName)){
			channelTypeInfoEntity=new ChannelTypeInfo();
			String channelTypeId=attributes.getValue(0);
			String channelTypeName=attributes.getValue(1);
			channelTypeInfoEntity.setChannelTypeId(channelTypeId);
			channelTypeInfoEntity.setChannelTypeName(channelTypeName);
			
		}else if ("program".equals(tagName)) {
			programInfoEntity = new ProgramInfo();
			String programId = attributes.getValue(0);
			String programName=attributes.getValue(1);
			programInfoEntity.setProgramId(programId);
			programInfoEntity.setProgramName(programName);
		}
	}

	
}
