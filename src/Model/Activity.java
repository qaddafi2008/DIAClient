package Model;

import java.util.Date;
import java.util.List;

public class Activity extends ContextEntity{
	String name;
	String description;
	Location location;
	Date startTime;
	Date endTime;
	List<User> users;
	
}
