package Model;

import java.util.List;

public class User extends ContextEntity{
	String username;
	String password;
	Location home;
	List<CompEntity> compEntities;
	Location nowLocation;
	Activity activity;
	Process process;
	
	public Location getHome() {
		return home;
	}
	public void setHome(Location home) {
		this.home = home;
	}

	public void setCompEntities(List<CompEntity> compEntities) {
		this.compEntities = compEntities;
	}
	public Location getNowLocation() {
		return nowLocation;
	}
	
	public Activity getActivity() {
		return activity;
	}
	
	public Process getProcess() {
		return process;
	}
	public ContextEntity[] getContextEntityOfUser() {
		return null;
	}

}
