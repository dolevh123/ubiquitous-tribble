import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.Placemark;

public class csvToKml extends csvWriter {

	private ArrayList<String[]> csvList;
	
	//Constructor.
	public csvToKml(String sourceFolder, String destinationFile) {
		super(sourceFolder, destinationFile);
	}

	// CSV to KML function using JAK API.
	public void writeFileKML() {
		try {
			csvToArrayList(getDestinationFile());
			Kml kml = KmlFactory.createKml(); // creating a new instance.
			Document document = kml.createAndSetDocument().withName("Placemarks");

			for (int i = 1; i < csvList.size(); i++) { // looping all elements in ArrayList.
				String[] s = csvList.get(i);
				String timeStampSimpleExtension = s[0];
				// create <Placemark> and set points and values.
				Placemark placemark = KmlFactory.createPlacemark();
				placemark.createAndSetTimeStamp().addToTimeStampSimpleExtension(timeStampSimpleExtension);
				document.createAndAddPlacemark().withName("Placemark" + i).withOpen(Boolean.TRUE)
				.withTimePrimitive(placemark.getTimePrimitive()).createAndSetPoint()
				.addToCoordinates(Double.parseDouble(s[3]), Double.parseDouble(s[2]), Double.parseDouble(s[4]));
			}
			kml.marshal(new File(getSourceFolder() + "\\newKml.kml")); // create file.
			System.out.println("KML file writing was successful.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Converting CSV file to an ArrayList. used by writeFileKML().
	private void csvToArrayList(String path) {
		// Reads CSV file from string input, than transfers all information to ArrayList.
		ArrayList<String[]> csvList = new ArrayList<String[]>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			String line;
			while ((line = br.readLine()) != null) {
				String[] entries = line.split(COMMA);
				csvList.add(entries);
			}
			br.close();
			System.out.println("CSV successfully converted to an ArrayList");
		} catch (IOException e) {
			System.out.println("CSV Reader Error.");
			e.printStackTrace();
		}
		this.csvList = csvList;
	}
}
