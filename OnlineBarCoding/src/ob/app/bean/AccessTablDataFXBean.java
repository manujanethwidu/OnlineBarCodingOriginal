package ob.app.bean;

import java.util.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AccessTablDataFXBean {

	private final StringProperty config;
	private final StringProperty tyresize;
	private final StringProperty tyrerim;
	private final StringProperty tyretype;
	private final StringProperty brand;
	private final StringProperty sidewall;
	private final StringProperty stencilno;
	private final StringProperty barcode;
	private final StringProperty qualitygrade;
	private final StringProperty remarks;
	private final ObjectProperty<Date> dateofmanufacture;
	private final StringProperty Processid;
	private final StringProperty tyrestatus;
	private final StringProperty DispatchStatus;
	private final ObjectProperty<Date> CreateDate;
	private final ObjectProperty<Date> Modifydatte;
	private final IntegerProperty Status;
	private final IntegerProperty MergeStatus;
	private final StringProperty PrevProcessid;

	public AccessTablDataFXBean() {
		this(null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, 0, 0);
	}

	public AccessTablDataFXBean(String configx, String tyresizex, String tyrerimx, String tyretypex, String brandx,
			String sidewallx, String stencilnox, String barcodex, String qualitygradex, String remarksx,
			Date dateofmanufacturex, String Processidx,String PrevProcessidx, String tyrestatusx, String DispatchStatusx, Date CreateDatex,
			Date Modifydattex, int Statusx, int MergeStatusx) {
		this.config = new SimpleStringProperty(configx);
		this.tyresize = new SimpleStringProperty(tyresizex);
		this.tyrerim = new SimpleStringProperty(tyrerimx);
		this.tyretype = new SimpleStringProperty(tyretypex);
		this.brand = new SimpleStringProperty(brandx);
		this.sidewall = new SimpleStringProperty(sidewallx);
		this.stencilno = new SimpleStringProperty(stencilnox);
		this.barcode = new SimpleStringProperty(barcodex);
		this.qualitygrade = new SimpleStringProperty(qualitygradex);
		this.remarks = new SimpleStringProperty(remarksx);
		this.dateofmanufacture = new SimpleObjectProperty<Date>(dateofmanufacturex);
		this.Processid = new SimpleStringProperty(Processidx);
		this.tyrestatus = new SimpleStringProperty(tyrestatusx);
		this.DispatchStatus = new SimpleStringProperty(DispatchStatusx);
		this.CreateDate = new SimpleObjectProperty<Date>(CreateDatex);
		this.Modifydatte = new SimpleObjectProperty<Date>(Modifydattex);
		this.Status = new SimpleIntegerProperty(Statusx);
		this.MergeStatus = new SimpleIntegerProperty(MergeStatusx);
		this.PrevProcessid = new SimpleStringProperty(PrevProcessidx);
	}

	public final StringProperty configProperty() {
		return this.config;
	}

	public final String getConfig() {
		return this.configProperty().get();
	}

	public final void setConfig(final String config) {
		this.configProperty().set(config);
	}

	public final StringProperty tyresizeProperty() {
		return this.tyresize;
	}

	public final String getTyresize() {
		return this.tyresizeProperty().get();
	}

	public final void setTyresize(final String tyresize) {
		this.tyresizeProperty().set(tyresize);
	}

	public final StringProperty tyrerimProperty() {
		return this.tyrerim;
	}

	public final String getTyrerim() {
		return this.tyrerimProperty().get();
	}

	public final void setTyrerim(final String tyrerim) {
		this.tyrerimProperty().set(tyrerim);
	}

	public final StringProperty tyretypeProperty() {
		return this.tyretype;
	}

	public final String getTyretype() {
		return this.tyretypeProperty().get();
	}

	public final void setTyretype(final String tyretype) {
		this.tyretypeProperty().set(tyretype);
	}

	public final StringProperty brandProperty() {
		return this.brand;
	}

	public final String getBrand() {
		return this.brandProperty().get();
	}

	public final void setBrand(final String brand) {
		this.brandProperty().set(brand);
	}

	public final StringProperty sidewallProperty() {
		return this.sidewall;
	}

	public final String getSidewall() {
		return this.sidewallProperty().get();
	}

	public final void setSidewall(final String sidewall) {
		this.sidewallProperty().set(sidewall);
	}

	public final StringProperty stencilnoProperty() {
		return this.stencilno;
	}

	public final String getStencilno() {
		return this.stencilnoProperty().get();
	}

	public final void setStencilno(final String stencilno) {
		this.stencilnoProperty().set(stencilno);
	}

	public final StringProperty barcodeProperty() {
		return this.barcode;
	}

	public final String getBarcode() {
		return this.barcodeProperty().get();
	}

	public final void setBarcode(final String barcode) {
		this.barcodeProperty().set(barcode);
	}

	public final StringProperty qualitygradeProperty() {
		return this.qualitygrade;
	}

	public final String getQualitygrade() {
		return this.qualitygradeProperty().get();
	}

	public final void setQualitygrade(final String qualitygrade) {
		this.qualitygradeProperty().set(qualitygrade);
	}

	public final StringProperty remarksProperty() {
		return this.remarks;
	}

	public final String getRemarks() {
		return this.remarksProperty().get();
	}

	public final void setRemarks(final String remarks) {
		this.remarksProperty().set(remarks);
	}

	public final ObjectProperty<Date> dateofmanufactureProperty() {
		return this.dateofmanufacture;
	}

	public final Date getDateofmanufacture() {
		return this.dateofmanufactureProperty().get();
	}

	public final void setDateofmanufacture(final Date dateofmanufacture) {
		this.dateofmanufactureProperty().set(dateofmanufacture);
	}

	public final StringProperty ProcessidProperty() {
		return this.Processid;
	}

	public final String getProcessid() {
		return this.ProcessidProperty().get();
	}

	public final void setProcessid(final String Processid) {
		this.ProcessidProperty().set(Processid);
	}

	public final StringProperty tyrestatusProperty() {
		return this.tyrestatus;
	}

	public final String getTyrestatus() {
		return this.tyrestatusProperty().get();
	}

	public final void setTyrestatus(final String tyrestatus) {
		this.tyrestatusProperty().set(tyrestatus);
	}

	public final StringProperty DispatchStatusProperty() {
		return this.DispatchStatus;
	}

	public final String getDispatchStatus() {
		return this.DispatchStatusProperty().get();
	}

	public final void setDispatchStatus(final String DispatchStatus) {
		this.DispatchStatusProperty().set(DispatchStatus);
	}

	public final ObjectProperty<Date> CreateDateProperty() {
		return this.CreateDate;
	}

	public final Date getCreateDate() {
		return this.CreateDateProperty().get();
	}

	public final void setCreateDate(final Date CreateDate) {
		this.CreateDateProperty().set(CreateDate);
	}

	public final ObjectProperty<Date> ModifydatteProperty() {
		return this.Modifydatte;
	}

	public final Date getModifydatte() {
		return this.ModifydatteProperty().get();
	}

	public final void setModifydatte(final Date Modifydatte) {
		this.ModifydatteProperty().set(Modifydatte);
	}

	public final IntegerProperty StatusProperty() {
		return this.Status;
	}

	public final int getStatus() {
		return this.StatusProperty().get();
	}

	public final void setStatus(final int Status) {
		this.StatusProperty().set(Status);
	}

	public final IntegerProperty MergeStatusProperty() {
		return this.MergeStatus;
	}

	public final int getMergeStatus() {
		return this.MergeStatusProperty().get();
	}

	public final void setMergeStatus(final int MergeStatus) {
		this.MergeStatusProperty().set(MergeStatus);
	}

	public final StringProperty PrevProcessidProperty() {
		return this.PrevProcessid;
	}
	

	public final String getPrevProcessid() {
		return this.PrevProcessidProperty().get();
	}
	

	public final void setPrevProcessid(final String PrevProcessid) {
		this.PrevProcessidProperty().set(PrevProcessid);
	}
	

}
