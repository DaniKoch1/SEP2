package model;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//TO DO, class not finished
public enum Class implements Serializable{
	 First, Second, Third, Fourth, Fifth, Sixth, Seventh, Eigth;
	
	public static List<String> getClasses() {
		return Stream.of(Class.values()).map(Enum::name).collect(Collectors.toList());
	}
}

