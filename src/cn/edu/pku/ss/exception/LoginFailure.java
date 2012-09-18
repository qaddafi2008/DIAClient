package cn.edu.pku.ss.exception;

public class LoginFailure extends Exception {
	public LoginFailure(){
		super("Fail to login!");
	}
}
