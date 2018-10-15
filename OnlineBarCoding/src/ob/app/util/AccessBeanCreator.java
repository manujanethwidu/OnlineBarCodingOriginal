package ob.app.util;

import java.sql.Connection;

import javax.swing.JOptionPane;

import ob.app.bean.AccessBean;
import ob.app.bean.BCPendingBean;
import ob.app.bean.BasicDatafromTireCodeBean;
import ob.app.bean.FIReportBean;
import ob.app.db.CreateConn;
import ob.app.tbl.AccessFile;
import ob.app.tbl.BCPendingTbl;

public class AccessBeanCreator {
	// Create complete access bean and return
	public AccessBean CreateAccessBean(int sn, int pidNw, int pidOld, String qg, boolean rbc) {
		// qg is seperately sent since Both A+ and A tires should be barcodes as A;
		try {
			AccessFile accessFile = new AccessFile();
			AccessBean accessBean = new AccessBean();
			// Create ProcessIDs(New and Old)
			String processIdNw = PIDAccessCreator.creasteAccessPID(pidNw);
			String processIdOld = PIDAccessCreator.creasteAccessPID(pidOld);
			String barCode = "";
			// Create StencilNo
			String stencilNo = "L" + sn;
			// Crate BarCode
			barCode = processIdNw + stencilNo + qg;
			// Get data from accessfile.PIDDetails table and set
			accessBean = accessFile.getAccessBean(processIdNw);
			// Setting General Data to Bean
			accessBean.setBarcode(barCode);
			accessBean.setQualitygrade(qg);
			accessBean.setRemarks("0");
			accessBean.setProcessIDnw(processIdNw);
			accessBean.setProcessIDOld(processIdOld);
			accessBean.setTyrestatus("No");
			accessBean.setStencilno(stencilNo);
			accessBean.setDispatchStatus("Stock");
			accessBean.setMergeStatus(1);
			// rebacode status =2

			// If rebc set Status to 2
			if (rbc)
				accessBean.setStatus(2);
			else
				accessBean.setStatus(1);
			
			
			// Fresh tires without TTS Update
			boolean blnFreshBC = false;
			boolean snAvlAccess = accessFile.serialNoAvailablityCheck(accessBean.getStencilno());
			CreateConn createConnx = new CreateConn();
			try (Connection conn = createConnx.GetConn()) {
				if (!snAvlAccess) {
					SNAvlCheck sna = new SNAvlCheck();
					blnFreshBC = sna.snAVlCheckinAllTbls(conn, sn);
				}

			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "AccessFileUpdateController.uploadAccessFileBCPending() " + e);
			}
			// If fresh tire without update access file status is1
			if (blnFreshBC)
				accessBean.setStatus(1);
			return accessBean;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "AccessBeanCreator.AccessBean()" + e);
			return null;
		}
	}
}
