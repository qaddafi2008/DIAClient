package cn.edu.pku.ss.exception;

public class MessageFormatError extends Exception {
	public MessageFormatError(){
		super("The message format is not right!");
	}
}
