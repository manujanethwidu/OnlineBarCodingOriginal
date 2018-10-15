package ob.app.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

import javax.swing.JOptionPane;

public class PublicIPGetter {
	public static String getPuplicIP() {
		String systemipaddress = "";
		try {

			URL url_name = new URL("http://bot.whatismyipaddress.com");
			BufferedReader sc = new BufferedReader(new InputStreamReader(url_name.openStream()));
			systemipaddress = sc.readLine().trim();
			if (!(systemipaddress.length() > 0)) {
				InetAddress localhost = InetAddress.getLocalHost();
				systemipaddress = (localhost.getHostAddress()).trim();
			}

		} catch (IOException e) {
			//JOptionPane.showMessageDialog(null, "connection issue");
		}
		return systemipaddress;
	}
}
