package dom.simple;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class NMEA {
	

	interface SentenceParser {
		public boolean parse(String [] tokens, GPSPosition position);
	}
	
	// utils
	static float Latitude2Decimal(String lat, String NS) {
		float med = Float.parseFloat(lat.substring(2))/60.0f;
		med +=  Float.parseFloat(lat.substring(0, 2));
		if(NS.startsWith("S")) {
			med = -med;
		}
		return med;
	}
 
	static float Longitude2Decimal(String lon, String WE) {
		float med = Float.parseFloat(lon.substring(3))/60.0f;
		med +=  Float.parseFloat(lon.substring(0, 3));
		if(WE.startsWith("W")) {
			med = -med;
		}
		return med;
	}
	
	
	//$GPRMC,024318.00,A,3857.08169,S,06801.47072,W,6.507,129.52,171214,,,A*62
	public static Date stringNMEAToDate (String gpll) throws ParseException {
		
		String time = gpll.split(",")[1].substring(0, 2)+" "+gpll.split(",")[1].substring(2, 4)+" "+gpll.split(",")[1].substring(4, 6)+" "+gpll.split(",")[9].substring(0, 2)+"/"+gpll.split(",")[9].substring(2, 4)+"/20"+gpll.split(",")[9].substring(4, 6);
		SimpleDateFormat formatter = new SimpleDateFormat("HH mm ss dd/MM/yyyy");
		Date fech = formatter.parse(time);
		return fech;
	}

	//"2014-12-17 02:45:00.641906000","ff:ff:ff:ff:ff:ff","ff:ff:ff:ff:ff:ff","8c:3a:e3:10:60:45","8c:3a:e3:10:60:45","ff:ff:ff:ff:ff:ff",""
	public static Date stringMACtoDate (String line) throws ParseException
	{
		String time = line.split(",")[0].substring(0, 18);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date fech = formatter.parse(time);
		return fech;
		
	}
	
 
	// parsers 
	class GPGGA implements SentenceParser {
		public boolean parse(String [] tokens, GPSPosition position) {
			position.time = Float.parseFloat(tokens[1]);
			position.lat = Latitude2Decimal(tokens[2], tokens[3]);
			position.lon = Longitude2Decimal(tokens[4], tokens[5]);
			position.quality = Integer.parseInt(tokens[6]);
			position.altitude = Float.parseFloat(tokens[9]);
			return true;
		}
	}
	
	class GPGGL implements SentenceParser {
		public boolean parse(String [] tokens, GPSPosition position) {
			position.lat = Latitude2Decimal(tokens[1], tokens[2]);
			position.lon = Longitude2Decimal(tokens[3], tokens[4]);
			position.time = Float.parseFloat(tokens[5]);
			return true;
		}
	}
	
	class GPRMC implements SentenceParser {
		public boolean parse(String [] tokens, GPSPosition position) {
			position.time = Float.parseFloat(tokens[1]);
			position.lat = Latitude2Decimal(tokens[3], tokens[4]);
			position.lon = Longitude2Decimal(tokens[5], tokens[6]);
			position.velocity = Float.parseFloat(tokens[7]);
			position.dir = Float.parseFloat(tokens[8]);
			return true;
		}
	}
	
	class GPVTG implements SentenceParser {
		public boolean parse(String [] tokens, GPSPosition position) {
			position.dir = Float.parseFloat(tokens[3]);
			return true;
		}
	}
	
	class GPRMZ implements SentenceParser {
		public boolean parse(String [] tokens, GPSPosition position) {
			position.altitude = Float.parseFloat(tokens[1]);
			return true;
		}
	}
	
	public class GPSPosition {
		public float time = 0.0f;
		public float lat = 0.0f;
		public float lon = 0.0f;
		public boolean fixed = false;
		public int quality = 0;
		public float dir = 0.0f;
		public float altitude = 0.0f;
		public float velocity = 0.0f;
		
		public void updatefix() {
			fixed = quality > 0;
		}
		
		public String toString() {
			return String.format("POSITION: lat: %f, lon: %f, time: %f, Q: %d, dir: %f, alt: %f, vel: %f", lat, lon, time, quality, dir, altitude, velocity);
		}
	}
	
	GPSPosition position = new GPSPosition();
	
	private static final Map<String, SentenceParser> sentenceParsers = new HashMap<String, SentenceParser>();
	
    public NMEA() {
    	sentenceParsers.put("GPGGA", new GPGGA());
    	sentenceParsers.put("GPGGL", new GPGGL());
    	sentenceParsers.put("GPRMC", new GPRMC());
    	sentenceParsers.put("GPRMZ", new GPRMZ());
    	//only really good GPS devices have this sentence but ...
    	sentenceParsers.put("GPVTG", new GPVTG());
    }
    
	public GPSPosition parse(String line) {
		
		if(line.startsWith("$")) {
			String nmea = line.substring(1);
			String[] tokens = nmea.split(",");
			String type = tokens[0];
			//TODO check crc
			if(sentenceParsers.containsKey(type)) {
				sentenceParsers.get(type).parse(tokens, position);
			}
			position.updatefix();
		}
		
		return position;
	}
}
