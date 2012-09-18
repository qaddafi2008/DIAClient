package Model;

import cn.edu.pku.ss.GasLevel;
import cn.edu.pku.ss.Humidity;
import cn.edu.pku.ss.Light;
import cn.edu.pku.ss.Temperature;

public class Entironment extends ContextEntity{
	Temperature temperature;
	Humidity humidity;
	GasLevel gaslevel;
	Light light;
	Location location;
}
