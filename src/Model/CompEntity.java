package Model;

import cn.edu.pku.ss.bean.Kind;

public abstract class CompEntity extends ContextEntity{
	String name;
	User user;
	Kind kind;
	Location location;
	abstract ContextEntity getvalue() ;
}
