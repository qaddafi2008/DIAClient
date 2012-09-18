package cn.edu.pku.ss.exception;

public class NoPermissionException extends Exception {
	public NoPermissionException(){
		super("You don't have the permission to do this action!");
	}
}
