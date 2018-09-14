package com.hjhq.teamface.common.bean;


public class Contact {
	public Contact() {

	}

	public Contact(String name, String number, String sortKey) {
		this.name = name;
		this.number = number;
		this.sortKey = sortKey;
		if(number!=null){
			this.simpleNumber=number.replaceAll("\\-|\\s", "");
		}
	}

	public String name;
	public String number;
	public String simpleNumber;
	public String sortKey;
	public boolean isAdded;
	public String photo;
	public long id;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((sortKey == null) ? 0 : sortKey.hashCode());
		return result;
	}

	public boolean isAdded() {
		return isAdded;
	}

	public void setAdded(boolean added) {
		isAdded = added;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (sortKey == null) {
			if (other.sortKey != null)
				return false;
		} else if (!sortKey.equals(other.sortKey))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getSimpleNumber() {
		return simpleNumber;
	}

	public void setSimpleNumber(String simpleNumber) {
		this.simpleNumber = simpleNumber;
	}

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
