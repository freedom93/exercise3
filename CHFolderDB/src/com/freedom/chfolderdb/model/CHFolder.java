package com.freedom.chfolderdb.model;

public class CHFolder {

	private Integer hashid;
	
	private String folder_name;
	
	private Integer folder_size;
	
	public CHFolder(String folder_name, Integer folder_size){
		this.folder_name = folder_name;
		this.folder_size = folder_size;
		this.hashid = hashCode();
	}
	public CHFolder(Integer hashid, String folder_name, Integer folder_size){
		this.folder_name = folder_name;
		this.folder_size = folder_size;
		this.hashid = hashid;
	}

	public Integer getHashid() {
		return hashid;
	}

	public void setHashid(Integer hashid) {
		this.hashid = hashid;
	}

	public String getFolder_name() {
		return folder_name;
	}

	public void setFolder_name(String folder_name) {
		this.folder_name = folder_name;
	}

	public Integer getFolder_size() {
		return folder_size;
	}

	public void setFolder_size(Integer folder_size) {
		this.folder_size = folder_size;
	}

	@Override
	public String toString() {
		return "CHFolder [hashid=" + hashid + ", folder_name=" + folder_name
				+ ", folder_size=" + folder_size + "]";
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((folder_name == null) ? 0 : folder_name.hashCode());
		result = prime * result
				+ ((folder_size == null) ? 0 : folder_size.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CHFolder other = (CHFolder) obj;
		if (hashid == null) {
			if (other.hashid != null)
				return false;
		} else if(!hashid.equals(other.hashid))
			return false;
		if (folder_name == null) {
			if (other.folder_name != null)
				return false;
		} else if (!folder_name.equals(other.folder_name))
			return false;
		if (folder_size == null) {
			if (other.folder_size != null)
				return false;
		} else if (!folder_size.equals(other.folder_size))
			return false;
		return true;
	}
	
	

}
