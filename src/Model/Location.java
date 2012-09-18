package Model;

import java.util.List;

public class Location extends ContextEntity{
	String zoneName;
	String description;
	List<Double> coordinates; 
	Location superLocation;
	Location subLocation;
	List<ContextEntity> contextEntities;
	public ContextEntity[] getContextEntityOfUser() {
		return null;
	}
	public User[] getAllPeople() {
		return null;
	}

}
