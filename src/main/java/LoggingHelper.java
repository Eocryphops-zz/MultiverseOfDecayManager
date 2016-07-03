
public class LoggingHelper {

	/**
	 * Objective:<br>
	 * Provide a simplified wrapper for logging<br>
	 * <br>
	 * Note: Could use an external lib, but this will slim the distro
	 * @param stringToWrap
	 * @return
	 */
	public void wrap(String stringToWrap, String stringToAppend) {
		System.out.println(
				String.format("[%s] - %s", stringToWrap, stringToAppend));
	}
	
	public void FIRST(String stringToPrint) {
		wrap("First", stringToPrint);
	}
	
	public void SCENARIO(String stringToPrint) {
		wrap("Scenario", stringToPrint);
	}
	
	public void GIVEN(String stringToPrint) {
		wrap("Given", stringToPrint);
	}
	
	public void THEN(String stringToPrint) {
		wrap("Then", stringToPrint);
	}
	
	public void AND(String stringToPrint) {
		wrap("And", stringToPrint);
	}
	
	public void LASTLY(String stringToPrint) {
		// Finally is protected - but I think this reads better
		wrap("Finally", stringToPrint);
	}
}
